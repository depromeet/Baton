package com.depromeet.baton.presentation.ui.writepost

import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWritePostBinding
import com.depromeet.baton.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WritePostActivity : BaseActivity<ActivityWritePostBinding>(R.layout.activity_write_post) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_post)
    }
}