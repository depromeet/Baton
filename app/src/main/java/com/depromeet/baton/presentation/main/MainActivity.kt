package com.depromeet.baton.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMainBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.chatting.ChattingFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.home.view.HomeFragment
import com.depromeet.baton.presentation.ui.mypage.view.MyPageFragment
import com.depromeet.baton.presentation.ui.search.view.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val searchFragment: SearchFragment by lazy { SearchFragment() }
    private val chattingFragment: ChattingFragment by lazy { ChattingFragment() }
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }


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

    fun bottomNavigationHandler(id : Int){
        binding.bnvMain.selectedItemId= binding.bnvMain.menu.findItem(id).itemId
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
//                R.id.menu_main_chatting -> {
//                    replace(chattingFragment)
//                    return@setOnItemSelectedListener true
//                }
                R.id.menu_main_mypage -> {
                    replace(myPageFragment, "myPageFragment")
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun replace(fragment: Fragment, tag: String? = null) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_main, fragment, tag)
            .commit()
    }

    fun moveToSearch() {
        binding.bnvMain.selectedItemId = R.id.menu_main_search
    }


    fun moveToHome() {
        binding.bnvMain.selectedItemId = R.id.menu_main_home
    }

    fun moveToChatting() {
//        binding.bnvMain.selectedItemId = R.id.menu_main_chatting
    }

    override fun onBackPressed() {
        super.onBackPressed()
       //finish()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}