package com.example.gripnotes.model

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow

/**
 * FirestoreRepository is responsible for interacting with Firestore database.
 * It implements IRepository interface to provide methods for managing notes.
 *
 * @property authService An instance of IAuthService for authentication.
 * @author ericwb0
 */
class FirestoreRepository constructor(val authService: IAuthService) : IRepository {
    private val db = Firebase.firestore

    /**
     * FIRESTORE FIELDS
     */
    private val userCollection = "Users"
    private val notesCollection = "Notes"

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