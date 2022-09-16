package com.depromeet.baton.presentation.ui.home.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityLibraryBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.home.adapter.AdapterItem
import com.depromeet.baton.presentation.ui.home.adapter.StickyHeaderRecyclerViewAdapter
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LibraryActivity : BaseActivity<ActivityLibraryBinding>(R.layout.activity_library) {
    private lateinit var recyclerView2Adapter: TicketItemRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.sv.run {
            header = binding.ctl
        }
        with(binding.rv) {
            recyclerView2Adapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, ::setTicketItemClickListener)
            val gridLayoutManager = GridLayoutManager(this@LibraryActivity, 2, GridLayoutManager.VERTICAL, false)
            adapter = recyclerView2Adapter

            layoutManager = gridLayoutManager
        }



        recyclerView2Adapter.submitList(events)
    }
    val events: List<FilteredTicket>
        get() {
            val result = mutableListOf<FilteredTicket>()
            for (i in 0 until 50) {
                result.add(
                    FilteredTicket(
                        i, "서울시 영등포구 당산동", "서울시 영등포구 당산동", price = "13000", null,
                        null, null, "30", latitude = 3434.33, longitude = 2424.5, null, null
                    )
                )
            }
            return result
        }
    private fun setTicketItemClickListener(ticketItem: FilteredTicket) {
        TicketDetailActivity.start(this, ticketItem.id)
    }
}