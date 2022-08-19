package com.depromeet.bds.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentBackwardAppBarBinding

class BdsBackwardAppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: BdsComponentBackwardAppBarBinding

    var bdsTitle: String?
        get() = binding.tvTitle.text.toString()
        set(value) { binding.tvTitle.text = value }

    init {

        val layoutInflater = LayoutInflater.from(context)
        binding = BdsComponentBackwardAppBarBinding.inflate(layoutInflater, this, true)

        context.withStyledAttributes(attrs, R.styleable.BdsBackwardAppBar) {
            val title = getString(R.styleable.BdsBackwardAppBar_bds_title)
            val iconResId = getDrawable(R.styleable.BdsBackwardAppBar_bds_icon)
            val buttonTitle = getString(R.styleable.BdsBackwardAppBar_bds_button)
            val backwardResId = getDrawable(R.styleable.BdsBackwardAppBar_bds_backward_icon)
                ?: ContextCompat.getDrawable(context, R.drawable.ic_arrow_back)

            when {
                iconResId != null && buttonTitle.isNullOrBlank().not() ->
                    throw IllegalStateException("icon, button 동시 사용 불가")
            }

            bdsTitle = title

            binding.imageBackward.setImageDrawable(backwardResId)

            binding.imageIcon.isVisible = iconResId != null
            binding.imageIcon.setImageDrawable(iconResId)

            binding.button.isVisible = buttonTitle.isNullOrBlank().not()
            binding.button.text = buttonTitle

            binding.vEmptyMargin.isVisible =
                binding.imageIcon.isVisible.not() && binding.button.isVisible.not()
        }
    }

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setButtonText(button: String){
        binding.button.text = button
    }

    fun setOnBackwardClick(listener: View.OnClickListener) {
        binding.imageBackward.setOnClickListener(listener)
    }

    fun setOnIconClick(listener: View.OnClickListener) {
        binding.imageIcon.setOnClickListener(listener)
    }

    fun setOnButton(listener: View.OnClickListener) {
        binding.button.setOnClickListener(listener)
    }

    fun setTitleVisible(visibility : Int){
        binding.tvTitle.visibility =visibility
    }

    fun setImageIconInvisible(){
        binding.imageIcon.visibility=View.INVISIBLE
    }
}
