package com.example.academy.views.recycler_view.course_lite_format_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.views.click_listeners.CourseOnClickListener

class CourseLiteFormatListAdapter(private var courseLiteFormatList: List<CourseModel>, private val courseClickHandler: CourseOnClickListener) : RecyclerView.Adapter<CourseLiteFormatViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseLiteFormatViewHolder {
        val courseLiteFormatView = LayoutInflater.from(parent.context).inflate(R.layout.course_cell_lite_format, parent, false)
        return CourseLiteFormatViewHolder(courseLiteFormatView)
    }

    override fun getItemCount(): Int {
        return this.courseLiteFormatList.size
    }

    fun updateData(newCourses: List<CourseModel>) {
        this.courseLiteFormatList = newCourses
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CourseLiteFormatViewHolder, position: Int) {
        val currentCourseLiteFormatData = this.courseLiteFormatList[position]
        holder.bind(currentCourseLiteFormatData)

        holder.itemView.setOnClickListener {
            courseClickHandler.displayCourseDetail(currentCourseLiteFormatData)
        }
    }
}
