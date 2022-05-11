package com.depromeet.bds.component
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentRangeSliderBinding
import com.jaygoo.widget.OnRangeChangedListener

class BdsRangeSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val layoutInflater = LayoutInflater.from(context)
    private val binding = BdsComponentRangeSliderBinding.inflate(layoutInflater, this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.BdsRangeSlider) {
            binding.bdsRangeSeekbar.setRange(getFloat(R.styleable.BdsRangeSlider_bds_range_min,0f)
                , getFloat(R.styleable.BdsRangeSlider_bds_range_max, 100f))

            val process =getFloat(R.styleable.BdsRangeSlider_bds_progress, 0.0f)
            if(process == 0f)
                binding.bdsRangeSeekbar.setProgress(0f,0f)
            else{
                binding.bdsRangeSeekbar.setProgress(0f,process)
            }
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)
        }

    }

    fun addRangeChangeListener( seekbarChangeListener : OnRangeChangedListener?){
        if(seekbarChangeListener != null)
            binding.bdsRangeSeekbar.setOnRangeChangedListener(seekbarChangeListener)
    }
}
