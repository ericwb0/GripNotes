package com.example.gripnotes.view.nav

import kotlinx.serialization.Serializable

/**
 * This file defines all the navigation destinations and sub-graphs for the app.
 * https://www.youtube.com/watch?v=sRv2L4PiqvM&ab_channel=HimanshuGaur
 *
 * @author ericwb0
 */
sealed class Dest {
    @Serializable
    data object Login : Dest()
    @Serializable
    data object SignUp : Dest()
    @Serializable
    data object Notes : Dest()
    @Serializable
    data class Editor(val noteId: String) : Dest()
    @Serializable
    data object AccountSettings : Dest()
}
sealed class SubGraph {
    @Serializable
    data object Auth : SubGraph()
    @Serializable
    data object Main : SubGraph()
}