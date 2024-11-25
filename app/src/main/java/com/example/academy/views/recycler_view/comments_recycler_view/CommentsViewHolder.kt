package com.example.academy.views.recycler_view.comments_recycler_view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.comments.CommentModel
import com.example.academy.utils.singleton.DataLayerSingleton

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var commentText: TextView = itemView.findViewById(R.id.comment_text)
    private var commentDate: TextView = itemView.findViewById(R.id.comment_date)
    private var userName: TextView = itemView.findViewById(R.id.comment_user_name)
    private var profilePicture: ImageView = itemView.findViewById(R.id.comment_profile_image)
    private var deleteButton: ImageButton = itemView.findViewById(R.id.comment_delete_button)
    private lateinit var sharedPreferences: SharedPreferences

    fun bind(comment: CommentModel, adapter: CommentsListAdapter) {
        sharedPreferences = itemView.context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        this.commentText.text = comment.content
        this.commentDate.text = comment.timestamp?.split("T")?.get(0) ?: "Unknown"

        deleteButton.setOnClickListener {
            DataLayerSingleton.getCommentViewModel().deleteComment(sharedPreferences.getString("auth_token", null)!!, comment.id, adapter)
        }

        deleteButton.visibility = View.GONE

        DataLayerSingleton.getUserViewModel().getUserById(sharedPreferences.getString("auth_token", null)!!, comment.userId) { user ->
            if(user != null) {
                this.userName.text = "${user.firstName} ${user.lastName}"

                Glide.with(profilePicture.context)
                    .load(user.profilePicture)
                    .placeholder(R.drawable.placeholder)
                    .into(profilePicture)

                if(comment.userId == DataLayerSingleton.userConnected?.id) {
                    deleteButton.visibility = View.VISIBLE
                }
            }
        }
    }
}
