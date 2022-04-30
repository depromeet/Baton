package com.depromeet.bds.component

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentSearchbarBinding

class BdsSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val layoutInflater = LayoutInflater.from(context)
    private val binding = BdsComponentSearchbarBinding.inflate(layoutInflater, this, true)

    init {

        context.withStyledAttributes(attrs, R.styleable.BdsSearchBar) {
            binding.searchBarEt.setText(getString(R.styleable.BdsSearchBar_bds_text))
            binding.searchBarEt.hint = getString(R.styleable.BdsSearchBar_bds_placeholder_text)
            val endIconResId = getResourceIdOrNull(R.styleable.BdsSearchBar_bds_endDrawable)

            binding.searchBarCancelIc.isVisible = endIconResId!= null
            binding.searchBarCancelIc.setImageDrawable(endIconResId.toDrawable(context))
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.searchBarEt.setTextAppearanceCompat(
                getResourceId(R.styleable.BdsView_bds_textAppearance, 0)
            )

            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isFilled, false)

        }

    }

}
