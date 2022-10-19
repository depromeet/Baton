package com.depromeet.baton.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMainBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.ask.view.AskFragment
import com.depromeet.baton.presentation.ui.home.view.HomeFragment
import com.depromeet.baton.presentation.ui.mypage.view.MyPageFragment
import com.depromeet.baton.presentation.ui.search.view.SearchFragment
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val searchFragment: SearchFragment by lazy { SearchFragment() }
    private val askFragment : AskFragment by lazy { AskFragment()}
    private val myPageFragment: MyPageFragment by lazy { MyPageFragment() }
    private var backBtnWaitTime = 0L
    private val toast: Toast by lazy { this.BdsToast("'뒤로' 버튼을 한번 더 누르시면 종료됩니다.") }

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

                R.id.menu_main_chatting -> {
                    replace(askFragment)
                    return@setOnItemSelectedListener true
                }
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
        //    binding.bnvMain.selectedItemId = R.id.menu_main_chatting
    }

    override fun onBackPressed() {
        //검색뷰 백스택
        val fragmentList = supportFragmentManager.fragments
        for (fragment in fragmentList) {
            if (fragment is OnBackPressedListener) {
                (fragment as OnBackPressedListener).onBackPressed()
                return
            }
        }

        if (System.currentTimeMillis() - backBtnWaitTime >= BACK_BTN_WAIT_TIME) {
            backBtnWaitTime = System.currentTimeMillis()
            toast.show()
        } else {
            toast.cancel()
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }
    }

    interface OnBackPressedListener {
        fun onBackPressed()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        private const val BACK_BTN_WAIT_TIME = 2000L
    }
}
