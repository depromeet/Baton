package com.depromeet.baton.presentation.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivitySampleBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.sample.SampleViewModel.ViewEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SampleActivity : BaseActivity<ActivitySampleBinding>(R.layout.activity_sample) {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        viewModel.viewEvent.observe(this) { event ->
            when (event) {
                is ViewEvent.ShowToast -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
