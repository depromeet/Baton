package com.depromeet.baton.presentation.ui.filter.view

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomFilterBinding
import com.depromeet.baton.presentation.ui.filter.adapter.TabLayoutAdapter
import com.depromeet.baton.presentation.ui.filter.getFilterTypeFromPosition
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.home.view.HomeFragment
import com.depromeet.baton.presentation.util.ResourcesProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomFilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomFilterBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private val filterViewModel: FilterViewModel by activityViewModels()

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_filter, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayoutAdapter()
        initTabLayout()
        setTabPosition()
        setFilterResetOnClickListener()
        setFilterSearchOnClickListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    private fun initTabLayoutAdapter() {
        val fragmentList = listOf(
            AlignmentFragment(),
            TicketKindFragment(),
            TermFragment(),
            PriceFragment(),
            TransactionMethodFragment(),
            AdditionalOptionsFragment(),
            HashTagFragment()
        )
        tabLayoutAdapter = TabLayoutAdapter(this)
        tabLayoutAdapter.fragments.addAll(fragmentList)
        binding.vpBottomFilter.adapter = tabLayoutAdapter
    }

    private fun initTabLayout() {
        val tabLabel = listOf(
            resourcesProvider.getString(R.string.filter_alignment),
            resourcesProvider.getString(R.string.filter_ticket_kind),
            resourcesProvider.getString(R.string.filter_term),
            resourcesProvider.getString(R.string.filter_price),
            resourcesProvider.getString(R.string.filter_transaction_method),
            resourcesProvider.getString(R.string.filter_additional_options),
            resourcesProvider.getString(R.string.filter_hashtag),
        )
        TabLayoutMediator(binding.tlBottomFilter, binding.vpBottomFilter) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()
    }

    private fun setTabPosition() {
        val position = arguments?.getInt(HomeFragment.CHECKED_FILTER_POSITION_KEY, 0) ?: 0
        binding.vpBottomFilter.setCurrentItem(position, false)
    }

    private fun setFilterResetOnClickListener() {
        binding.btnBottomFilterReset.setOnClickListener {
            filterViewModel.filterReset(getFilterTypeFromPosition(binding.tlBottomFilter.selectedTabPosition) ?: return@setOnClickListener)
        }
    }

    private fun setFilterSearchOnClickListener() {
        binding.llBottomFilterSearch.setOnClickListener {
            Handler(Looper.getMainLooper())
                .postDelayed({ dialog?.dismiss() }, 200)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
