package com.depromeet.baton.presentation.ui.home.view

import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityHowToUseBinding
import com.depromeet.baton.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HowToUseActivity : BaseActivity<ActivityHowToUseBinding>(R.layout.activity_how_to_use) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bdsAppbarHowToUse.setOnBackwardClick { onBackPressed() }
    }
}