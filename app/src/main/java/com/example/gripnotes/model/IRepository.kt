package com.example.gripnotes.model

import kotlinx.coroutines.flow.Flow


interface IRepository {
    val notes: Flow<List<Note>>

    suspend fun setNote(note: Note)
    suspend fun deleteNote(note: Note)
}