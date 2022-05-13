package com.depromeet.baton.presentation.ui.filter.view

import android.os.Bundle
import android.text.Editable
import com.jaygoo.widget.OnRangeChangedListener
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPriceBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.filter.viewmodel.FilterViewModel
import com.depromeet.bds.component.BdsSearchBar
import com.jaygoo.widget.RangeSeekBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PriceFragment : BaseFragment<FragmentPriceBinding>(R.layout.fragment_price) {
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterViewModel=filterViewModel
        setRangeChangeListener()
    }

    //왼쪽 오른쪽 값 모두 넘겨주어야함
    private fun setProgress(left: Float, right: Float) {
        binding.bdsRangeslider.setProgress(left, right)
    }

    private fun setRangeChangeListener() {
      //  setProgress(0f,150f)
        binding.bdsRangeslider.addRangeChangeListener(object : OnRangeChangedListener {
            override fun onRangeChanged(
                rangeSeekBar: RangeSeekBar, leftValue: Float,
                rightValue: Float, isFromUser: Boolean
            ) {
                filterViewModel.setPrice(leftValue,rightValue)
               // if
                //뷰모델한테 MIN MAX값 넘겨줌

          //      binding.tvSliderMin.text=leftValue.toString()
            //    setProgress(leftValue,rightValue)
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