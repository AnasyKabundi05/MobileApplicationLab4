package com.example.eventhub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventhub.model.Event
@Composable
fun EventDetailsScreen(
    event: Event,
    categoryName: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)

    ) {
        OutlinedButton(onClick = onBack) { Text("Back") }
        Text(categoryName, style = MaterialTheme.typography.labelLarge)
        Text(event.title, style = MaterialTheme.typography.headlineMedium)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Date: ${event.date}")
                Text("Location: ${event.location}")
                Text("Places: ${event.availablePlaces}")
                Text(
                    if (event.priceEuro == 0) "Free admission"
                    else "EUR ${event.priceEuro} per ticket"
                )
            }
        }
        Text("About this event", style = MaterialTheme.typography.titleLarge)
        Text(event.description)

    }

}
