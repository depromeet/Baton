package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentTagBinding

class BdsTag @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentTagBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentTagBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsTextView) {
            binding.tvText.text = getString(R.styleable.BdsTextView_bds_text)
        }
    }

    fun setText(content : String){
        binding.tvText.text = content
    }
}
