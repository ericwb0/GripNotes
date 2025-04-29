package com.example.gripnotes.model

sealed class NoteContentItem {
    data class TextItem(val text: String) : NoteContentItem()
    data class CheckboxItem(val text: String, val isChecked: Boolean) : NoteContentItem()
}