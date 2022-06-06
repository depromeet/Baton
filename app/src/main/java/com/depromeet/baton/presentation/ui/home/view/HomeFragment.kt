package com.depromeet.baton.presentation.ui.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.data.mapper.SearchMapper
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.UIState
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
    private lateinit var ticketItemRvAdapter: TicketItemRvAdapter

    @Inject
    lateinit var spfManager: BatonSpfManager


    override fun onResume() {
        super.onResume()
        filterViewModel.setFilterTypeOrderList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        initView()
    }

    private fun initView() {
        filterViewModel.updateFilteredTicketList()
        setInitBtnClickListener()
        setTicketItemRvAdapter()
        setStickyScroll()

        binding.tvHomeLocation.text = if (spfManager.getAddress().roadAddress != "") spfManager.getAddress().roadAddress.slice(0..5) + "..."
        else "위치 설정"
    }

    private fun setStickyScroll() {
        binding.svHome.run {
            header = binding.ctlHomeFcv
        }
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
            ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, requireContext(), ::setTicketItemClickListener)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            rvHome.layoutManager = gridLayoutManager
        }


        val list = listOf(
            ResponseFilteredTicket(
                id = 0,
                location = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                address = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                price = 1500000,
                mainImage = "https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg",
                createAt = "2022-42-42",
                state = "FD",
                tags = listOf("체계적인 수업", "친절한 선생님", "어쩌구", "하하하"),
                images = listOf(com.depromeet.baton.data.response.ResponseFilteredTicket.Image(1, ",", "", false)),
                isMembership = true,
                remainingNumber = 290,
                expiryDate = "1010-10-10",
                latitude = 36.1234,
                longitude = 36.1234,
                distance = 699.9
            ),
            ResponseFilteredTicket(
                id = 0,
                location = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                address = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                price = 1500000,
                mainImage = "https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg",
                createAt = "2022-42-42",
                state = "FD",
                tags = listOf(),
                images = listOf(com.depromeet.baton.data.response.ResponseFilteredTicket.Image(1, ",", "", false)),
                isMembership = true,
                remainingNumber = 290,
                expiryDate = "1010-10-10",
                latitude = 36.1234,
                longitude = 36.1234,
                distance = 699.9
            ),
            ResponseFilteredTicket(
                id = 0,
                location = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                address = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                price = 1500000,
                mainImage = "https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg",
                createAt = "2022-42-42",
                state = "FD",
                tags = listOf("체계적인 수업"),
                images = listOf(com.depromeet.baton.data.response.ResponseFilteredTicket.Image(1, ",", "", false)),
                isMembership = true,
                remainingNumber = 290,
                expiryDate = "1010-10-10",
                latitude = 36.1234,
                longitude = 36.1234,
                distance = 699.9
            ),
            ResponseFilteredTicket(
                id = 0,
                location = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                address = "일이삼사오육칠팔구십일이삼사오육치팔구십일이삼사",
                price = 1500000,
                mainImage = "https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg",
                createAt = "2022-42-42",
                state = "FD",
                tags = listOf("체계적인 수업"),
                images = listOf(com.depromeet.baton.data.response.ResponseFilteredTicket.Image(1, ",", "", false)),
                isMembership = true,
                remainingNumber = 290,
                expiryDate = "1010-10-10",
                latitude = 36.1234,
                longitude = 36.1234,
                distance = 699.9
            )
        )
        //TODO filteredTicketList로 넘기기
        ticketItemRvAdapter.submitList(list)
    }

    private fun setTicketItemClickListener(ticketItem: ResponseFilteredTicket) {
        startActivity(Intent(requireContext(), TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }
}



