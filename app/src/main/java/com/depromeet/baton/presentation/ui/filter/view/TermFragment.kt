package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentTermBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TermFragment : BaseFragment<FragmentTermBinding>(R.layout.fragment_term) {
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel = filterViewModel
        setRangeChangeListener()
        binding.bdsTermRangeslider.setProgress(0f, 60f)
        //제일 처음에 세팅해두기
    }


    private fun setRangeChangeListener() {
        binding.bdsTermRangeslider.addRangeChangeListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
                filterViewModel.setTerm(leftValue, rightValue)
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