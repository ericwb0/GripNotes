package com.example.gripnotes

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * Application class to initialize services.
 *
 * @author ericwb0
 */
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