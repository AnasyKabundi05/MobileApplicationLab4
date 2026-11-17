package com.example.eventhub.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AuthUiState(
    val uid: String? = null,
    val email: String? = null,
    val isBusy: Boolean = false,
    val error: String? = null
)

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val mutableState = MutableStateFlow(AuthUiState(
        uid = auth.currentUser?.uid,
        email = auth.currentUser?.email
    ))
    val uiState = mutableState.asStateFlow()
    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        mutableState.update { it.copy(
            uid = firebaseAuth.currentUser?.uid,
            email = firebaseAuth.currentUser?.email
        ) }
    }
    init { auth.addAuthStateListener(listener) }

    fun login(email: String, password: String) {
        if (mutableState.value.isBusy) return
        if (email.isBlank() || password.isBlank()) {
            mutableState.update { it.copy(error = "Enter email and password") }
            return
        }
        mutableState.update { it.copy(isBusy = true, error = null) }
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                mutableState.update { it.copy(
                    isBusy = false,
                    error = if (task.isSuccessful) null
                    else "Login failed. Check credentials and connection."
                ) }
            }
    }

    fun logout() {
        if (mutableState.value.isBusy) return
        auth.signOut()
        mutableState.value = AuthUiState()
    }

    override fun onCleared() {
        auth.removeAuthStateListener(listener)
        super.onCleared()
    }
}