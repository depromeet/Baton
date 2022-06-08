package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPriceBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceFragment : BaseFragment<FragmentPriceBinding>(R.layout.fragment_price) {
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        initView()
        setSliderResetObserve()
        setRangeChangeListener()
    }

    private fun initView() {
        binding.bdsRangeslider.setProgress(
            filterViewModel.priceRange.value?.first ?: TermFragment.MIN,
            filterViewModel.priceRange.value?.second ?: TermFragment.PRICE_MAX
        )
    }

    private fun setPriceInitSlider() {
        binding.bdsRangeslider.setProgress(TermFragment.MIN, TermFragment.PRICE_MAX)
    }

    private fun setSliderResetObserve() {
        filterViewModel.isPriceFiltered.observe(viewLifecycleOwner) {
            if (!it && binding.tvPriceSelectedAll.visibility == View.INVISIBLE) {
                binding.tvPriceSelectedAll.visibility = View.VISIBLE
                setPriceInitSlider()
            }
        }
    }

    private fun setRangeChangeListener() {
        binding.bdsRangeslider.addRangeChangeListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
                filterViewModel.setPrice(leftValue, rightValue)
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