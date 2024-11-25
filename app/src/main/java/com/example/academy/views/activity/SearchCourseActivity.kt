package com.example.academy.views.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.CourseViewModel
import com.example.academy.views.click_listeners.CourseOnClickListener
import com.example.academy.views.recycler_view.course_lite_format_recycler_view.CourseLiteFormatListAdapter

class SearchCourseActivity : AppCompatActivity(), CourseOnClickListener {
    private val dataLayer = DataLayerSingleton
    private lateinit var courseLiteFormatListRecyclerView: RecyclerView
    private lateinit var courseLiteFormatAdapter: CourseLiteFormatListAdapter
    private lateinit var searchCourseSearchView: SearchView
    private var courseViewModel: CourseViewModel = this.dataLayer.getCourseViewModel()
    private var fullCourseList: List<CourseModel> = listOf()
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val COURSE_MODEL_EXTRA = "COURSE_MODEL_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_course)

        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        this.courseLiteFormatListRecyclerView = findViewById(R.id.search_course_lite_format_list_rv)
        this.searchCourseSearchView = findViewById(R.id.search_course_search_view)

        observeCourseListData()

        this.searchCourseSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCourseList(newText ?: "")
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        fetchCourseList()
    }

    private fun setUpActivityViews(data: List<CourseModel>) {
        this.fullCourseList = data
        courseLiteFormatAdapter = CourseLiteFormatListAdapter(data, this)
        val linearLayoutManagerCourseLiteFormat = LinearLayoutManager(this)
        linearLayoutManagerCourseLiteFormat.orientation = LinearLayoutManager.VERTICAL
        this.courseLiteFormatListRecyclerView.layoutManager = linearLayoutManagerCourseLiteFormat
        this.courseLiteFormatListRecyclerView.adapter = courseLiteFormatAdapter
    }

    private fun observeCourseListData() {
        this.courseViewModel.courses.observe(this) { courseList ->
            this.setUpActivityViews(courseList)
        }

        this.courseViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchCourseList() {
        this.courseViewModel.fetchCoursesFromRepository(sharedPreferences.getString("auth_token", null)!!)
    }

    private fun filterCourseList(query: String) {
        val filteredList = fullCourseList.filter { course ->
            course.title?.contains(query, ignoreCase = true) == true
        }
        courseLiteFormatAdapter.updateData(filteredList)
    }

    override fun displayCourseDetail(course: CourseModel?) {
        Intent(this, CourseDetailsActivity::class.java).also {
            it.putExtra(COURSE_MODEL_EXTRA, course)
            startActivity(it)
        }
    }

    override fun verifyCourseExists(courseId: String?) { }
}
