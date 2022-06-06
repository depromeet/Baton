package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentCheckboxBinding

class BdsCheckbox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentCheckboxBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentCheckboxBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsToggle) {
            binding.checkbox.isChecked = getBoolean(R.styleable.BdsCheckbox_isChecked, false)
            binding.checkbox.isEnabled = getBoolean(R.styleable.BdsCheckbox_isEnabled, true)
        }
    }

    fun setOnClickListener(listener: () -> Unit) {
        binding.checkbox.setOnClickListener { listener() }
    }

    fun getIsChecked() = binding.checkbox.isChecked

    fun setIsEnabled(isEnabled: Boolean) {
        binding.checkbox.isEnabled = isEnabled
    }
}
