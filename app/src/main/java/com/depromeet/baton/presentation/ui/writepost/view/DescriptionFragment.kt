package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionFragment : BaseFragment<FragmentDescriptionBinding>(R.layout.fragment_description) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFocusListener()
        binding.writePostViewModel = writePostViewModel
    }

    //TODO TextField Bds로 변경
    private fun setFocusListener() {
        binding.etDescription.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.etDescription.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
            }
        }
        binding.etDescription.doAfterTextChanged {
            writePostViewModel.setCurrentTextLength(binding.etDescription.text.length)
        }
    }
}