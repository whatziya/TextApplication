package com.whatziya.textapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whatziya.textapplication.events.NavGraphEvent
import com.whatziya.textapplication.extensions.share
import com.whatziya.textapplication.preferences.PreferenceProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class SharedViewModel(
    private val preferenceProvider: PreferenceProvider
) : ViewModel() {

    private val navGraphEvent = Channel<NavGraphEvent>()

    fun setNavGraphEvent(event: NavGraphEvent) = viewModelScope.launch {
        delay(500L)
        navGraphEvent.send(event)
    }

    fun setNavGraphEvent() = viewModelScope.launch {
        if (preferenceProvider.isSignedIn) {
            navGraphEvent.send(NavGraphEvent.Main)
        } else {
            navGraphEvent.send(NavGraphEvent.Login)
        }
    }

    fun observeNavGraphEvent(): Flow<NavGraphEvent> {
        return navGraphEvent.consumeAsFlow().share(viewModelScope)
    }
}