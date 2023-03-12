package com.depromeet.baton.presentation.ui.ask.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentAskBinding
import com.depromeet.baton.databinding.FragmentSaleTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.ask.viewModel.AskViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class AskFragment: BaseFragment<FragmentAskBinding>(R.layout.fragment_ask),MainActivity.OnBackPressedListener{
    private lateinit var pagerAdapter: AskViewAdapter
    private val askViewModel : AskViewModel by  activityViewModels()
    private val titles = listOf(
        "보낸 내역",
        "받은 내역"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    override fun onBackPressed(){
        requireActivity().supportFragmentManager.popBackStack()
        (activity as MainActivity).bottomNavigationHandler(R.id.menu_main_home)
    }

    private fun initViewPager(){
        runCatching{
            pagerAdapter = AskViewAdapter(childFragmentManager,this.lifecycle)
            binding.viewPager.adapter = pagerAdapter
            binding.viewPager.isSaveEnabled= false

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = titles[position]
            }.attach()
        }.onFailure {
            Timber.e(it.message)
        }

    }

}