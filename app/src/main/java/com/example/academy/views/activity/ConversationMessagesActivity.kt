package com.example.academy.views.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.conversation.ConversationModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.fragment.MessagesFragment.Companion.CONVERSATION_MODEL_EXTRA
import com.example.academy.views.recycler_view.conversation_messages_recycler_view.ConversationMessageListAdapter

class ConversationMessagesActivity : AppCompatActivity() {
    private val dataLayer = DataLayerSingleton
    private lateinit var conversationMessagesListRecyclerView: RecyclerView
    private lateinit var conversationMessagesAdapter: ConversationMessageListAdapter
    private lateinit var conversationMessagesProfilePicture: ImageView
    private lateinit var conversationMessageUserName: TextView
    private lateinit var conversationMessagesInput: EditText
    private lateinit var conversationMessagesSendButton: ImageButton
    private lateinit var conversationUserId: String
    private lateinit var conversationUserName: String
    private lateinit var conversationUserProfilePicture: String
    private lateinit var sharedPreferences: SharedPreferences
    private var pollingHandler: android.os.Handler? = null

    private val pollingRunnable = object : Runnable {
        override fun run() {
            fetchMessagesList()
            pollingHandler?.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_messages)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        getIntentExtraData()
        setupViews()
        observeConversationListData()
    }

    override fun onResume() {
        super.onResume()
        startPollingForMessages()
    }

    override fun onPause() {
        super.onPause()
        stopPollingForMessages()
    }

    private fun getIntentExtraData() {
        intent.getParcelableExtra<ConversationModel>(CONVERSATION_MODEL_EXTRA)?.let { conversationData ->
            conversationUserId = conversationData.user2id ?: ""
            conversationUserName = conversationData.userName ?: ""
            conversationUserProfilePicture = conversationData.userProfilePicture ?: ""
        }
    }

    private fun setupViews() {
        conversationMessagesListRecyclerView = findViewById(R.id.conversation_messages_recycler_view)
        conversationMessagesProfilePicture = findViewById(R.id.conversation_messages_profile_picture)
        conversationMessageUserName = findViewById(R.id.conversation_messages_profile_name)
        conversationMessagesInput = findViewById(R.id.conversation_messages_input)
        conversationMessagesSendButton = findViewById(R.id.conversation_messages_send_button)

        Glide.with(this)
            .load(conversationUserProfilePicture)
            .placeholder(R.drawable.placeholder)
            .into(conversationMessagesProfilePicture)
        conversationMessageUserName.text = conversationUserName

        conversationMessagesAdapter = ConversationMessageListAdapter(emptyList())
        conversationMessagesListRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationMessagesListRecyclerView.adapter = conversationMessagesAdapter

        conversationMessagesSendButton.setOnClickListener {
            sendMessage()
            this.conversationMessagesInput.setText("");
        }
    }

    private fun observeConversationListData() {
        dataLayer.getMessageViewModel().messages.observe(this) { messageList ->
            if(messageList != null) {
                val sortedMessages = messageList.sortedBy {
                    it.timeStamp
                }
                conversationMessagesAdapter.updateData(sortedMessages)
            }
        }
    }

    private fun fetchMessagesList() {
        dataLayer.getMessageViewModel().getMessages(sharedPreferences.getString("auth_token", null)!!, dataLayer.userConnected?.id, conversationUserId)
    }

    private fun sendMessage() {
        val message = conversationMessagesInput.text.toString()
        dataLayer.getMessageViewModel().sendMessage(sharedPreferences.getString("auth_token", null)!!, dataLayer.userConnected?.id, conversationUserId, message)
    }

    private fun startPollingForMessages() {
        pollingHandler = android.os.Handler(android.os.Looper.getMainLooper())
        pollingHandler?.post(pollingRunnable)
    }

    private fun stopPollingForMessages() {
        pollingHandler?.removeCallbacks(pollingRunnable)
        pollingHandler = null
    }
}
