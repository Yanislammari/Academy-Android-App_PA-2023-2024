package com.example.academy.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.academy.R
import com.example.academy.databinding.ActivityMainBinding
import com.example.academy.models.user.UserModel
import com.example.academy.utils.singleton.DataLayerSingleton
import com.example.academy.views.fragment.HomeFragment
import com.example.academy.views.fragment.MessagesFragment
import com.example.academy.views.fragment.MyCoursesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentName: String

    companion object {
        const val EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME"
        const val USER_MODEL_EXTRA = "USER_MODEL_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DataLayerSingleton.userConnected = getIntentExtraData()
        fragmentName = intent.getStringExtra(EXTRA_FRAGMENT_NAME) ?: throw IllegalArgumentException("Fragment name must be provided")

        when(fragmentName) {
            "HomeFragment" -> {
                binding.bottomNavigationView.selectedItemId = R.id.home
                replaceFragment(HomeFragment(getIntentExtraData()))
            }
            "MyCoursesFragment" -> {
                binding.bottomNavigationView.selectedItemId = R.id.myCourses
                replaceFragment(MyCoursesFragment())
            }
            "MessageFragment" -> {
                binding.bottomNavigationView.selectedItemId = R.id.messages
                replaceFragment(MessagesFragment())
            }
            else -> {
                throw Error("Error in Navigation Bar selection!")
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> replaceFragment(HomeFragment(getIntentExtraData()))
                R.id.myCourses -> replaceFragment(MyCoursesFragment())
                R.id.messages -> replaceFragment(MessagesFragment())
                else -> {
                    throw Error("Error in Navigation Bar selection!")
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun getIntentExtraData(): UserModel? {
        return intent.getParcelableExtra(USER_MODEL_EXTRA)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
