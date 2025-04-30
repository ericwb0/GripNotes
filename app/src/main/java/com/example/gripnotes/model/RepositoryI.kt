package com.example.gripnotes.model

import kotlinx.coroutines.flow.Flow

/**
 * IRepository is an interface that defines the contract for a repository
 * that manages notes.
 *
 * @property notes A Flow that emits a list of notes.
 * @author ericwb0
 */
interface RepositoryI {
    val notes: Flow<List<Note>>

    /**
     * Adds or updates a note in the repository.
     */
    suspend fun setNote(note: Note)

    /**
     * Deletes a note from the repository.
     *
     * @param id The ID of the note to be deleted.
     */
    suspend fun deleteNote(id: String)

    /**
     * Gets a note by ID.
     *
     * @param id The ID of the note to be retrieved.
     * @return The note with the specified ID, or null if not found.
     */
    suspend fun getNoteById(id: String): Note?

    /**
     * Adds or updates a user in the repository.
     *
     * @param user The user to be added or updated.
     */
    suspend fun setUser(user: User)

    /**
     * Deletes a user from the repository.
     *
     * @param id The ID of the user to be deleted.
     */
    suspend fun deleteUser(id: String)

    /**
     * Gets user by ID.
     */
    suspend fun getUserById(id: String): User?
}