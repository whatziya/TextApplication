package com.whatziya.textapplication.models

import java.util.Date

data class ChatMessage(
    val senderId: String,
    val receiverId: String,
    val message: String,
    val dateTime: String,
    val dateObject: Date
)
