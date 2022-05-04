package com.depromeet.baton.presentation.ui.filter.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomFilterBinding
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.presentation.ui.filter.adapter.TabLayoutAdapter
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomFilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomFilterBinding? = null
    private val binding get()= _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")

    private lateinit var tabLayoutAdapter: TabLayoutAdapter
    private val filterViewModel: FilterViewModel by activityViewModels()

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
        initTabLayout()
        setCurrentFilterPosition()
        setFilterResetOnClickListener()
        setFilterSearchOnClickListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    private fun initTabLayout() {
        filterViewModel.filterTypeOrderList.observe(viewLifecycleOwner) { filterTypeOrderList ->
            if (filterTypeOrderList == null) return@observe

            val fragmentList = mutableListOf<Fragment>()
            for (filterType in filterTypeOrderList) {
                when (filterType) {
                    FilterType.Alignment.value -> fragmentList.add(AlignmentFragment())
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

    private fun setCurrentFilterPosition() {
        filterViewModel.currentFilterPosition.observe(viewLifecycleOwner) { currentFilterPosition ->
            binding.vpBottomFilter.setCurrentItem(currentFilterPosition, false)
        }
    }

    private fun setFilterResetOnClickListener() {
        binding.btnBottomFilterReset.setOnClickListener {
            filterViewModel.setCurrentFilterBottomPosition(binding.vpBottomFilter.currentItem)
            filterViewModel.filterReset()
        }
    }

    private fun setFilterSearchOnClickListener() {
        binding.llBottomFilterSearch.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
