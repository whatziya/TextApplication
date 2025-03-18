package com.whatziya.textapplication.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.whatziya.textapplication.databinding.ItemContainerReceivedMessageBinding
import com.whatziya.textapplication.databinding.ItemContainerSentMessageBinding
import com.whatziya.textapplication.models.ChatMessage

class ChatAdapter(
    private val receiverProfileImage: String,
    private val senderId: String
) : ListAdapter<ChatMessage, ViewHolder>(diffUtil) {
    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
        val diffUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.senderId == newItem.senderId || oldItem.receiverId == newItem.receiverId
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class SentMessageViewHolder(
        private val binding: ItemContainerSentMessageBinding
    ) : ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.textMessage.text = chatMessage.message
            binding.textDateTime.text = chatMessage.dateTime
        }
    }

    inner class ReceivedMessageViewHolder(
        private val binding: ItemContainerReceivedMessageBinding
    ) : ViewHolder(binding.root) {
        fun bind(chatMessage: ChatMessage) {
            binding.textMessage.text = chatMessage.message
            binding.textDateTime.text = chatMessage.dateTime
            binding.imageProfile.setImageBitmap(decodeUserImage(receiverProfileImage))
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == senderId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_SENT) {
            SentMessageViewHolder(ItemContainerSentMessageBinding.inflate(inflater, parent, false))
        } else {
            ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is SentMessageViewHolder -> holder.bind(message)
            is ReceivedMessageViewHolder -> holder.bind(message)
        }
    }

    private fun decodeUserImage(encodedImage: String?): Bitmap {
        if (encodedImage.isNullOrEmpty()) return getDefaultBitmap()

        return try {
            val bytes = Base64.decode(encodedImage, Base64.NO_WRAP)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: getDefaultBitmap()
        } catch (e: IllegalArgumentException) {
            getDefaultBitmap()
        }
    }

    private fun getDefaultBitmap(): Bitmap {
        return Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.GRAY)
        }
    }
}