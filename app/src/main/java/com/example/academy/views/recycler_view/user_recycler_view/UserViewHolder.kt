package com.example.academy.views.recycler_view.user_recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.user.UserModel

class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private var userName: TextView = itemView.findViewById(R.id.user_name)
    private var userEmail: TextView = itemView.findViewById(R.id.user_email)
    private var userRole: TextView = itemView.findViewById(R.id.user_role)
    private var userProfilePicture: ImageView = itemView.findViewById(R.id.user_profile_image)

    fun bind(user: UserModel) {
        this.userName.text = "${user.firstName} ${user.lastName}"
        this.userEmail.text = user.email
        this.userRole.text = user.role

        Glide.with(itemView.context)
            .load(user.profilePicture)
            .placeholder(R.drawable.placeholder)
            .into(this.userProfilePicture)
    }
}
