package com.example.eventhub.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class AuthUiState(
    val uid: String? = null,
    val email: String? = null,
    val isAdmin: Boolean = false,
    val roleReady: Boolean = false,
    val isBusy: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val mutableState = MutableStateFlow(AuthUiState())
    val uiState = mutableState.asStateFlow()
    private var generation = 0

    private fun loadRole(uid: String, ticket: Int) {
        val reference = firestore.collection("users").document(uid)
        reference.get(Source.SERVER).addOnSuccessListener { document ->
            if (ticket != generation || auth.currentUser?.uid != uid) {
                return@addOnSuccessListener
            }
            if (document.exists()) {
                mutableState.update { it.copy(
                    isAdmin = document.getString("role") == "admin",
                    roleReady = true, error = null
                ) }
            } else {
                // Never assign admin in Android code.
                reference.set(mapOf("role" to "user"))
                    .addOnCompleteListener { task ->
                        if (ticket == generation && auth.currentUser?.uid == uid) {
                            mutableState.update { it.copy(
                                isAdmin = false, roleReady = true,
                                error = if (task.isSuccessful) null
                                else "Account signed in; user profile could not be saved"
                            ) }
                        }
                    }
            }
        }.addOnFailureListener {
            if (ticket == generation && auth.currentUser?.uid == uid) {
                mutableState.update { it.copy(isAdmin = false,
                    roleReady = true, error = "Online role check unavailable") }
            }
        }
    }

    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val ticket = ++generation
        val user = firebaseAuth.currentUser
        mutableState.update { it.copy(
            uid = user?.uid, email = user?.email,
            isAdmin = false, roleReady = user == null, error = null
        ) }
        if (user != null) loadRole(user.uid, ticket)
    }
    init { auth.addAuthStateListener(listener) }

    private suspend fun requireAdmin() {
        val user = FirebaseAuth.getInstance().currentUser
            ?: error("Admin sign in required")
        val document = suspendCancellableCoroutine<DocumentSnapshot> { continuation ->
            FirebaseFirestore.getInstance().collection("users")
                .document(user.uid).get(Source.SERVER)
                .addOnSuccessListener {
                    if (continuation.isActive) continuation.resume(it)
                }
                .addOnFailureListener {
                    if (continuation.isActive) continuation.resumeWithException(it)
                }
        }
        check(FirebaseAuth.getInstance().currentUser?.uid == user.uid &&
                document.getString("role") == "admin") { "Admin access required" }
    }

    fun register(email: String, password: String) {
        if (mutableState.value.isBusy) return
        mutableState.update { it.copy(isBusy = true, error = null) }
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                mutableState.update { it.copy(isBusy = false,
                    error = if (task.isSuccessful) it.error
                    else "Registration failed. Check details and password policy.") }
            }
        // Auth listener creates users/{uid} with role user if absent.
    }

    fun logout() {
        if (mutableState.value.isBusy) return
        auth.signOut()
        mutableState.value = AuthUiState(roleReady = true)
    }

    override fun onCleared() {
        ++generation
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}