package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import android.view.View
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}