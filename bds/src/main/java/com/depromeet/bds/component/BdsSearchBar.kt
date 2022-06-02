package com.depromeet.bds.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
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
    var textListener: TextListener? = null

    interface TextListener {
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?)
    }

    open class DefaultTextListener : TextListener {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    init {

        context.withStyledAttributes(attrs, R.styleable.BdsSearchBar) {
            binding.searchBarEt.setText(getString(R.styleable.BdsSearchBar_bds_text))
            binding.searchBarEt.hint = getString(R.styleable.BdsSearchBar_bds_placeholder_text)
            binding.searchBarCancelIc.isVisible = getBoolean(R.styleable.BdsSearchBar_isSelected, false)
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {

            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)

        }
        context.withStyledAttributes(attrs, R.styleable.BdsTextView) {
            binding.searchBarEt.setTextAppearance(getResourceId(R.styleable.BdsView_bds_textAppearance, 0))
        }

        initEvents()

    }

    private fun initEvents() {
        binding.searchBarEt.addTextChangedListener(BdsTextWatcher()) //textwatcher 추가
        searchBarStatusListener()
    }

    private fun searchBarStatusListener() {
        binding.searchBarEt.setOnFocusChangeListener { v, hasFocus ->
            when (hasFocus) {
                true -> {
                    setSearchBarSelected()
                }
                else -> {
                    binding.searchBarEt.clearFocus()
                    binding.root.isSelected = false
                    binding.searchBarCancelIc.visibility = View.GONE
                }
            }
        }

        binding.searchBarCancelIc.setOnClickListener {
            binding.searchBarEt.setText("")
            binding.root.isEnabled = true
        }

    }


    private inner class BdsTextWatcher() : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            textListener?.beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textListener?.onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            textListener?.afterTextChanged(s)
        }
    }


    fun setSearchBarSelected() {
        if (binding.searchBarEt.hasFocus()) {
            binding.root.isSelected = true
            binding.searchBarCancelIc.visibility = View.VISIBLE
        }
    }

    fun setSearchBarEnabled() {
        if (binding.searchBarEt.hasFocus()) {
            binding.root.isSelected = false
            binding.searchBarCancelIc.visibility = View.GONE
        }
    }

    fun searchBarKeyBoardListener(isSelected: Boolean) {
        if (isSelected) setSearchBarSelected()
        else setSearchBarEnabled()
    }

    fun getText(): String {
        return binding.searchBarEt.text.toString()
    }

    fun setImeListener(listener: (EditText) -> Unit) {
        binding.searchBarEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || event?.action == KeyEvent.ACTION_DOWN || event?.action == KeyEvent.KEYCODE_ENTER
            ) {
                listener(binding.searchBarEt)
            }
            true
        }
    }

    fun setEditText(text: String) {
        binding.searchBarEt.setText(text)
    }
}
