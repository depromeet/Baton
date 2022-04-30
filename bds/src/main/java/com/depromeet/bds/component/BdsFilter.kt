package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentFilterBinding

class BdsFilter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {

        val layoutInflater = LayoutInflater.from(context)
        val binding = BdsComponentFilterBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsFilter) {
            binding.tvText.text = getString(R.styleable.BdsFilter_bds_text)

            val startIconResId = getResourceIdOrNull(R.styleable.BdsFilter_bds_startDrawable)
            val endIconResId = getResourceIdOrNull(R.styleable.BdsFilter_bds_endDrawable)

            binding.ivStartDrawable.isVisible = startIconResId != null
            binding.vStartMargin.isVisible = startIconResId == null
            binding.ivEndDrawable.isVisible = endIconResId != null
            binding.vEndMargin.isVisible = endIconResId == null

            binding.ivStartDrawable.setImageDrawable(startIconResId.toDrawable(context))
            binding.ivEndDrawable.setImageDrawable(endIconResId.toDrawable(context))
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.tvText.setTextAppearanceCompat(
                getResourceId(R.styleable.BdsView_bds_textAppearance, 0)
            )

            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)
        }
    }
}
