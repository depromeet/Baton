package com.depromeet.baton.presentation.sample

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.baton.databinding.ActivitySampleBinding
import com.depromeet.baton.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : BaseActivity() {

    private val binding: ActivitySampleBinding by lazy {
        ActivitySampleBinding.inflate(layoutInflater)
    }
    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
