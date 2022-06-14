package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.databinding.FragmentLikeTicketBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.mypage.adapter.BookMarkItemRvAdapter
import com.depromeet.baton.presentation.ui.mypage.viewmodel.MyBookmarkViewModel
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class LikeTicketFragment : BaseFragment<FragmentLikeTicketBinding>(R.layout.fragment_like_ticket) {
    private val viewModel by viewModels<MyBookmarkViewModel>()

    private val ticketItemRvAdapter by lazy{
        BookMarkItemRvAdapter( requireContext(), ::setTicketItemClickListener, ::setTicketBookmarkListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        setOnBackPressed()
        setObserver()
    }

    private fun setObserver(){

        viewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { state ->
                run{
                    binding.uistate=state
                    ticketItemRvAdapter.submitList( state.list )
                }
            }
            .launchIn(viewLifecycleScope)

    }

    private fun initRecyclerView(){
        with(binding) {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            mypageLikeRv.adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            mypageLikeRv.layoutManager = gridLayoutManager

        }
    }

    private fun setOnBackPressed(){
        binding.likeTicketToolbar.setOnBackwardClick { parentFragmentManager.popBackStack() }
    }


    private fun setTicketItemClickListener(ticketItem:BookmarkTicket) {
        //TODO 관심상품 삭제
        startActivity(TicketDetailActivity.start(requireContext(),ticketItem.ticket.id))
    }

    private fun setTicketBookmarkListener(ticketItem: BookmarkTicket, position :Int){
        viewModel.deleteBookMark(ticketItem.id)
    }

}