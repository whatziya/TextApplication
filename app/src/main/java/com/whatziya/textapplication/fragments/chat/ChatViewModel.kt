package com.whatziya.textapplication.fragments.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.whatziya.textapplication.models.ChatMessage
import com.whatziya.textapplication.models.User
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.utilities.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TreeSet

class ChatViewModel(
    preferenceProvider: PreferenceProvider
) : ViewModel() {
    private val database = FirebaseFirestore.getInstance()
    val senderId = preferenceProvider.userId

    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    private var chatListener: ListenerRegistration? = null
    private val _user = MutableLiveData<User>()

    fun sendMessage(message: String) {
        val userId = _user.value?.id
        if (userId.isNullOrEmpty() || message.isBlank()) return
        val messageMap = hashMapOf(
            Constants.KEY_SENDER_ID to senderId,
            Constants.KEY_RECEIVER_ID to userId,
            Constants.KEY_MESSAGE to message,
            Constants.KEY_TIMESTAMP to Date()
        )
        database.collection(Constants.KEY_COLLECTION_CHAT).add(messageMap)
            .addOnFailureListener { Log.e("ChatViewModel", "Error sending message", it) }
    }

    private fun getReadableDateTime(date: Date): String {
        return SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date)
    }

    fun setUser(user: User) {
        _user.postValue(user)
    }

    fun listenMessages(receiverId: String) {
        chatListener?.remove()
        chatListener = database.collection(Constants.KEY_COLLECTION_CHAT)
            .whereIn(Constants.KEY_SENDER_ID, listOf(senderId, receiverId))
            .whereIn(Constants.KEY_RECEIVER_ID, listOf(senderId, receiverId))
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("ChatViewModel", "Error fetching messages: ${error.message}")
                    return@addSnapshotListener
                }
                if (value != null) {
                    val messagesSet = TreeSet<ChatMessage> { obj1, obj2 ->
                        obj1.dateObject.compareTo(obj2.dateObject)
                    }
                    for (document in value.documents) {
                        val sender = document.getString(Constants.KEY_SENDER_ID)
                        val receiver = document.getString(Constants.KEY_RECEIVER_ID)
                        val message = document.getString(Constants.KEY_MESSAGE)
                        val timestamp = document.getDate(Constants.KEY_TIMESTAMP)

                        if (sender != null && receiver != null && message != null && timestamp != null) {
                            messagesSet.add(
                                ChatMessage(
                                    sender,
                                    receiver,
                                    message,
                                    getReadableDateTime(timestamp),
                                    timestamp
                                )
                            )
                        }
                    }
                    _chatMessages.value = messagesSet.toList()
                }
            }
    }

    override fun onCleared() {
        chatListener?.remove()
        super.onCleared()
    }

}
