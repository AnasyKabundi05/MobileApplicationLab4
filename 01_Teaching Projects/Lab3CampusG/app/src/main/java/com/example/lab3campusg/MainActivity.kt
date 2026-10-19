package com.example.lab3campusg
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.example.lab3campusg.ui.theme.Lab3CampusGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab3CampusGTheme {
                CampusGuideApp()
            }
        }
    }
}