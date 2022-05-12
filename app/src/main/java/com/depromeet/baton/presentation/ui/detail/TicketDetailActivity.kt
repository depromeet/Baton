package com.depromeet.baton.presentation.ui.detail

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityTicketDetailBinding
import com.depromeet.baton.presentation.base.BaseActivity

class TicketDetailActivity :BaseActivity<ActivityTicketDetailBinding>(R.layout.activity_ticket_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    fun initView(){
        binding.ticketDetailRv.apply {
            val mLayoutManager = LinearLayoutManager(this@TicketDetailActivity)
            mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            this.layoutManager=mLayoutManager
        }

        binding.ticketDetailToolbar.ticketToolbarMenuIc.setOnClickListener {

        }
    }

}