package com.example.academy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academy.models.comments.CommentModel
import com.example.academy.models.comments.CommentDTO
import com.example.academy.models.comments.CommentRequests
import com.example.academy.network.repository.CommentRepository
import com.example.academy.views.recycler_view.comments_recycler_view.CommentsListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel(private val commentRepository: CommentRepository) : ViewModel() {
    var comments = MutableLiveData<List<CommentModel>?>()
    var singleComment = MutableLiveData<CommentModel?>()

    fun getComments(token: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    commentRepository.getAllComments(token).execute()
                }

                if(response.isSuccessful) {
                    val commentsDto = response.body()?.comments
                    val commentModels = commentsDto?.map {
                        CommentModel(
                            id = it.id,
                            userId = it.userId,
                            courseId = it.courseId,
                            content = it.content,
                            timestamp = it.timestamp
                        )
                    }
                    comments.postValue(commentModels)
                }
                else {
                    comments.postValue(null)
                }
            }
            catch(e: Exception) {
                e.printStackTrace()
                comments.postValue(null)
            }
        }
    }

    fun getCommentById(token: String, id: String) {
        commentRepository.getCommentById(token, id).enqueue(object : Callback<CommentDTO> {
            override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                if(response.isSuccessful) {
                    val commentDTO = response.body()
                    val commentModel = commentDTO?.let {
                        CommentModel(
                            id = it.id,
                            userId = it.userId,
                            courseId = it.courseId,
                            content = it.content,
                            timestamp = it.timestamp
                        )
                    }
                    singleComment.postValue(commentModel)
                }
                else {
                    singleComment.postValue(null)
                }
            }

            override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                t.printStackTrace()
                singleComment.postValue(null)
            }
        })
    }

    fun postComment(token: String, comment: CommentModel) {
        commentRepository.postComment(token, CommentRequests.CommentRequest(comment.userId, comment.courseId, comment.content)).enqueue(object : Callback<CommentDTO> {
            override fun onResponse(call: Call<CommentDTO>, response: Response<CommentDTO>) {
                if(response.isSuccessful) {
                    val postedCommentDTO = response.body()
                    val postedCommentModel = postedCommentDTO?.let {
                        CommentModel(
                            id = it.id,
                            userId = it.userId,
                            courseId = it.courseId,
                            content = it.content,
                            timestamp = it.timestamp
                        )
                    }
                    singleComment.postValue(postedCommentModel)
                }
                else {
                    singleComment.postValue(null)
                }
            }

            override fun onFailure(call: Call<CommentDTO>, t: Throwable) {
                t.printStackTrace()
                singleComment.postValue(null)
            }
        })
    }

    fun deleteComment(token: String, id: String?, adapter: CommentsListAdapter) {
        if(id == null) {
            return
        }

        commentRepository.deleteComment(token, id).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if(response.isSuccessful) {
                    adapter.removeCommentById(id)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}
