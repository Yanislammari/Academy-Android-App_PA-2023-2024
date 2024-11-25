package com.example.academy.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.conversation.ConversationModel
import com.example.academy.models.user.UserModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.click_listeners.UserOnClickListener
import com.example.academy.views.fragment.MessagesFragment.Companion.CONVERSATION_MODEL_EXTRA
import com.example.academy.views.recycler_view.user_recycler_view.UserListAdapter

class SearchUserActivity : AppCompatActivity(), UserOnClickListener {
    private val dataLayer = DataLayerSingleton
    private lateinit var userListRecyclerView: RecyclerView
    private lateinit var userAdapter: UserListAdapter
    private lateinit var searchUserSearchView: SearchView
    private lateinit var sharedPreferences: SharedPreferences
    private var fullUserList: List<UserModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        this.userListRecyclerView = findViewById(R.id.search_user_recycler_view)
        this.searchUserSearchView = findViewById(R.id.search_user_search_view)

        observeUserListData()

        this.searchUserSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterUserList(newText ?: "")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchUserList()
    }

    private fun setUpActivityViews(data: List<UserModel>?) {
        if(data != null) {
            this.fullUserList = data
        }

        userAdapter = data?.let { UserListAdapter(it, this) }!!
        val linearLayoutManagerUser = LinearLayoutManager(this)
        linearLayoutManagerUser.orientation = LinearLayoutManager.VERTICAL
        this.userListRecyclerView.layoutManager = linearLayoutManagerUser
        this.userListRecyclerView.adapter = userAdapter
    }

    private fun observeUserListData() {
        this.dataLayer.getUserViewModel().users.observe(this) { userList ->
            this.setUpActivityViews(userList)
        }
    }

    private fun fetchUserList() {
        this.dataLayer.getUserViewModel().getUsers(sharedPreferences.getString("auth_token", null)!!)
    }

    private fun filterUserList(query: String) {
        val filteredList = fullUserList.filter { user ->
            user.email?.contains(query, ignoreCase = true) == true
        }
        userAdapter.updateData(filteredList)
    }

    override fun startConversation(user: UserModel?) {
        val currentUser = dataLayer.userConnected

        if(currentUser?.email == user?.email) {
            Toast.makeText(this, "You cannot start a conversation with yourself", Toast.LENGTH_SHORT).show()
        }
        else {
            Intent(this, ConversationMessagesActivity::class.java).also {
                val conversation = ConversationModel(
                    lastMessage = null,
                    userName = "${user?.firstName} ${user?.lastName}",
                    userProfilePicture = user?.profilePicture,
                    user2id = user?.id
                )
                it.putExtra(CONVERSATION_MODEL_EXTRA, conversation)
                startActivity(it)
            }
        }
    }
}
