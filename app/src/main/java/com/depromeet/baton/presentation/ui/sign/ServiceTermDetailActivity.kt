package com.depromeet.baton.presentation.ui.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityServiceTermDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity
import java.io.BufferedReader
import java.io.InputStreamReader

class ServiceTermDetailActivity :
    BaseActivity<ActivityServiceTermDetailBinding>(R.layout.activity_service_term_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchServiceTerms()

        binding.appbar.setOnBackwardClick { onBackPressed() }
    }

    private fun fetchServiceTerms() {
        resources.assets.open("service_terms.txt")
            .let(::InputStreamReader)
            .let(::BufferedReader)
            .use {
                val text = it.readText()
                binding.txtContent.text = text
            }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ServiceTermDetailActivity::class.java))
        }
    }
}
