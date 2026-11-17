package com.example.eventhub

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.eventhub.ui.EventHubViewModel
import com.example.eventhub.ui.components.EventHubMenuShell
import com.example.eventhub.ui.screens.AboutScreen
import com.example.eventhub.ui.screens.EventDetailsScreen
import com.example.eventhub.ui.screens.HelpScreen
import com.example.eventhub.ui.screens.HomeScreen
import com.example.eventhub.ui.screens.ManageEventsScreen
import com.example.eventhub.ui.screens.SettingsScreen
import com.example.eventhub.ui.screens.WelcomeScreen

@Composable
fun EventHubApp(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val application = LocalContext.current.applicationContext as Application
    val model: EventHubViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    )
    val state by model.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedRoute = backStackEntry?.destination?.route

    fun openTopLevel(route: String) {
        navController.navigate(route) {
            popUpTo("welcome") { inclusive = false }
            launchSingleTop = true
        }
    }

    EventHubMenuShell(
        selectedRoute = selectedRoute,
        enabled = !state.isLoading && state.loadError == null,
        onWelcome = { openTopLevel("welcome") },
        onBrowseEvents = { openTopLevel("events") },
        onSettings = { openTopLevel("settings") }
    ) { padding ->
        if (state.isLoading) {
            Column(Modifier.padding(padding).padding(16.dp)) {
                CircularProgressIndicator()
                Text("Loading saved events…")
            }
        } else if (state.loadError != null) {
            Text("Database error: ${state.loadError}",
                Modifier.padding(padding).padding(16.dp))
        } else {
            NavHost(navController = navController,
                startDestination = "welcome",
                modifier = Modifier.padding(padding)) {
                composable("welcome") {
                    WelcomeScreen(
                        onBrowseEvents = { openTopLevel("events") },
                        onAboutEvents = { navController.navigate("about") },
                        onHelpScreen = { navController.navigate("help") }
                    )
                }
                composable("about") {
                    AboutScreen(Back = { navController.popBackStack() })
                }
                composable("help") {
                    HelpScreen(Back = { navController.popBackStack() })
                }
                composable("settings") {
                    SettingsScreen(
                        darkTheme = darkTheme,
                        onDarkThemeChange = onDarkThemeChange
                    )
                }
                composable("home") {
                    HomeScreen(
                        events = state.events,
                        categories = state.categories,
                        onEventOpen = { navController.navigate("details/$it") },
                        onExplore = { openTopLevel("events") }
                    )
                }
                composable("events") {
                    ManageEventsScreen(
                        events = state.events,
                        categories = state.categories,
                        isSaving = state.isAdding,
                        actionMessage = state.actionMessage,
                        onAddEvent = { model.saveEvent(it) },
                        onDeleteEvent = { model.deleteEvent(it) },
                        onEventOpen = { navController.navigate("details/$it") }
                    )
                }
                composable("details/{eventId}",
                    arguments = listOf(navArgument("eventId") {
                        type = NavType.IntType
                    })) { entry ->
                    val id = entry.arguments?.getInt("eventId")
                    val event = state.events.firstOrNull { it.id == id }
                    if (event == null) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Event not found")
                            Button(onClick = { navController.popBackStack() }) {
                                Text("Back")
                            }
                        }
                    } else {
                        val categoryName = state.categories.firstOrNull {
                            it.id == event.categoryId
                        }?.name ?: "Uncategorised"
                        EventDetailsScreen(event = event,
                            categoryName = categoryName,
                            onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}
