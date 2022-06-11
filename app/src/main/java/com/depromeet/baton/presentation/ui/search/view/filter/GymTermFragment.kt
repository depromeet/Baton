package com.depromeet.baton.presentation.ui.search.view.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentGymTermSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GymTermFragment : BaseFragment<FragmentGymTermSearchBinding>(R.layout.fragment_gym_term_search) {
    private val filterViewModel: FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel

        setSliderResetObserve()
        setRangeChangeListener()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (filterViewModel.isResetClick.value == true) {
            setGymInitSlider()
            filterViewModel.setResetClickFalse()
        }
    }

    private fun initView() {
        try {
            binding.bdsTermRangesliderGym.setProgress(
                filterViewModel.gymTermRange.value?.first ?: TermFragment.MIN,
                filterViewModel.gymTermRange.value?.second ?: TermFragment.GYM_MAX
            )
        } catch (e: Exception) {
            setRangeChangeListener()
            Timber.e(e.message)
        }
    }

    private fun setGymInitSlider() {
        binding.bdsTermRangesliderGym.setProgress(TermFragment.MIN, TermFragment.GYM_MAX)
    }

    private fun setSliderResetObserve() {
        filterViewModel.isGymTermFiltered.observe(viewLifecycleOwner) {
            if (!it && binding.tvTermSelectedAllGym.visibility == View.INVISIBLE) {
                binding.tvTermSelectedAllGym.visibility = View.VISIBLE
                setGymInitSlider()
            }
        }
    }

    private fun setRangeChangeListener() {
        binding.bdsTermRangesliderGym.addRangeChangeListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
                filterViewModel.setGymTerm(leftValue, rightValue)
            }

            override fun onStartTrackingTouch(
                view: RangeSeekBar?,
                isLeft: Boolean
            ) {

            }

            override fun onStopTrackingTouch(
                view: RangeSeekBar?,
                isLeft: Boolean
            ) {
            }
        }
        )
    }
}