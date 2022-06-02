package com.depromeet.baton.presentation.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.ui.search.SearchViewModel
import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {


    private val searchViewModel: SearchViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()

    @Inject
    lateinit var spfManager: BatonSpfManager


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitBtnClickListener()
        setTicketItemRvAdapter()
    }


    override fun onResume() {
        super.onResume()
        binding.tvHomeLocation.text = if (spfManager.getAddress().roadAddress != "") spfManager.getAddress().roadAddress.slice(0..5) + "..."
        else "위치 설정"
    }

    private fun setInitBtnClickListener() {
        with(binding) {
            tvHomeLocation.setOnClickListener {
                val intent = Intent(requireContext(), AddressActivity::class.java)
                startActivity(intent)
            }
            fabHome.setOnClickListener {
                startActivity(Intent(requireContext(), WritePostActivity::class.java))
            }
            btnHomeMore.setOnClickListener {
                startActivity(Intent(requireContext(), HowToUseActivity::class.java))
            }
            ivHomeSearch.setOnClickListener {
                (activity as MainActivity).moveToSearch()
            }

            llHomeSpecificGym.setOnClickListener {
                searchViewModel.searchKeyword("헬스 회원권")
                (activity as MainActivity).moveToSearch()
            }

            llHomeSpecificEtc.setOnClickListener {
                searchViewModel.searchKeyword("기타")
                (activity as MainActivity).moveToSearch()
            }

            llHomeSpecificPilates.setOnClickListener {
                searchViewModel.searchKeyword("필라테스 회원권")
                (activity as MainActivity).moveToSearch()
            }

            llHomeSpecificPt.setOnClickListener {
                searchViewModel.searchKeyword("PT 이용권")
                (activity as MainActivity).moveToSearch()
            }
        }
    }

    private fun setTicketItemRvAdapter() {
        with(binding) {
            val ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, requireContext(), ::setTicketItemClickListener)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            rvHome.layoutManager = gridLayoutManager

            filterViewModel.filteredTicketList.observe(viewLifecycleOwner) {
                ticketItemRvAdapter.submitList(it)
            }
        }
    }


    private fun setTicketItemClickListener(ticketItem: ResponseFilteredTicket) {
        startActivity(Intent(requireContext(), TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }
}



