package com.example.gripnotes.model

import java.util.UUID

/**
 * Simple user data class for representing users in the app.
 *
 * @author ericwb0
 */
data class User (
    /*
     * We will almost always use the id from Firebase auth,
     * but for testing purposes, we will use a random UUID.
     */
    val id: String = UUID.randomUUID().toString(),
    val email: String = ""
)