package com.depromeet.bds.component

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentChoiceChipBinding
import com.depromeet.bds.utils.getAttributeColor
import com.depromeet.bds.utils.getAttributeStyle
import com.depromeet.bds.utils.setTextAppearanceCompat
import com.depromeet.bds.utils.toPx

class BdsChoiceChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    val binding: BdsComponentChoiceChipBinding
    private var isOn: Boolean = false
    private var shape: Int = 0
    private val isOutlined: Boolean
        get() = shape == 0
    private val isFilled: Boolean
        get() = shape == 1

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentChoiceChipBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsChoiceChip) {
            val isOn = getBoolean(R.styleable.BdsChoiceChip_isOn, isOn)
            val shape = getInt(R.styleable.BdsChoiceChip_shape, shape)
            setOnAndShape(isOn, shape)
        }

        context.withStyledAttributes(attrs, R.styleable.BdsTextView) {
            binding.tvText.text = getString(R.styleable.BdsTextView_bds_text)
            binding.tvText.setTextAppearanceCompat(
                getResourceId(R.styleable.BdsView_bds_textAppearance, 0)
            )
        }
    }

    fun setOnAndShape(isOn: Boolean = this.isOn, shape: Int = this.shape) {
        this.isOn = isOn
        this.shape = shape
        refresh()
    }

    private fun refresh() {
        // text color 설정
        setTextColor()
        // text appearance 설정
        setTextAppearance()
        // background 설정
        setBackground()
    }

    private fun setTextColor() {
        val isOff = !isOn
        val attrId = when {
            isOff && isOutlined -> R.attr.grey_scale70
            isOn && isOutlined -> R.attr.primary50
            isOff && isFilled -> R.attr.grey_scale70
            isOn && isFilled -> R.attr.primary50
            else -> throw IllegalStateException("불가능한 케이스")
        }
        binding.tvText.setTextColor(context.getAttributeColor(attrId))
    }

    private fun setTextAppearance() {
        val attrId = if (isOn) R.attr.body3 else R.attr.body4
        binding.tvText.setTextAppearance(context.getAttributeStyle(attrId))
    }

    private fun setBackground() {
        val backgroundColorAttrId = when {
            isOutlined -> R.attr.wh100
            isOn -> R.attr.primary5
            else -> R.attr.grey_scale30
        }
        val backgroundColor = context.getAttributeColor(backgroundColorAttrId)

        val isOff = !isOn
        val borderColorAttrId = when {
            isOff && isOutlined -> R.attr.grey_scale40
            isOn && isOutlined -> R.attr.primary40
            isOff && isFilled -> R.attr.grey_scale40
            isOn && isFilled -> R.attr.primary20
            else -> throw IllegalStateException("불가능한 케이스")
        }
        val borderColor = context.getAttributeColor(borderColorAttrId)

        val radius = 8.toPx().toFloat()

        val gradientDrawable = GradientDrawable()
        gradientDrawable.setStroke(1.toPx(), borderColor)
        gradientDrawable.setColor(backgroundColor)
        gradientDrawable.cornerRadius = radius
        binding.root.background = gradientDrawable
    }
}

