package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentTextFieldBinding

// 자식으로 EditText 를 받고 거기에 위임하자.
// EditText 관련 안드로이드 서포트 동작을 전부 오버라이드 하는 것은 미친 짓임.
class BdsComponentTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentTextFieldBinding
    private var innerEditText: EditText? = null

    var label: String? = null
        private set(value) {
            if (field == value) return

            binding.txtLabel.isVisible = !value.isNullOrBlank()
            binding.txtLabel.text = value

            field = value
        }

    var helperMessage: String? = null

    val isError: Boolean
        get() = !binding.txtError.text.isNullOrBlank()

    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentTextFieldBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(
            attrs,
            R.styleable.BdsComponentTextField,
            defStyleAttr,
            defStyleRes
        ) {
            getString(R.styleable.BdsComponentTextField_bds_label)
                .also { label = it }

            val error = getString(R.styleable.BdsComponentTextField_bds_error_message)
            val success = getString(R.styleable.BdsComponentTextField_bds_success_message)
            helperMessage = getString(R.styleable.BdsComponentTextField_bds_helper_message)
            updateDescriptionText(error, success, helperMessage)

            getColorStateList(R.styleable.BdsComponentTextField_bds_helper_textColor)
                .also { colorStateList ->
                    colorStateList?.let { binding.txtHelper.setTextColor(it) }
                }
        }

    }

    fun setSuccess(message: String?) {
        updateDescriptionText(success = message)
    }

    fun setError(message: String?) {
        updateDescriptionText(error = message)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        innerEditText = requireNotNull(
            value = findInnerEditText(this),
            lazyMessage = { "EditText 를 자식으로 제공해야합니다." }
        )
        placeInnerEditText()
    }

    private fun placeInnerEditText() {
        (innerEditText?.parent as? ViewGroup)?.let {
            it.removeView(innerEditText)
            binding.containerInnerText.addView(
                innerEditText,
                0,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                    weight = 1f
                }
            )
        }
    }

    private fun updateDescriptionText(
        error: String? = null,
        success: String? = null,
        helper: String? = helperMessage
    ) {
        fun TextView.clear() {
            this.isVisible = false
            this.text = null
        }

        binding.txtError.clear()
        binding.txtSuccess.clear()
        binding.txtHelper.clear()

        when {
            !error.isNullOrBlank() -> {
                binding.txtError.isVisible = true
                binding.txtError.text = error
            }
            !success.isNullOrBlank() -> {
                binding.txtSuccess.isVisible = true
                binding.txtSuccess.text = success
            }
            !helper.isNullOrBlank() -> {
                binding.txtHelper.isVisible = true
                binding.txtHelper.text = helper
            }
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isError) {
            mergeDrawableStates(drawableState, ERROR_STATE_SET)
        }
        return drawableState
    }

    companion object {
        private val ERROR_STATE_SET = intArrayOf(R.attr.state_error)

        private fun findInnerEditText(viewGroup: ViewGroup?): EditText? {
            if (viewGroup == null) return null

            for (child in viewGroup.children) {
                if (child is EditText) return child
                else if (child is ViewGroup) {
                    findInnerEditText(child)
                        ?.let { return it }
                }
            }
            return null
        }
    }
}
