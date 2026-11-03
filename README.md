EventHub – Lab 5: Room Database Integration
📱 Project Overview

EventHub is an Android application developed using Kotlin and Jetpack Compose. This project builds upon Lab 4 by introducing Room Database to store and retrieve event and category information locally.

In Lab 4, event information was displayed using sample Kotlin data lists. In Lab 5, this data is stored in a local SQLite database using Room, allowing events to remain available even after the application is closed and reopened.

The existing navigation journey from Home to Events and Event Details is maintained.

🎯 Learning Outcomes
Convert Category and Event data classes into Room entities.
Implement primary keys and foreign key relationships.
Create a Data Access Object (DAO) for database operations.
Use Kotlin Flow to observe database changes.
Create a shared Room database instance.
Seed initial categories and events into the database.
Implement a repository to manage database operations.
Use a ViewModel to manage application UI state.
Connect Room database data to Jetpack Compose screens.
Demonstrate local data persistence across application restarts.
✨ Features
Home Screen: Displays featured events and categories.
Events Screen: Displays the available events stored in Room.
Event Details: Displays detailed information about a selected event.
Navigation: Navigate between Home, Events and Event Details.
Room Database: Stores categories and events locally.
Automatic Seeding: Inserts initial sample data when the database is empty.
Add Test Event: Allows a new event to be inserted into the database.
Live Database Updates: Uses Kotlin Flow to automatically update the UI when database records change.
Persistent Storage: Saved events remain available after closing and reopening the application.
Event Lookup: Uses event IDs to retrieve the correct event details.
🛠️ Technologies Used
Technology	Purpose
Kotlin	Main programming language
Jetpack Compose	Modern Android UI development
Material 3	UI components and styling
Room Database	Local data persistence
SQLite	Underlying database storage
Kotlin Coroutines	Asynchronous database operations
Kotlin Flow	Observable database queries
ViewModel	UI state management
Repository Pattern	Separates data operations from UI
Navigation Compose	Screen navigation
KSP	Room annotation processing
Gradle Version Catalog	Dependency management
🗂️ Project Structure

The project follows a structured package organisation:

text
EventHubTeachingProject/
│
├── app/
│   ├── schemas/
│   │   └── Room database schema exports
│   │
│   └── src/main/java/
│       │
│       ├── com/example/eventhub/
│       │   │
│       │   ├── model/
│       │   │   ├── Category.kt
│       │   │   ├── Event.kt
│       │   │   └── SampleData.kt
│       │   │
│       │   ├── data/
│       │   │   ├── EventRepository.kt
│       │   │   │
│       │   │   └── local/
│       │   │       ├── EventHubDao.kt
│       │   │       └── EventHubDatabase.kt
│       │   │
│       │   └── ui/
│       │       ├── EventHubViewModel.kt
│       │       │
│       │       └── screens/
│       │           ├── HomeScreen.kt
│       │           ├── EventsScreen.kt
│       │           ├── EventDetailsScreen.kt
│       │           └── EventCard.kt
│       │
│       └── com/example/eventhubteachingproject/
│           ├── MainActivity.kt
│           └── EventHubApp.kt
│
├── gradle/
│   └── libs.versions.toml
│
└── README.md

🗄️ Database Design

The application uses Room to manage two database tables.

Categories Table

Stores the available event categories.

Column	Type	Description
id	Int	Primary key
name	String	Category name
Events Table

Stores individual events and their information.

Column	Type	Description
id	Int	Auto-generated primary key
title	String	Event title
categoryId	Int	Foreign key referencing Categories
date	String	Event date
location	String	Event location
description	String	Event description
priceEuro	Int	Event price in euros
availablePlaces	Int	Available places
Relationship

The database uses a one-to-many relationship:

One category can contain multiple events.
Each event belongs to one category.
The categoryId foreign key ensures that events reference an existing category.
ForeignKey.RESTRICT prevents deleting a category while events still reference it.
🔄 Application Data Flow

The application follows this structure:

text
MainActivity
     |
     v
EventHubApp
     |
     v
EventHubViewModel
     |
     v
EventRepository
     |
     v
EventHubDatabase
     |
     v
EventHubDao
     |
     v
Room Database
     |
     v
Categories & Events
     |
     v
Jetpack Compose UI


When the application starts:

MainActivity launches EventHubApp.
EventHubApp obtains the EventHubViewModel.
The repository checks whether the database needs initial sample data.
Categories and events are inserted inside a database transaction.
Room provides observable data through Kotlin Flow.
The ViewModel exposes the data using StateFlow.
Compose observes the UI state and displays the database records.
Selecting an event navigates to its details using its unique ID.
🧭 Navigation

The application maintains the navigation implemented in Lab 4.

Route	Screen	Description
home	HomeScreen	Displays featured events
events	EventsScreen	Displays all available events
details/{eventId}	EventDetailsScreen	Displays the selected event

The application passes an event ID through the navigation route rather than passing the entire Event object.

The destination then searches the observed database event list to find the matching event.

💾 Data Persistence

One of the main objectives of Lab 5 is demonstrating that event data is stored permanently in the local database rather than being held only in memory.

The application includes an Add test event button that inserts a new event:

Title: Student Study Meetup
Location: Campus library
Date: To be arranged
Price: €0
Available places: 12

Room automatically generates a unique ID for the new event.

The database is seeded only when both the categories and events tables are empty. This prevents the initial sample data from being duplicated every time the application launches.

🧪 Testing and Verification

The following tests are used to verify the application:

Test	Expected Result
Launch application	Five initial sample events are displayed
Navigate to Events	All stored events are displayed
Open event details	Correct event information is displayed
Add test event	Student Study Meetup is inserted
Check generated ID	A unique event ID is returned
Navigate back	Previous screen is displayed
Rotate device	Event data and navigation remain correct
Force-stop and reopen	Newly inserted event remains available
Relaunch application	Initial sample events are not duplicated
Database Inspector	Categories and events tables contain saved records
Open invalid event ID	Event not found message is displayed
Database Inspector

Android Studio's Database Inspector can be used to inspect the local eventhub.db database.

Example SQL query:

sql
SELECT events.id, events.title, categories.name
FROM events
JOIN categories
ON events.categoryId = categories.id
ORDER BY events.id;


This query joins the events and categories tables to display each event alongside its category name.

⚙️ Installation and Setup
Clone this repository:
bash
git clone YOUR_REPOSITORY_URL

Open the project in Android Studio.
Check that your development environment meets the project requirements:
JDK 17 or compatible project configuration.
Android SDK with API 23 or higher.
Compatible Kotlin, Android Gradle Plugin and Gradle versions.
Allow Gradle to synchronise the project dependencies.
Ensure KSP is correctly configured for Room annotation processing.
Build and run the application using an Android emulator or physical Android device.

Note: The teaching configuration uses Room 2.8.4 and Lifecycle 2.9.4. KSP 2.3.10 is only suitable for the compatible toolchain described in the lab instructions. Check the project's existing Gradle configuration before changing versions.

📚 Key Concepts Demonstrated
Room Database

Provides structured local storage for categories and events.

DAO (Data Access Object)

Defines database queries and insertion operations.

Repository Pattern

Separates database access from the ViewModel and UI.

ViewModel

Maintains UI state across configuration changes such as device rotation.

StateFlow and Flow

Allow the application to observe changes to database records and update the interface automatically.

Database Transactions

Ensure initial categories and events are inserted together as one transaction.

Foreign Keys

Maintain relationships between categories and events and help protect database integrity.

🚀 Future Improvements

Possible extensions for future development include:

Add a form to create events with custom titles.
Allow users to select categories when creating events.
Implement event editing and deletion.
Add a booking system.
Introduce user profiles and account management.
Add search and filtering for events.
Implement database migrations as the application develops.
📝 Lab Summary

Lab 5 extends the EventHub application from Lab 4 by replacing in-memory sample data with a persistent Room database.

The project demonstrates how to structure an Android application using entities, DAOs, repositories, ViewModels, Kotlin Flow and Jetpack Compose.

The original navigation journey remains unchanged, while event and category information is now retrieved from a local database. Newly inserted events remain stored even after the application is closed and reopened.

Project: EventHub – Lab 5
Platform: Android
Language: Kotlin
UI Framework: Jetpack Compose
Database: Room / SQLite
Purpose: Learning Android local database integration and persistence.
