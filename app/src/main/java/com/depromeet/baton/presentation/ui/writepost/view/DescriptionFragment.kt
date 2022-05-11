package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment


class DescriptionFragment : BaseFragment<FragmentDescriptionBinding>(R.layout.fragment_description) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFocusListener()
    }

    private fun setFocusListener(){
        binding.etDescription.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.etDescription.setBackgroundResource(com.depromeet.bds.R.drawable.temp_textfield_chip_bacground)
            }
        }
    }
}