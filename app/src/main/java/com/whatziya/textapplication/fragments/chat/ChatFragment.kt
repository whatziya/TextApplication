package com.whatziya.textapplication.fragments.chat

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.whatziya.textapplication.R
import com.whatziya.textapplication.adapters.ChatAdapter
import com.whatziya.textapplication.databinding.FragmentChatBinding
import com.whatziya.textapplication.extensions.gone
import com.whatziya.textapplication.extensions.toast
import com.whatziya.textapplication.extensions.visible
import com.whatziya.textapplication.fragments.BaseFragment
import com.whatziya.textapplication.fragments.factory.ViewModelFactory
import com.whatziya.textapplication.models.User
import com.whatziya.textapplication.preferences.PreferenceProvider
import com.whatziya.textapplication.preferences.SharedPreferencesHelper
import com.whatziya.textapplication.utilities.Constants

class ChatFragment : BaseFragment(R.layout.fragment_chat) {
    private val binding by viewBinding(FragmentChatBinding::bind)
    private val viewModel: ChatViewModel by viewModels {
        ViewModelFactory(
            providePreferences()
        )
    }
    private var user: User? = null
    private lateinit var adapter: ChatAdapter

    override fun setup() {
        user = getParcelableCompat(Constants.KEY_USER)

        if (user == null) {
            toast("Error loading user")
            findNavController().popBackStack()
            return
        }

        adapter = ChatAdapter(
            receiverProfileImage = user?.image.orEmpty(),
            senderId = viewModel.senderId
        )

        binding.chatRecyclerView.adapter = adapter
        user?.let {
            viewModel.setUser(it)
            binding.textName.text = it.name
            it.id.let { id -> viewModel.listenMessages(id) }
        }
    }

    override fun clicks() = with(binding) {
        imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        layoutSend.setOnClickListener {
            viewModel.sendMessage(inputMessage.text.toString())
            inputMessage.text = null
        }
    }

    override fun observe() = with(viewModel){
        chatMessages.observe(viewLifecycleOwner) { messages ->
            if (messages.isNotEmpty()) {
                binding.progressBar.gone()
                binding.chatRecyclerView.visible()
                adapter.submitList(messages)
                binding.chatRecyclerView.scrollToPosition(messages.size - 1)
            } else {
                binding.progressBar.visible()
                binding.chatRecyclerView.gone()
            }
        }
    }

    private fun providePreferences(): PreferenceProvider {
        return PreferenceProvider(
            SharedPreferencesHelper.provideSharedPreferences(requireContext())
        )
    }

    private inline fun <reified T : android.os.Parcelable> getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(key)
        }
    }
}