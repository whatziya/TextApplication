package com.whatziya.textapplication.extensions

import com.whatziya.textapplication.utilities.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

fun <T> Flow<T>.share(scope: CoroutineScope) = shareIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(Constants.Number.FIVE_THOUSAND),
    replay = Constants.Number.ONE
)