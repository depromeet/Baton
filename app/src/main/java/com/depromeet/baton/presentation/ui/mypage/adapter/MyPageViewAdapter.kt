package com.depromeet.baton.presentation.ui.mypage.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.depromeet.baton.presentation.ui.mypage.view.SaleTabFragment
import com.depromeet.baton.presentation.ui.mypage.view.SoldoutTabFragment
import timber.log.Timber
import java.lang.IllegalArgumentException

class MyPageViewAdapter (
    private val fm:FragmentManager,lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount(): Int {
       return 2
    }

    override fun createFragment(position: Int): Fragment {
       try{

         val tab=  when (position) {
               0 -> SaleTabFragment()
               1-> SoldoutTabFragment()
               else -> error("can not find fragment")
           }
         return tab
       }catch (e : Error){
           Timber.e(e.message)
           return throw(e)
       }

    }

}