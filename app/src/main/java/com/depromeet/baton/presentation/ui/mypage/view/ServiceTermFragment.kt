package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentServiceTermBinding
import com.depromeet.baton.databinding.FragmentSoldoutTabBinding
import com.depromeet.baton.presentation.base.BaseFragment
import java.io.BufferedReader
import java.io.InputStreamReader

class ServiceTermFragment : BaseFragment<FragmentServiceTermBinding>(R.layout.fragment_service_term){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchServiceTerms()
    }

    private fun fetchServiceTerms() {
        resources.assets.open("service_terms.txt")
            .let(::InputStreamReader)
            .let(::BufferedReader)
            .use {
                val text = it.readText()
                binding.serviceTermContent.text = text
            }
    }
}