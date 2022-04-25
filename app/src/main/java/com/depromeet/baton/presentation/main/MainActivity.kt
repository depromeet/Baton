package com.depromeet.baton.presentation.main

import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityMainBinding
import com.depromeet.baton.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}