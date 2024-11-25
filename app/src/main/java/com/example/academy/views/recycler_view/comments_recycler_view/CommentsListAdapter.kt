package com.example.academy.views.recycler_view.comments_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.comments.CommentModel

class CommentsListAdapter(private var commentsList: MutableList<CommentModel>) : RecyclerView.Adapter<CommentsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val commentView = LayoutInflater.from(parent.context).inflate(R.layout.comment_cell, parent, false)
        return CommentsViewHolder(commentView)
    }

    override fun getItemCount(): Int {
        return this.commentsList.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val currentCommentData = this.commentsList[position]
        holder.bind(currentCommentData, this)
    }

    fun removeCommentById(commentId: String) {
        val index = commentsList.indexOfFirst { it.id == commentId }
        if(index != -1) {
            commentsList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
