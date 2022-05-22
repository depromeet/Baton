package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentLikeTicketBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration

class LikeTicketFragment : BaseFragment<FragmentLikeTicketBinding>(R.layout.fragment_like_ticket) {
    private val ticketItemRvAdapter by lazy{
        TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, requireContext(), ::setTicketItemClickListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setOnBackPressed()
    }

    private fun initRecyclerView(){
        with(binding) {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            mypageLikeRv.layoutManager = gridLayoutManager

            ticketItemRvAdapter.submitList(
                arrayListOf(
                    TicketItem(
                        "휴메이크 휘트니스 석촌점", "헬스", "123,000원", "50일 남음", "광진구 중곡동", "12m", R.drawable.dummy1),
                    TicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy2),
                    TicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy4),
                    TicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy5),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy7),
                )
            )
        }
    }

    private fun setOnBackPressed(){
        binding.likeTicketToolbar.setOnBackwardClick { parentFragmentManager.popBackStack() }
    }

    private fun setTicketItemClickListener(ticketItem: TicketItem) {
        //TODO 관심상품 삭제
    }

}