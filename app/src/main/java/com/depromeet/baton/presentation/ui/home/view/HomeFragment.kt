package com.depromeet.baton.presentation.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity

import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFloatingActionBtnClickListener()
        setTicketItemRvAdapter()
    }

    private fun setFloatingActionBtnClickListener() {
        binding.fabHome.setOnClickListener {
            startActivity(Intent(requireContext(), WritePostActivity::class.java))
        }
    }

    private fun setTicketItemRvAdapter() {
        with(binding) {
            val ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, ::setTicketItemClickListener)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            rvHome.layoutManager = gridLayoutManager

            ticketItemRvAdapter.submitList(
                arrayListOf(
                    TicketItem("휴메이크 휘트니스 석촌점", "헬스", "123,000원", "50일 남음", "광진구 중곡동", "12m"),
                    TicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m"),
                    TicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m"),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m"),
                    TicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m"),
                )
            )
        }
    }

    private fun setTicketItemClickListener(ticketItem: TicketItem) {
        startActivity(Intent(requireContext(), TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }
}