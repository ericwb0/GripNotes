package com.example.gripnotes.model

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.tasks.await

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
    private val updatedField = "updated"

    @OptIn(ExperimentalCoroutinesApi::class)
    override val notes: Flow<List<Note>>
        get() = db.collection(userCollection)
            .document(authService.currentUserId)
            .collection(notesCollection)
            .orderBy(updatedField, Direction.DESCENDING)
            .snapshots().map {
                // Convert each document to a Note object
                it.documents.map { doc ->
                    docToNote(doc)
                }
            }

    override suspend fun setNote(note: Note) {
        val doc = noteToDoc(note)
        // Set the note in Firestore
        try {
            db.collection(userCollection).document(authService.currentUserId)
                .collection(notesCollection).document(note.id).set(doc).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error setting note: ${e.message}")
        }
    }

    override suspend fun deleteNote(id: String) {
        // Delete the note from Firestore
        try {
            db.collection(userCollection).document(authService.currentUserId)
                .collection(notesCollection).document(id).delete().await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting note: ${e.message}")
        }
    }

    suspend fun getNoteById(noteId: String): Note? {
        // Get the note from Firestore
        return try {
            val doc = db.collection(userCollection).document(authService.currentUserId)
                .collection(notesCollection).document(noteId).get().await()
            docToNote(doc)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting note by ID: ${e.message}")
            null
        }
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    fun searchNotes(searchTerm: String): Flow<List<Note>> {
        TODO("Not yet implemented")
    }

    /*
     * HELPERS
     */
    /**
     * Converts a Firestore DocumentSnapshot to a Note object.
     *
     * @param doc The DocumentSnapshot to convert.
     * @return A Note object representing the document.
     */
    private fun docToNote(doc: DocumentSnapshot?): Note {
        if (doc == null) {
            Log.e("FirestoreRepository", "Attempted to convert null document to Note")
            return Note()
        }
        val content = doc.get("content") as? List<Map<String, Any>> ?: emptyList()
        return Note(
            id = doc.id,
            title = doc.getString("title") ?: "",
            created = doc.getLong("created") ?: System.currentTimeMillis(),
            updated = doc.getLong("updated") ?: System.currentTimeMillis(),
            content = content.map {
                mapToNoteContentItem(it)
            }
        )
    }

    /**
     * Converts a Note object to a Firestore DocumentSnapshot.
     */
    private fun noteToDoc(note: Note): Map<String, Any> {
        return mapOf(
            "title" to note.title,
            "created" to note.created,
            "updated" to note.updated,
            "content" to note.content.map {
                noteContentItemToMap(it)
            }
        )
    }

    /**
     * Converts a Map to a NoteContentItem object.
     */
    private fun mapToNoteContentItem(map: Map<String, Any?>): NoteContentItem {
        return when (map["type"]) {
            NoteContentItem.TextItem::class.simpleName ->
                NoteContentItem.TextItem(map["text"] as String)

            NoteContentItem.CheckboxItem::class.simpleName ->
                NoteContentItem.CheckboxItem(
                    map["text"] as String,
                    map["isChecked"] as Boolean
                )

            else -> throw IllegalArgumentException("Unknown content type")
        }
    }

    /**
     * Converts a NoteContentItem object to a Map.
     */
    private fun noteContentItemToMap(item: NoteContentItem): Map<String, Any> {
        return when (item) {
            is NoteContentItem.TextItem -> mapOf(
                // We know the name won't be null
                "type" to NoteContentItem.TextItem::class.simpleName!!,
                "text" to item.text
            )

            is NoteContentItem.CheckboxItem -> mapOf(
                "type" to NoteContentItem.CheckboxItem::class.simpleName!!,
                "text" to item.text,
                "isChecked" to item.isChecked
            )
        }
    }
}