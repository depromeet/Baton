package com.depromeet.baton.presentation.ui.home.view


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTestBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.adapter.AdapterItem
import com.depromeet.baton.presentation.ui.home.adapter.StickyHeaderRecyclerViewAdapter
import com.depromeet.baton.presentation.ui.home.viewmodel.HomeViewModel
import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import com.depromeet.baton.util.BatonSpfManager
import com.skydoves.balloon.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@AndroidEntryPoint
class TestFragment : BaseFragment<FragmentTestBinding>(R.layout.fragment_test) {

    private lateinit var stickyHeaderRecyclerViewAdapter: StickyHeaderRecyclerViewAdapter

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by activityViewModels()

    @Inject
    lateinit var spfManager: BatonSpfManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onResume() {
        super.onResume()
        initLayout()
        filterViewModel.updateFilteredTicketList()
        if (homeViewModel.fromAddress.value == true) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    homeViewModel.checkToolTipState(
                        filterViewModel.ticketCount.value!!,
                        spfManager.getAddress().roadAddress
                    )
                }, 600)
            homeViewModel.setFromAddress(false)
        }
        if (spfManager.getInitToolTip()) {
            showToolTip()
            spfManager.setInitToolTip(false)
        }
    }

    private fun initView() {
        initLayout()
        setObserve()
        initAdapter()
        /*FragmentTransaction.add()에 전달된 ID(예: R.id.feedContentContainer)는 setContentView()에 지정된 레이아웃의 자식이어야 한다.
        아무리 inflate해도  java.lang.IllegalArgumentException: No view found for id for fragment filterchipfragment뜬이유
        LayoutInflter.from(context).inflate(R.layout.fragment_header2, null)*/
    }

    private fun setObserve() {
        homeViewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)

        homeViewModel.homeUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
    }

    private fun initLayout() {
        val roadAddress = spfManager.getAddress().roadAddress

        binding.tvHomeLocation.text = if (roadAddress != "") {
            roadAddress.slice(0..9) + "..."
        } else "위치 설정"
    }

    private fun initAdapter() {
        val recyclerItemList: ArrayList<AdapterItem> = ArrayList()

        stickyHeaderRecyclerViewAdapter = StickyHeaderRecyclerViewAdapter(
            filterViewModel,
            viewLifecycleOwner,
            parentFragmentManager,
            requireContext()
        )
        binding.rvHome.adapter = stickyHeaderRecyclerViewAdapter
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.addItemDecoration(StickyHeaderItemDecoration(getSectionCallback()))

        recyclerItemList.add(AdapterItem(StickyHeaderRecyclerViewAdapter.TOP, false))
        recyclerItemList.add(AdapterItem(StickyHeaderRecyclerViewAdapter.HEADER, true))
        recyclerItemList.add(AdapterItem(StickyHeaderRecyclerViewAdapter.BOTTOM, false))

        stickyHeaderRecyclerViewAdapter.submitList(recyclerItemList)
    }

    private fun getSectionCallback(): StickyHeaderItemDecoration.SectionCallback {
        return object : StickyHeaderItemDecoration.SectionCallback {
            override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
                return stickyHeaderRecyclerViewAdapter.getHeaderView(list, position)
            }
        }
    }

    private fun showToolTip() {
        val balloon = Balloon.Builder(requireContext())
            .setWidthRatio(0.0f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setElevation(3)
            .setMarginBottom(10)
            .setMarginLeft(16)
            .setTextSize(10f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowDrawableResource(com.depromeet.bds.R.drawable.ic_tooltip_subtract)
            .setArrowSize(10)
            .setArrowPosition(0.57f)
            .setPadding(10)
            .setCornerRadius(4f)
            .setBackgroundColorResource(com.depromeet.bds.R.color.bg)
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .setLayout(com.depromeet.baton.R.layout.tooltip_home)
            .setLifecycleOwner(this)
            .build()
        binding.viewTooltip.showAlignBottom(balloon, 0, 0)
    }

    private fun handleViewEvents(viewEvents: List<HomeViewModel.HomeViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                HomeViewModel.HomeViewEvent.ToLocation -> {
                    homeViewModel.setFromAddress(true)
                    AddressActivity.start(requireContext())
                }
                HomeViewModel.HomeViewEvent.ToSearch -> (activity as MainActivity).moveToSearch() //todo 검색 keyword 초기화

                HomeViewModel.HomeViewEvent.ToAlarm -> (activity as MainActivity).moveToChatting()

                HomeViewModel.HomeViewEvent.ToWritePost -> WritePostActivity.start(requireContext())

                HomeViewModel.HomeViewEvent.ShowToolTip -> showToolTip()
            }
            homeViewModel.consumeViewEvent(viewEvent)
        }
    }
}

