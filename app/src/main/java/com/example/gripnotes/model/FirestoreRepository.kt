package com.example.gripnotes.model

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow

class FirestoreRepository constructor(val authService: IAuthService) : IRepository {
    val db = Firebase.firestore

    override val notes: Flow<List<Note>>
        get() = TODO("Not yet implemented")

    override suspend fun setNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(note: Note) {
        TODO("Not yet implemented")
    }

    suspend fun getNoteById(noteId: String): Note? {
        TODO("Not yet implemented")
    }
    fun searchNotes(searchTerm: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }
}