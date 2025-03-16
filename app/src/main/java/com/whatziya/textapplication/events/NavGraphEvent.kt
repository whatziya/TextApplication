package com.whatziya.textapplication.events

sealed interface NavGraphEvent {
    data object Login : NavGraphEvent
    data object Main : NavGraphEvent
}