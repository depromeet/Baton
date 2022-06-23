package com.depromeet.baton.presentation.ui.search.view.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomFilterSearchBinding
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.presentation.ui.filter.adapter.FilteredChipRvAdapter
import com.depromeet.baton.presentation.ui.filter.adapter.TabLayoutAdapter
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import com.depromeet.baton.presentation.util.ChipSpacesItemDecoration
import com.depromeet.bds.utils.toPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BottomFilterSearchFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomFilterSearchBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private val filterSearchViewModel: FilterSearchViewModel by activityViewModels()

    lateinit var filteredChipRvAdapter: FilteredChipRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_filter_search, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initTabLayout()
        setCurrentFilterPosition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterSearchViewModel
        initTabLayout()
        setFilteredChipRvAdapter()
        setSearchOnClickListener()
        setCurrentFilterPosition()
        setFilteredChipObserve()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog).apply {
            setOnShowListener { setupFullHeight(it as BottomSheetDialog) }
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun initTabLayout() {
        val initFilterTypeOrderList = listOf(
            FilterType.TicketKind.value, FilterType.Term.value, FilterType.Price.value, FilterType.TransactionMethod.value, FilterType.HashTag.value
        )

        filterSearchViewModel.filterTypeOrderList.observe(viewLifecycleOwner) { filterTypeOrderList ->
            if (filterTypeOrderList == null || initFilterTypeOrderList == filterTypeOrderList) return@observe
            else {
                val fragmentList = mutableListOf<Fragment>()
                for (filterType in filterTypeOrderList) {
                    when (filterType) {
                        FilterType.TicketKind.value -> fragmentList.add(TicketKindFragment())
                        FilterType.Term.value -> fragmentList.add(TermFragment())
                        FilterType.Price.value -> fragmentList.add(PriceFragment())
                        FilterType.TransactionMethod.value -> fragmentList.add(TransactionMethodFragment())
                        FilterType.AdditionalOptions.value -> fragmentList.add(AdditionalOptionsFragment())
                        FilterType.HashTag.value -> fragmentList.add(HashTagFragment())
                    }
                }

                tabLayoutAdapter = TabLayoutAdapter(this)
                tabLayoutAdapter.fragments.addAll(fragmentList)
                binding.vpBottomFilter.adapter = tabLayoutAdapter

                TabLayoutMediator(binding.tlBottomFilter, binding.vpBottomFilter) { tab, position ->
                    tab.text = filterTypeOrderList[position]
                }.attach()
            }
        }
    }

    private fun setCurrentFilterPosition() {
        filterSearchViewModel.currentFilterPosition.observe(viewLifecycleOwner) { currentFilterPosition ->
            binding.vpBottomFilter.setCurrentItem(currentFilterPosition, false)
        }
    }

    private fun setSearchOnClickListener() {
        binding.btnBottomFilterSearch.setOnClickListener {
            filterSearchViewModel.setFilterPosition()
            dialog?.dismiss()
        }
    }

    private fun setFilteredChipRvAdapter() {
        with(binding) {
            filteredChipRvAdapter = FilteredChipRvAdapter(filterViewModel ?: return, requireContext())
            rvBottomFilter.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = filteredChipRvAdapter
            itemDecoration = ChipSpacesItemDecoration(8.toPx())
        }
    }

    private fun setFilteredChipObserve() {
        filterSearchViewModel.filteredChipList.observe(viewLifecycleOwner) { filteredChipList ->
            filteredChipRvAdapter.submitList(filteredChipList?.map { it }?.distinct()) {
                if (filteredChipList != null) {
                    binding.rvBottomFilter.scrollToPosition(filteredChipRvAdapter.itemCount - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        filterSearchViewModel.setFilterTypeOrderList()
        super.onDestroyView()
        _binding = null
    }
}
