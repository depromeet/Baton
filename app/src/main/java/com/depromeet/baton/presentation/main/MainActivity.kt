package com.depromeet.baton.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMainBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.chatting.ChattingFragment
import com.depromeet.baton.presentation.ui.home.view.HomeFragment
import com.depromeet.baton.presentation.ui.mypage.view.MyPageFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.MyPageViewModel
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val searchFragment: SearchFragment by lazy { SearchFragment() }
    private val chattingFragment: ChattingFragment by lazy { ChattingFragment() }
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }

    private val profileViewModel by lazy { viewModels<ProfileViewModel>()}
    private val myPageViewModel  by lazy { viewModels<MyPageViewModel>()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBottomNavigation()
        setBottomNavigationSelectedListener()
    }

    private fun initBottomNavigation() {
        replace(homeFragment)
        binding.bnvMain.itemIconTintList = null
    }

    private fun setBottomNavigationSelectedListener() {
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_home -> {
                    replace(homeFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_main_search -> {
                    replace(searchFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_main_chatting -> {
                    replace(chattingFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.menu_main_mypage -> {
                    replace(myPageFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main, fragment, null)
            .commit()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}