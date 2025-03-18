package com.whatziya.textapplication.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whatziya.textapplication.databinding.ItemContainerUserBinding
import com.whatziya.textapplication.models.User

class UsersAdapter(
    private val onClick: (User) -> Unit
) : ListAdapter<User, UsersAdapter.UserViewHolder>(diffUtil) {
    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.email == newItem.email
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class UserViewHolder(
        private val binding: ItemContainerUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) = with(binding) {
            textName.text = user.name
            textEmail.text = user.email
            val cachedBitmap = decodedImages[user.image]
            if (cachedBitmap != null) {
                imageProfile.setImageBitmap(cachedBitmap)
            } else {
                val bitmap = decodeUserImage(user.image)
                decodedImages[user.image] = bitmap
                imageProfile.setImageBitmap(bitmap)
            }
            root.setOnClickListener { onClick(user) }
        }
    }

    private val decodedImages = mutableMapOf<String, Bitmap>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}