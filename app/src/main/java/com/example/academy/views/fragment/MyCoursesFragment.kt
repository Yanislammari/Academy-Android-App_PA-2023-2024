package com.example.academy.views.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.CourseViewModel
import com.example.academy.viewmodels.EnrollmentViewModel
import com.example.academy.views.click_listeners.CourseOnClickListener
import com.example.academy.views.fragment.HomeFragment.Companion.COURSE_MODEL_EXTRA
import com.example.academy.views.activity.EnrolledCourseDetailsActivity
import com.example.academy.views.recycler_view.enrolled_course_recycler_view.EnrolledCourseListAdapter

class MyCoursesFragment : Fragment(), CourseOnClickListener {
    private val dataLayer = DataLayerSingleton
    private lateinit var enrolledCourseListRecyclerView: RecyclerView
    private lateinit var enrolledCourseAdapter: EnrolledCourseListAdapter
    private var enrollmentViewModel: EnrollmentViewModel = this.dataLayer.getEnrollmentViewModel()
    private var courseViewModel: CourseViewModel = this.dataLayer.getCourseViewModel()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = context?.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)!!

        this.enrolledCourseListRecyclerView = view.findViewById(R.id.enrollled_courses_list_rv)
        observeCourseListData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        enrollmentViewModel.coursesOfStudent.removeObservers(viewLifecycleOwner)
        courseViewModel.courseDetail.removeObservers(viewLifecycleOwner)
        courseViewModel.errorMessage.removeObservers(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        fetchCourseList()
    }

    private fun setUpActivityViews(data: List<CourseModel?>) {
        this.enrolledCourseAdapter = EnrolledCourseListAdapter(data, this)
        val linearLayoutManagerEnrolledCourse = LinearLayoutManager(requireContext())
        linearLayoutManagerEnrolledCourse.orientation = LinearLayoutManager.VERTICAL
        this.enrolledCourseListRecyclerView.layoutManager = linearLayoutManagerEnrolledCourse
        this.enrolledCourseListRecyclerView.adapter = enrolledCourseAdapter
    }

    private fun observeCourseListData() {
        this.enrollmentViewModel.coursesOfStudent.observe(viewLifecycleOwner) { courseList ->
            this.setUpActivityViews(courseList)
        }

        this.courseViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchCourseList() {
        this.dataLayer.getEnrollmentViewModel().getEnrollmentsOfStudent(sharedPreferences.getString("auth_token", null)!!, dataLayer.userConnected?.id)
    }

    override fun displayCourseDetail(course: CourseModel?) {
        handleCourseDetail(course)
    }

    private fun handleCourseDetail(course: CourseModel?) {
        if(course?.lessons?.isEmpty() == true) {
            Toast.makeText(requireContext(), "This course does not have any lessons yet", Toast.LENGTH_SHORT).show()
        }
        else {
            Intent(requireContext(), EnrolledCourseDetailsActivity::class.java).also {
                it.putExtra(COURSE_MODEL_EXTRA, course)
                startActivity(it)
            }
        }
    }

    override fun verifyCourseExists(courseId: String?) { }
}
