package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentButtonBinding

class BdsButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    // primary, secondary, outlined, tertiary ->
    // 컬러는 theme 선에서 정의되어있고, 각각 별도의 theme을 적용
    init {

        val layoutInflater = LayoutInflater.from(context)
        val binding = BdsComponentButtonBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsButton) {
            binding.tvText.text = getString(R.styleable.BdsButton_bds_text)

            val startIconResId = getResourceIdOrNull(R.styleable.BdsButton_bds_startDrawable)
            val endIconResId = getResourceIdOrNull(R.styleable.BdsButton_bds_endDrawable)

            binding.ivStartDrawable.isVisible = startIconResId != null
            binding.ivEndDrawable.isVisible = endIconResId != null

            binding.ivStartDrawable.setImageDrawable(startIconResId.toDrawable(context))
            binding.ivEndDrawable.setImageDrawable(endIconResId.toDrawable(context))
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.tvText.setTextAppearanceCompat(
                getResourceId(R.styleable.BdsView_bds_textAppearance, 0)
            )

            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
        }
    }
}
