package com.depromeet.baton.presentation.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityInquiryBinding
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity

class InquiryActivity : BaseActivity<ActivityInquiryBinding>(R.layout.activity_inquiry) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.ticketDetailToolbar.setImageIconInvisible()
    }

}