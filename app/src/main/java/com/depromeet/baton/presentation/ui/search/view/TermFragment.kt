package com.depromeet.baton.presentation.ui.search.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTermBinding
import com.depromeet.baton.databinding.FragmentTermSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.baton.presentation.ui.search.FilterSearchViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermFragment : BaseFragment<FragmentTermSearchBinding>(R.layout.fragment_term_search) {
    private val filterViewModel: FilterSearchViewModel by activityViewModels()
    private val gymTermFragment: GymTermFragment by lazy { GymTermFragment() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel

        setRangeChangeListener()
        setGoToTicketKindFragment()
        setSliderResetObserve()
        setGymTermFragment()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (binding.tvTermSelectedAllPt.visibility == View.VISIBLE) setPtInitSlider()
    }

    private fun initView() {
        binding.bdsTermRangesliderPt.setProgress(
            filterViewModel.ptTermRange.value?.first ?: MIN,
            filterViewModel.ptTermRange.value?.second ?: PT_MAX
        )
    }

    private fun setPtInitSlider() {
        binding.bdsTermRangesliderPt.setProgress(MIN, PT_MAX)
    }

    private fun setSliderResetObserve() {
        filterViewModel.isPtTermFiltered.observe(viewLifecycleOwner) {
            if (!it && binding.tvTermSelectedAllPt.visibility == View.INVISIBLE) {
                binding.tvTermSelectedAllPt.visibility = View.VISIBLE
                setPtInitSlider()
            }
        }
    }

    private fun setGoToTicketKindFragment() {
        binding.bdsBtnTermSelectKind.setOnClickListener {
          //  filterViewModel.setCurrentFilterPosition(0)
        }
    }

    private fun setRangeChangeListener() {
        binding.bdsTermRangesliderPt.addRangeChangeListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
                filterViewModel.setPtTerm(leftValue, rightValue)
            }

            override fun onStartTrackingTouch(
                view: RangeSeekBar?,
                isLeft: Boolean
            ) {
            }

            override fun onStopTrackingTouch(
                view: RangeSeekBar?,
                isRight: Boolean
            ) {
            }
        }
        )
    }

    private fun setGymTermFragment() {
        childFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fcv_term_gym, gymTermFragment, "gymTermFragment")
            .commit()
    }

    companion object {
        const val MIN = 0f
        const val PT_MAX = 60f
        const val GYM_MAX = 12f
        const val PRICE_MAX = 1500000f
    }
}