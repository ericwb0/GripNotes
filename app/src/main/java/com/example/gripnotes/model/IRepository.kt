package com.example.gripnotes.model

import kotlinx.coroutines.flow.Flow

/**
 * IRepository is an interface that defines the contract for a repository
 * that manages notes.
 *
 * @property notes A Flow that emits a list of notes.
 * @author ericwb0
 */
interface IRepository {
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
}