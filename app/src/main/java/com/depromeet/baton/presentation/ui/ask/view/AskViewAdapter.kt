package com.depromeet.baton.presentation.ui.ask.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import timber.log.Timber

class AskViewAdapter  (
    private val fm: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }
    override fun createFragment(position: Int): Fragment {
        try{
            val tab=  when (position) {
                0 -> AskSendFragment()
                1-> AskRecvFragment()
                else -> error("can not find fragment")
            }
            return tab
        }catch (e : Error){
            Timber.e(e.message)
            return throw(e)
        }

    }

}