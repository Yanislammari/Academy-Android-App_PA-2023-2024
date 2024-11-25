package com.example.academy.views.click_listeners

import com.example.academy.models.user.UserModel

interface UserOnClickListener {
    fun startConversation(user: UserModel?)
}
