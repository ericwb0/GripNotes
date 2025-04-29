package com.example.gripnotes

import android.app.Application
import com.google.firebase.FirebaseApp

class GripNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize services here
        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}