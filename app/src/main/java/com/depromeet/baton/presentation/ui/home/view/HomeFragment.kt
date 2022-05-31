package com.depromeet.baton.presentation.ui.home.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.GridLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentHomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.writepost.view.WritePostActivity
import com.depromeet.baton.presentation.ui.home.adapter.TicketItemRvAdapter
import com.depromeet.baton.presentation.util.TicketItemVerticalDecoration
import com.depromeet.baton.util.getAddress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFloatingActionBtnClickListener()
        setTicketItemRvAdapter()
        setLocationClickListener()
    }

    override fun onResume() {
        super.onResume()
        binding.tvHomeLocation.text = if (getAddress().roadAddress != "") getAddress().roadAddress.slice(0..5) + "..."
        else "위치 설정"
    }

    private fun setFloatingActionBtnClickListener() {
        binding.fabHome.setOnClickListener {
            @SuppressLint("ResourceType")
            val dialog = Dialog(requireContext())
            dialog.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_write_post_temp_save)
                setCancelable(true)
                window!!.setGravity(Gravity.CENTER)
                show()
            }

            val originBtn = dialog.findViewById<Button>(R.id.btn_write_post_dialog_origin)
            val newBtn = dialog.findViewById<TextView>(R.id.btn_write_post_dialog_new)

            originBtn.setOnClickListener {
                startActivity(Intent(requireContext(), WritePostActivity::class.java))
                dialog.dismiss()
            }
            newBtn.setOnClickListener {
                startActivity(Intent(requireContext(), WritePostActivity::class.java))
                dialog.dismiss()
            }
        }
    }

    private fun setTicketItemRvAdapter() {
        with(binding) {
            val ticketItemRvAdapter =
                TicketItemRvAdapter(TicketItemRvAdapter.SCROLL_TYPE_VERTICAL, requireContext(), ::setTicketItemClickListener)
            val gridLayoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

            adapter = ticketItemRvAdapter
            itemDecoration = TicketItemVerticalDecoration()
            rvHome.layoutManager = gridLayoutManager

            ticketItemRvAdapter.submitList(
                arrayListOf(
                    TicketItem(
                        "휴메이크 휘트니스 석촌점", "헬스", "123,000원", "50일 남음", "광진구 중곡동", "12m", R.drawable.dummy1
                    ),
                    TicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy2),
                    TicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy4),
                    TicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy5),
                    TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy7),
                )
            )
        }
    }

    private fun setTicketItemClickListener(ticketItem: TicketItem) {
        startActivity(Intent(requireContext(), TicketDetailActivity::class.java).apply {
            //TODO 게시글 id넘기기
        })
    }

    private fun setLocationClickListener() {
        binding.ctlHomeLocation.setOnClickListener {
            val intent = Intent(requireContext(), AddressActivity::class.java)
            startActivity(intent)
        }
    }
}