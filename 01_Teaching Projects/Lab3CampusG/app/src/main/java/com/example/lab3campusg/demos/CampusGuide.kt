package com.example.lab3campusg


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.lab3campusg.R

private enum class CampusScreen(val title: String) {
    Library("Library"),
    ComputingLabs("Computing labs"),
    Cafeteria("Canteen"),
    SportsCentre("Sports centre"),
    StudentSupport("Student support")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampusGuideApp() {
    var selectedScreen by rememberSaveable {
        mutableStateOf<CampusScreen?>(null)
    }

    val currentScreen = selectedScreen

    // Handle system Back only when a content screen is open.
    BackHandler(enabled = currentScreen != null) {
        selectedScreen = null
    }

    Scaffold(
        topBar = {

            CenterAlignedTopAppBar(
               /* title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.tus_logo),
                            contentDescription = "TUS logo",
                            modifier = Modifier.height(72.dp).width(102.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(currentScreen?.title ?: "TUS Campus Guide")
                    }
                },*/



                expandedHeight = 160.dp,
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.tus_logo),
                            contentDescription = "TUS logo",
                            modifier = Modifier
                                .width(200.dp)
                                .height(80.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            text = currentScreen?.title ?: "TUS Campus Guide",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                navigationIcon = {
                    if (currentScreen != null) {
                        TextButton(
                            onClick = { selectedScreen = null }
                        ) {
                            Text("Back")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            when (currentScreen) {
                null -> CampusGuideMenu(
                    onSelect = { screen ->
                        selectedScreen = screen
                    }
                )

                CampusScreen.Library -> LibraryScreen()
                CampusScreen.ComputingLabs -> ComputingLabsScreen()
                CampusScreen.Cafeteria -> CafeteriaScreen()
                CampusScreen.SportsCentre -> SportsCentreScreen()
                CampusScreen.StudentSupport -> StudentSupportScreen()
            }
        }
    }
}

@Composable
private fun CampusGuideMenu(
    onSelect: (CampusScreen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        /*Text(
            text = "Campus Guide",
            style = MaterialTheme.typography.headlineMedium
        )*/

        Text(
            text = "Explore useful places and support around campus."
        )

        CampusScreen.entries.forEach { screen ->
            Button(
                onClick = { onSelect(screen) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(screen.title)
            }
        }
    }
}

@Composable
private fun LibraryScreen() {
    CampusContent(
        title = "Library",
        imageRes = R.drawable.campus_library,
        imageDescription = "Campus library",
        description = "Find a quiet place to study and access learning resources.",
        facilities = "Study spaces, computers and book loans",
        openingTime = "09:00",
        closingTime = "18:00"
    )
}

@Composable
private fun ComputingLabsScreen() {
    CampusContent(
        title = "Computing labs",
        imageRes = R.drawable.campus_computing,
        imageDescription = "Campus computing lab",
        description = "Use the computing labs for coursework and practical activities.",
        facilities = "Computers, specialist software and practical workspaces",
        openingTime = "09:00",
        closingTime = "17:00"
    )
}

@Composable
private fun CafeteriaScreen() {
    CampusContent(
        title = "Cafeteria",
        imageRes = R.drawable.campus_canteen,
        imageDescription = "Campus cafeteria",
        description = "Take a break, get something to eat and meet other students.",
        facilities = "Hot meals, snacks, drinks and seating",
        openingTime = "08:00",
        closingTime = "16:00"
    )
}

@Composable
private fun SportsCentreScreen() {
    CampusContent(
        title = "Sports centre",
        imageRes = R.drawable.campus_sport,
        imageDescription = "Campus sports centre",
        description = "Explore opportunities to exercise and take part in sport.",
        facilities = "Fitness spaces, sports facilities and changing rooms",
        openingTime = "07:00",
        closingTime = "20:00"
    )
}

@Composable
private fun StudentSupportScreen() {
    CampusContent(
        title = "Student support",
        imageRes = R.drawable.campus_support,
        imageDescription = "Student support centre",
        description = "Find advice and support for student life and your studies.",
        facilities = "Study guidance, wellbeing support and student advice",
        openingTime = "09:00",
        closingTime = "17:00"
    )
}

// Shared layout used by all five content screens.
@Composable
private fun CampusContent(
    title: String,
    imageRes: Int,
    imageDescription: String,
    description: String,
    facilities: String,
    openingTime: String,
    closingTime: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        Image(
            painter = painterResource(imageRes),
            contentDescription = imageDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant,
                contentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Facilities",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(facilities)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Example opening hours",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Opens")
                        Text(openingTime)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Closes")
                        Text(closingTime)
                    }
                }
            }
        }
    }
}