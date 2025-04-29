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

    suspend fun setNote(note: Note)
    suspend fun deleteNote(note: Note)
}