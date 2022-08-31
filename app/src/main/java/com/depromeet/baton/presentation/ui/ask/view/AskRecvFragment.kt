package com.depromeet.baton.presentation.ui.ask.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentAskRecvTabBinding
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.ask.viewModel.AskViewModel
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class AskRecvFragment : BaseFragment<FragmentAskRecvTabBinding>(R.layout.fragment_ask_recv_tab) {
    private val askViewModel by viewModels<AskViewModel>()
    private val messageAdapter : MessageitemAdapter by lazy{
        MessageitemAdapter( ::onClickMessage )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.messageRecvRv.apply{
            adapter = messageAdapter
            layoutManager = mLayoutManager
        }
        askViewModel.getRecvMsgList()
        setObserver()
    }

    private fun setObserver() {
        askViewModel.recvUiState
            .flowWithLifecycle(viewLifecycle)
            .onEach {
                binding.uistate = it
                messageAdapter.submitList(it.recvList)
            }
            .launchIn(viewLifecycleScope)
    }

    private fun onClickMessage(message : Message){
        Timber.e(message.id.toString())
        MsgRcvActivity.start(requireContext() , message.id!!)
    }
}