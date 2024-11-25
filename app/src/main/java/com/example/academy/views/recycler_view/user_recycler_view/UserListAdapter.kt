package com.example.academy.views.recycler_view.user_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.user.UserModel
import com.example.academy.views.click_listeners.UserOnClickListener

class UserListAdapter(private var userList: List<UserModel>, private val userClickHandler: UserOnClickListener) : RecyclerView.Adapter<UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userView = LayoutInflater.from(parent.context).inflate(R.layout.user_cell, parent, false)
        return UserViewHolder(userView)
    }

    override fun getItemCount(): Int {
        return this.userList.size
    }

    fun updateData(newUsers: List<UserModel>) {
        this.userList = newUsers
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUserData = this.userList[position]
        holder.bind(currentUserData)

        holder.itemView.setOnClickListener {
            userClickHandler.startConversation(currentUserData)
        }
    }
}
