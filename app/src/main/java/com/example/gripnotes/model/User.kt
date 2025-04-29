package com.example.gripnotes.model

import java.util.UUID

data class User (
    val id: String = UUID.randomUUID().toString(),
    val email: String = ""
)