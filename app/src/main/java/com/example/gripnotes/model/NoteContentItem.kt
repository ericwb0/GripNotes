package com.example.gripnotes.model

/**
 * NoteContentItem is a sealed class representing different types of content items
 * in a note.
 * It can be either a text item or a checkbox item.
 *
 * @author ericwb0
 */
sealed class NoteContentItem {
    data class TextItem(val text: String) : NoteContentItem()
    data class CheckboxItem(val text: String, val isChecked: Boolean) : NoteContentItem()
}