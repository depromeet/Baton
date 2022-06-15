package com.depromeet.baton.presentation.ui.mypage.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageViewAdapter (
    fragmentActivity : Fragment,
    var fragments: MutableList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {


/*    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size-1)
    }*/

    override fun getItemCount(): Int {
       return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}