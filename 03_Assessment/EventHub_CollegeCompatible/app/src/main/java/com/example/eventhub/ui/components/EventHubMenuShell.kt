package com.example.eventhub.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.eventhub.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventHubMenuShell(
    selectedRoute: String?,
    enabled: Boolean = true,
    onWelcome: () -> Unit = {},
    onBrowseEvents: () -> Unit = {},
    onSettings: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EventHub") },
                actions = {
                    IconButton(enabled = enabled, onClick = onSettings) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Open settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedRoute == "welcome",
                    onClick = onWelcome,
                    enabled = enabled,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_home),
                            contentDescription = "Welcome"
                        )
                    },
                    label = { Text("Welcome") }
                )
                NavigationBarItem(
                    selected = selectedRoute == "events" || selectedRoute == "home",
                    onClick = onBrowseEvents,
                    enabled = enabled,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_events),
                            contentDescription = "Events"
                        )
                    },
                    label = { Text("Events") }
                )
                NavigationBarItem(
                    selected = selectedRoute == "settings",
                    onClick = onSettings,
                    enabled = enabled,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_settings),
                            contentDescription = "Settings"
                        )
                    },
                    label = { Text("Settings") }
                )
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
