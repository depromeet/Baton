package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentToggleBinding

class BdsToggle  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentToggleBinding

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentToggleBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsToggle) {
            binding.switchButton.isChecked= getBoolean(R.styleable.BdsToggle_isChecked, false)
            binding.switchButton.isEnabled= getBoolean(R.styleable.BdsToggle_isEnabled, true)
        }
    }

    fun setOnClickListener(listener: () -> Unit) {
        binding.switchButton.setOnClickListener{ listener() }
    }

    fun getIsChecked() = binding.switchButton.isChecked

    fun setIsEnabled(_isEnabled: Boolean) {
        binding.switchButton.isEnabled=_isEnabled
    }
}
