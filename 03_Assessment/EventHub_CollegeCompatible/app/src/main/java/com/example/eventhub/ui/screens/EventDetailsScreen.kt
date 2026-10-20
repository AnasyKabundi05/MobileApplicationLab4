package com.example.eventhub.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventhub.model.Event

@Composable
fun EventDetailsScreen(event: Event, onBack: () -> Unit, onViewBookings: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        TextButton(onClick = onBack) { Text("← Back to events") }
        Text(event.category, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(event.title, style = MaterialTheme.typography.headlineMedium)
        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("When: ${event.date}")
                Text("Where: ${event.location}")
                Text("Places available: ${event.availablePlaces}")
                Text(if (event.priceEuro == 0) "Admission: Free" else "Admission: €${event.priceEuro}")
            }
        }
        Text(event.description, style = MaterialTheme.typography.bodyLarge)
        Text("Booking features will be added in future lessons.", style = MaterialTheme.typography.bodySmall)
        OutlinedButton(onClick = onViewBookings) { Text("View sample bookings") }
    }
}
