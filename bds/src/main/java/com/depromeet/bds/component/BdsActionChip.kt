package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsActionChipBinding

class BdsActionChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val binding: BdsActionChipBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsActionChipBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsTextView) {
            val text = getString(R.styleable.BdsTextView_bds_text)
            binding.tvText.text = text
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
        }
    }
}
