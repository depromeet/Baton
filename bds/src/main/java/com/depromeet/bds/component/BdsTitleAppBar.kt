package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentTitleAppBarBinding

class BdsTitleAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentTitleAppBarBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentTitleAppBarBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsTitleAppBar) {
            val title = getString(R.styleable.BdsTitleAppBar_bds_title)
            val iconResId = getDrawable(R.styleable.BdsTitleAppBar_bds_icon)
            val additionalIconResId = getDrawable(R.styleable.BdsTitleAppBar_bds_additional_icon)
            val buttonTitle = getString(R.styleable.BdsTitleAppBar_bds_button)
            when {
                additionalIconResId != null && iconResId == null ->
                    throw IllegalStateException("bds_icon 사용")
                iconResId != null && buttonTitle.isNullOrBlank().not() ->
                    throw IllegalStateException("icon, button 동시 사용 불가")
            }

            binding.tvTitle.text = title

            binding.imageFirstIcon.isVisible = iconResId != null
            binding.imageFirstIcon.setImageDrawable(iconResId)

            binding.imageSecondIcon.isVisible = additionalIconResId != null
            binding.imageSecondIcon.setImageDrawable(additionalIconResId)

            binding.vMarginBtwIcon.isVisible =
                binding.imageFirstIcon.isVisible && binding.imageSecondIcon.isVisible

            binding.button.isVisible = buttonTitle.isNullOrBlank().not()
            binding.button.text = buttonTitle
        }
    }
}
