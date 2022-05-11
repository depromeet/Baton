package com.depromeet.baton.presentation.ui.writepost.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentDescriptionBinding
import com.depromeet.baton.databinding.FragmentTransactionMethodBinding
import com.depromeet.baton.presentation.base.BaseFragment

class TransactionMethodRegisterFragment : BaseFragment<FragmentTransactionMethodBinding>(R.layout.fragment_transaction_method) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.transactionMethodRegisterFragment = this
        setOnClickListener()
    }

    //TODO Choice Chip
    private fun setOnClickListener() {
        with(binding) {
            btnTransactionMethodFace.setOnClickListener {
                btnTransactionMethodFace.setBackgroundResource(com.depromeet.bds.R.drawable.temp_choice_chip_background_selected)
                btnTransactionMethodFace.setTextColor(Color.parseColor("#0066FF"))
            }
            btnTransactionMethodNonFace.setOnClickListener {
                btnTransactionMethodNonFace.setBackgroundResource(com.depromeet.bds.R.drawable.temp_choice_chip_background_selected)
                btnTransactionMethodNonFace.setTextColor(Color.parseColor("#0066FF"))
            }
        }
    }
}