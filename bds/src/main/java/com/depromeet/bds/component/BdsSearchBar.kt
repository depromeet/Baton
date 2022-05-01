package com.depromeet.bds.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.withStyledAttributes
import androidx.core.view.isVisible
import com.depromeet.bds.R
import com.depromeet.bds.databinding.BdsComponentSearchbarBinding


class BdsSearchBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes){

    private val layoutInflater = LayoutInflater.from(context)
    private val binding = BdsComponentSearchbarBinding.inflate(layoutInflater, this, true)
    var textListener : TextListener? =null
    var keyboardListener : KeyBoardListener? =null
    private var treeObserver : ViewTreeObserver.OnGlobalLayoutListener? =null


    interface KeyBoardListener{
        fun keyboardStatusListener() : Int
    }

    interface TextListener{
        fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        fun afterTextChanged(s: Editable?)
    }

    init {

        context.withStyledAttributes(attrs, R.styleable.BdsSearchBar) {
            binding.searchBarEt.setText(getString(R.styleable.BdsSearchBar_bds_text))
            binding.searchBarEt.hint = getString(R.styleable.BdsSearchBar_bds_placeholder_text)
            binding.searchBarCancelIc.isVisible = getBoolean(R.styleable.BdsSearchBar_isSelected, false)
        }

        context.withStyledAttributes(attrs, R.styleable.BdsView) {
            binding.searchBarEt.setTextAppearanceCompat(
                getResourceId(R.styleable.BdsView_bds_textAppearance, 0)
            )
            binding.root.isEnabled = getBoolean(R.styleable.BdsView_isEnabled, true)
            binding.root.isSelected = getBoolean(R.styleable.BdsView_isSelected, false)

        }

        initEvents()
    }

    private fun initEvents(){
        setKeyboardListener()
        binding.searchBarEt.addTextChangedListener( BdsTextWatcher() ) //textwatcher 추가
        searchBarStatusListener()
    }

    private fun searchBarStatusListener(){
        binding.searchBarEt.setOnFocusChangeListener { v, hasFocus ->
            when(hasFocus){
                true ->{
                    binding.root.isSelected = true
                    binding.searchBarCancelIc.visibility=View.VISIBLE
                }
                else->{
                    binding.searchBarEt.clearFocus()
                    binding.root.isSelected = false
                    binding.searchBarCancelIc.visibility=View.GONE
                }
            }
        }

        binding.searchBarCancelIc.setOnClickListener {
            binding.searchBarEt.setText("")
            binding.root.isEnabled=true
        }
    }


    private inner class BdsTextWatcher() : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            textListener?.beforeTextChanged(s,start,count,after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textListener?.onTextChanged(s,start,before,count)
        }

        override fun afterTextChanged(s: Editable?) {
            textListener?.afterTextChanged(s)
        }
    }



    private fun setKeyboardListener(){
        treeObserver = ViewTreeObserver.OnGlobalLayoutListener {
            when( keyboardListener?.keyboardStatusListener() ){
                KEYBOARD_SHOW -> {
                    if(binding.searchBarEt.hasFocus()) {
                        binding.root.isSelected = true
                        binding.searchBarCancelIc.visibility=View.VISIBLE
                    }
                }
                KEYBOARD_HIDE -> {
                    binding.root.isSelected = false
                    binding.searchBarCancelIc.visibility=View.GONE
                }
            }
        }

        binding.searchBarEt.viewTreeObserver.addOnGlobalLayoutListener(treeObserver)

    }

    fun removeKeyboardListener(){
       binding.searchBarEt.viewTreeObserver.removeOnGlobalLayoutListener(treeObserver)
    }

    companion object{
        val KEYBOARD_SHOW =1
        val KEYBOARD_HIDE =2
    }
}
