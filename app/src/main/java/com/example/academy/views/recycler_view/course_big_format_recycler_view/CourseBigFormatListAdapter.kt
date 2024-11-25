package com.example.academy.views.recycler_view.course_big_format_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.views.click_listeners.CourseOnClickListener

class CourseBigFormatListAdapter(private var courseBigFormatList: List<CourseModel>, private val courseClickHandler: CourseOnClickListener) : RecyclerView.Adapter<CourseBigFormatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseBigFormatViewHolder {
        val courseBigFormatView = LayoutInflater.from(parent.context).inflate(R.layout.course_cell_big_format, parent, false)
        return CourseBigFormatViewHolder(courseBigFormatView)
    }

    override fun getItemCount(): Int {
        return this.courseBigFormatList.size
    }

    fun updateData(newCourses: List<CourseModel>) {
        this.courseBigFormatList = newCourses
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CourseBigFormatViewHolder, position: Int) {
        val currentCourseBigFormatData = this.courseBigFormatList[position]
        holder.bind(currentCourseBigFormatData)

        holder.itemView.setOnClickListener {
            courseClickHandler.displayCourseDetail(currentCourseBigFormatData)
        }
    }
}
