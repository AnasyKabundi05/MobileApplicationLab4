package com.example.eventhub.ui.components

import android.R.attr.enabled
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@Composable
fun EventHubMenu(
    // EventHubMenuShell.kt parameter list gains:
    onSettings: () -> Unit,
){
// TopAppBar actions gains:
    actions = {
        IconButton(enabled = enabled, onClick = onSettings) {
            Icon(
                painterResource(R.drawable.ic_settings),
                contentDescription = "Open settings"
            )
        }
    }
}