package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentInputChipBinding

class BdsInputChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val binding: BdsComponentInputChipBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentInputChipBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsTextView) {
            val text = getString(R.styleable.BdsTextView_bds_text)
            binding.tvText.text = text
        }
    }
}
