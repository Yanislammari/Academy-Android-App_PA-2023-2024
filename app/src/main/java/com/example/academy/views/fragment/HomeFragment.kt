package com.example.academy.views.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.academy.R
import com.example.academy.models.course.CourseModel
import com.example.academy.models.user.UserModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.viewmodels.AuthViewModel
import com.example.academy.viewmodels.CourseViewModel
import com.example.academy.views.click_listeners.CourseOnClickListener
import com.example.academy.views.activity.CourseDetailsActivity
import com.example.academy.views.activity.MainStartActivity
import com.example.academy.views.activity.SearchCourseActivity
import com.example.academy.views.recycler_view.course_big_format_recycler_view.CourseBigFormatListAdapter
import com.example.academy.views.recycler_view.course_lite_format_recycler_view.CourseLiteFormatListAdapter
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

class HomeFragment(private val userConnected: UserModel?) : Fragment(), CourseOnClickListener {
    private lateinit var courseBigFormatListRecyclerView: RecyclerView
    private lateinit var courseLiteFormatListRecyclerView: RecyclerView
    private lateinit var courseBigFormatAdapter: CourseBigFormatListAdapter
    private lateinit var courseLiteFormatAdapter: CourseLiteFormatListAdapter
    private var courseViewModel: CourseViewModel = DataLayerSingleton.getCourseViewModel()
    private var authViewModel: AuthViewModel = DataLayerSingleton.getAuthViewModel()
    private lateinit var helloTextView: TextView
    private lateinit var userConnectedProfilePicture: ImageView
    private lateinit var searchImageView: ImageView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val COURSE_MODEL_EXTRA = "COURSE_MODEL_EXTRA"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DataLayerSingleton.initialize(requireContext())

        sharedPreferences = context?.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)!!

        courseBigFormatListRecyclerView = view.findViewById(R.id.course_big_format_list_rv)
        courseLiteFormatListRecyclerView = view.findViewById(R.id.course_lite_format_list_rv)
        helloTextView = view.findViewById(R.id.home_hello_text_view)
        userConnectedProfilePicture = view.findViewById(R.id.home_user_connected_profile_picture)
        searchImageView = view.findViewById(R.id.home_search_image_view)

        helloTextView.text = "Hello ${userConnected?.firstName}"
        Glide.with(userConnectedProfilePicture.context)
            .load(userConnected?.profilePicture)
            .placeholder(R.drawable.placeholder)
            .into(userConnectedProfilePicture)

        userConnectedProfilePicture.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        searchImageView.setOnClickListener {
            Intent(requireContext(), SearchCourseActivity::class.java).also {
                startActivity(it)
            }
        }

        setupRecyclerViews()
        observeCourseListData()
    }

    override fun onResume() {
        super.onResume()
        fetchCourseLists()
    }

    private fun setupRecyclerViews() {
        courseBigFormatAdapter = CourseBigFormatListAdapter(emptyList(), this)
        courseBigFormatListRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        courseBigFormatListRecyclerView.adapter = courseBigFormatAdapter

        courseLiteFormatAdapter = CourseLiteFormatListAdapter(emptyList(), this)
        courseLiteFormatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        courseLiteFormatListRecyclerView.adapter = courseLiteFormatAdapter
    }

    private fun observeCourseListData() {
        courseViewModel.courses.observe(viewLifecycleOwner) { courseList ->
            courseBigFormatAdapter.updateData(courseList)

            courseViewModel.localCourses.observe(viewLifecycleOwner) { localCourseList ->
                val filteredLocalCourses = localCourseList.filter { localCourse ->
                    courseList.any { apiCourse -> apiCourse.title == localCourse.title }
                }
                val sortedUniqueCourses = filteredLocalCourses
                    .reversed()
                    .distinctBy {
                        it.title
                    }
                courseLiteFormatAdapter.updateData(sortedUniqueCourses)
            }
        }

        courseViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fetchCourseLists() {
        courseViewModel.fetchCoursesFromRepository(sharedPreferences?.getString("auth_token", null)!!)
        courseViewModel.getAllCourseFromDB()
    }

    override fun displayCourseDetail(course: CourseModel?) {
        Intent(requireContext(), CourseDetailsActivity::class.java).also {
            it.putExtra(COURSE_MODEL_EXTRA, course)
            startActivity(it)
        }
    }

    override fun verifyCourseExists(courseId: String?) {
        courseViewModel.getCourseById(sharedPreferences.getString("auth_token", null)!!, courseId)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Log Out")
        builder.setMessage("Do you want to log out?")

        builder.setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
            handleLogout()
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun handleLogout() {
        authViewModel.logout()
        Intent(requireContext(), MainStartActivity::class.java).also {
            startActivity(it)
            requireActivity().finish()
        }
    }
}
