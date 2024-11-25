package com.example.academy.views.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.conversation.ConversationModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.activity.ConversationMessagesActivity
import com.example.academy.views.activity.SearchUserActivity
import com.example.academy.views.click_listeners.ConversationOnClickListener
import com.example.academy.views.recycler_view.conversations_recycler_view.ConversationListAdapter

class MessagesFragment : Fragment(), ConversationOnClickListener {
    private val dataLayer = DataLayerSingleton
    private lateinit var conversationListRecyclerView: RecyclerView
    private lateinit var conversationAdapter: ConversationListAdapter
    private lateinit var searchButton: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val CONVERSATION_MODEL_EXTRA = "CONVERSATION_MODEL_EXTRA"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)!!

        this.conversationListRecyclerView = view.findViewById(R.id.conversations_recycler_view)
        this.searchButton = view.findViewById(R.id.search_icon)
        observeConversationListData()

        searchButton.setOnClickListener {
            Intent(requireContext(), SearchUserActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchConversationList()
    }

    private fun setUpActivityViews(data: List<ConversationModel>) {
        val sortedMessages = data.sortedByDescending {
            it.lastMessage?.timeStamp
        }
        this.conversationAdapter = ConversationListAdapter(sortedMessages, this)
        val linearLayoutManagerConversation = LinearLayoutManager(requireContext())
        linearLayoutManagerConversation.orientation = LinearLayoutManager.VERTICAL
        this.conversationListRecyclerView.layoutManager = linearLayoutManagerConversation
        this.conversationListRecyclerView.adapter = conversationAdapter
    }

    private fun observeConversationListData() {
        this.dataLayer.getConversationViewModel().conversations.observe(viewLifecycleOwner) { conversationList ->
            if(conversationList != null) {
                this.setUpActivityViews(conversationList)
            }
        }
    }

    private fun fetchConversationList() {
        this.dataLayer.getConversationViewModel().getConversationsById(sharedPreferences.getString("auth_token", null)!!, this.dataLayer.userConnected?.id)
    }

    override fun displayConversationMessages(conversation: ConversationModel) {
        Intent(requireContext(), ConversationMessagesActivity::class.java).also {
            it.putExtra(CONVERSATION_MODEL_EXTRA, conversation)
            startActivity(it)
        }
    }
}
