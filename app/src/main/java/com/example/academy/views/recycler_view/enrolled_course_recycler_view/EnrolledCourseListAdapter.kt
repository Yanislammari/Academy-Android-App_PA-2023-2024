package com.example.academy.views.recycler_view.enrolled_course_recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.EnrollmentViewModel
import com.example.academy.views.click_listeners.CourseOnClickListener

class EnrolledCourseListAdapter(private var enrolledCourseList: List<CourseModel?>, private val courseClickHandler: CourseOnClickListener, private var enrollmentViewModel: EnrollmentViewModel = DataLayerSingleton.getEnrollmentViewModel()) : RecyclerView.Adapter<EnrolledCourseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnrolledCourseViewHolder {
        val enrolledCourseFormatView = LayoutInflater.from(parent.context).inflate(R.layout.enrolled_course_cell, parent, false)
        return EnrolledCourseViewHolder(enrolledCourseFormatView)
    }

    override fun getItemCount(): Int {
        val count = this.enrolledCourseList.size
        return count
    }

    override fun onBindViewHolder(holder: EnrolledCourseViewHolder, position: Int) {
        val currentEnrolledCourseData = this.enrolledCourseList[position]
        val enrollmentOfTheCourse = enrollmentViewModel.getEnrollmentOfACourse(currentEnrolledCourseData?.id)
        holder.bind(currentEnrolledCourseData, enrollmentOfTheCourse)

        holder.itemView.setOnClickListener {
            courseClickHandler.displayCourseDetail(currentEnrolledCourseData)
        }
    }
}
