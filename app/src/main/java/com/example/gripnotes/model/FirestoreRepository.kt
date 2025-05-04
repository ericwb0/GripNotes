package com.example.gripnotes.model

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * FirestoreRepository is responsible for interacting with Firestore database.
 * It implements IRepository interface to provide methods for managing notes.
 *
 * @property authService An instance of IAuthService for authentication.
 * @author ericwb0
 */
class FirestoreRepository @Inject constructor(private val authService: AuthServiceI) : RepositoryI {
    private val db = Firebase.firestore

    /**
     * FIRESTORE FIELDS
     * Avoid using hardcoded strings.
     */
    private object Schema {
        const val USER_COLLECTION = "Users"
        const val NOTES_COLLECTION = "Notes"

        object UserFields {
            const val ID = "id"
            const val EMAIL = "email"
        }

        object NoteFields {
            const val ID = "id"
            const val TITLE = "title"
            const val CREATED = "created"
            const val UPDATED = "updated"
            const val CONTENT = "content"
        }

        object NoteContentItemFields {
            const val TYPE = "type"
            const val TEXT = "text"
            const val IS_CHECKED = "isChecked"
        }
    }

    override val notes: Flow<List<Note>>
        get() =
            db.collection(Schema.USER_COLLECTION)
                .document(authService.currentUserId!!)
                .collection(Schema.NOTES_COLLECTION)
                .orderBy(Schema.NoteFields.UPDATED, Direction.DESCENDING)
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
            db.collection(Schema.USER_COLLECTION).document(authService.currentUserId!!)
                .collection(Schema.NOTES_COLLECTION).document(note.id).set(doc).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error setting note: ${e.message}")
        }
    }

    override suspend fun deleteNote(id: String) {
        // Delete the note from Firestore
        try {
            db.collection(Schema.USER_COLLECTION).document(authService.currentUserId!!)
                .collection(Schema.NOTES_COLLECTION).document(id).delete().await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting note: ${e.message}")
        }
    }

    override suspend fun getNoteById(id: String): Note? {
        // Get the note from Firestore
        return try {
            val doc = db.collection(Schema.USER_COLLECTION).document(authService.currentUserId!!)
                .collection(Schema.NOTES_COLLECTION).document(id).get().await()
            if (doc.exists()) {
                docToNote(doc)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting note by ID: ${e.message}")
            null
        }
    }

    override suspend fun setUser(user: User) {
        try {
            db.collection(Schema.USER_COLLECTION).document(user.id).set(user).await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error setting user: ${e.message}")
        }
    }

    override suspend fun deleteUser(id: String) {
        try {
            db.collection(Schema.USER_COLLECTION).document(id).delete().await()
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error deleting user: ${e.message}")
        }
    }

    override suspend fun getUserById(id: String): User? {
        // Get the user from Firestore
        return try {
            val doc = db.collection(Schema.USER_COLLECTION).document(id).get().await()
            if (doc.exists()) {
                User(
                    id = doc.getString(Schema.UserFields.ID) ?: "",
                    email = doc.getString(Schema.UserFields.EMAIL) ?: ""
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "Error getting user by ID: ${e.message}")
            null
        }
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
    @Suppress("UNCHECKED_CAST")
    private fun docToNote(doc: DocumentSnapshot?): Note {
        if (doc == null) {
            Log.e("FirestoreRepository", "Attempted to convert null document to Note")
            return Note()
        }
        val content = doc.get(Schema.NoteFields.CONTENT) as? List<Map<String, Any>> ?: emptyList()
        return Note(
            id = doc.id,
            title = doc.getString(Schema.NoteFields.TITLE) ?: "",
            created = doc.getLong(Schema.NoteFields.CREATED) ?: System.currentTimeMillis(),
            updated = doc.getLong(Schema.NoteFields.UPDATED) ?: System.currentTimeMillis(),
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
            Schema.NoteFields.ID to note.id,
            Schema.NoteFields.TITLE to note.title,
            Schema.NoteFields.CREATED to note.created,
            Schema.NoteFields.UPDATED to note.updated,
            // Convert the content list to a list of maps for Firestore
            Schema.NoteFields.CONTENT to note.content.map {
                noteContentItemToMap(it)
            }
        )
    }

    /**
     * Converts a Map to a NoteContentItem object.
     */
    private fun mapToNoteContentItem(map: Map<String, Any?>): NoteContentItem {
        return when (map[Schema.NoteContentItemFields.TYPE] as? String) {
            NoteContentItem.TextItem::class.simpleName ->
                NoteContentItem.TextItem(map[Schema.NoteContentItemFields.TEXT] as String)

            NoteContentItem.CheckboxItem::class.simpleName ->
                NoteContentItem.CheckboxItem(
                    map[Schema.NoteContentItemFields.TEXT] as String,
                    map[Schema.NoteContentItemFields.IS_CHECKED] as Boolean
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
                Schema.NoteContentItemFields.TYPE to NoteContentItem.TextItem::class.simpleName!!,
                Schema.NoteContentItemFields.TEXT to item.text
            )

            is NoteContentItem.CheckboxItem -> mapOf(
                Schema.NoteContentItemFields.TYPE to NoteContentItem.CheckboxItem::class.simpleName!!,
                Schema.NoteContentItemFields.TEXT to item.text,
                Schema.NoteContentItemFields.IS_CHECKED to item.isChecked
            )
        }
    }
}