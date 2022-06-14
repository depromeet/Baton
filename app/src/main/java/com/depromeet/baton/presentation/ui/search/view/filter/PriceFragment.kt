package com.depromeet.baton.presentation.ui.search.view.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPriceSearchBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.search.viewmodel.FilterSearchViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceFragment : BaseFragment<FragmentPriceSearchBinding>(R.layout.fragment_price_search) {
    private val filterSearchViewModel: FilterSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterSearchViewModel
        initView()
        setSliderResetObserve()
        setRangeChangeListener()
    }

    private fun initView() {
        binding.bdsRangeslider.setProgress(
            filterSearchViewModel.priceRange.value?.first ?: TermFragment.MIN,
            filterSearchViewModel.priceRange.value?.second ?: TermFragment.PRICE_MAX
        )
    }

    private fun setPriceInitSlider() {
        binding.bdsRangeslider.setProgress(TermFragment.MIN, TermFragment.PRICE_MAX)
    }

    private fun setSliderResetObserve() {
        filterSearchViewModel.isPriceFiltered.observe(viewLifecycleOwner) {
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
                filterSearchViewModel.setPrice(leftValue, rightValue)
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