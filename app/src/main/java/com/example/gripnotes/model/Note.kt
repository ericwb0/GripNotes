package com.example.gripnotes.model

import java.util.UUID

/**
 * Data class for representing notes in the app,
 * read from the Repository
 *
 * @author ericwb0
 */
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val created: Long = System.currentTimeMillis(),
    val updated: Long = System.currentTimeMillis(),
    val content: List<NoteContentItem> = emptyList()
)
