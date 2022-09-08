package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTestBinding
import com.depromeet.baton.databinding.FragmentTopBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.home.adapter.RecyclerView2Adapter
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopFragment : BaseFragment<FragmentTopBinding>(R.layout.fragment_top) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}