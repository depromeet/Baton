package com.depromeet.bds.component

import android.content.Context

import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentSliderBinding

class BdsSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {


    private val layoutInflater = LayoutInflater.from(context)
    private  val binding = BdsComponentSliderBinding.inflate(layoutInflater, this, true)


    init {
        context.withStyledAttributes(attrs, R.styleable.BdsSlider) {
            binding.bdsSlider.progress = getInteger( R.styleable.BdsSlider_bds_progress,0)
            binding.bdsSlider.max = getInteger(R.styleable.BdsSlider_bds_max,0)
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)
        }

    }

    fun addSeekbarChangeListener(seekbarChangeListener  : SeekBar.OnSeekBarChangeListener?){
        binding.bdsSlider.setOnSeekBarChangeListener(seekbarChangeListener )
    }

    fun setProgress(value: Int){
        binding.bdsSlider.progress=value
    }

}
