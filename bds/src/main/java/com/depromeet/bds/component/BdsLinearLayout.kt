package com.depromeet.bds.component

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import com.depromeet.bds.R

class BdsLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            val radius = getDimension(R.styleable.BdsView_bdsRadius, 0f)
            val borderWidth = getDimension(R.styleable.BdsView_bdsBorderWidth, 0f)
            val borderColor = getColorStateList(R.styleable.BdsView_bdsBorderColor)
            val backgroundColor = getColorStateList(R.styleable.BdsView_bdsBackgroundColor)

            val gradientDrawable = GradientDrawable().apply {
                setStroke(borderWidth.toInt(), borderColor)
                color = backgroundColor
                cornerRadius = radius
            }

            this@BdsLinearLayout.background = gradientDrawable
        }
    }
}
