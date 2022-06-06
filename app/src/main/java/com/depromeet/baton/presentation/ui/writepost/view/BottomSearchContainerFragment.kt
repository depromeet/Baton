package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomSearchContainerBinding
import com.depromeet.baton.presentation.base.BaseBottomDialogFragment
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class BottomSearchContainerFragment : BaseBottomDialogFragment<FragmentBottomSearchContainerBinding>(R.layout.fragment_bottom_search_container) {

    private val bottomSearchShopFragment = BottomSearchShopFragment()
    private val bottomSelfWriteFragment = BottomSelfWriteFragment()
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setObserve()
        writePostViewModel.bottomSearchUiState.value.onGoSearchShopClick.invoke()
    }

    private fun setObserve() {
        writePostViewModel.bottomSearchViewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<WritePostViewModel.BottomSearchViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                WritePostViewModel.BottomSearchViewEvent.ToSearchShop -> {
                    if (!childFragmentManager.fragments.contains(bottomSearchShopFragment)) {
                        childFragmentManager.beginTransaction()
                            .add(R.id.fcv_bottom_search, bottomSearchShopFragment)
                            .commit()
                    }

                    childFragmentManager.beginTransaction()
                        .show(bottomSearchShopFragment)
                        .commit()

                    childFragmentManager.beginTransaction()
                        .hide(bottomSelfWriteFragment)
                        .commit()
                }

                WritePostViewModel.BottomSearchViewEvent.ToSelfWrite -> {
                    if (!childFragmentManager.fragments.contains(bottomSelfWriteFragment)) {
                        childFragmentManager.beginTransaction()
                            .add(R.id.fcv_bottom_search, bottomSelfWriteFragment)
                            .commit()
                    }
                    childFragmentManager.beginTransaction()
                        .show(bottomSelfWriteFragment)
                        .commit()

                    childFragmentManager.beginTransaction()
                        .hide(bottomSearchShopFragment)
                        .commit()
                }
                WritePostViewModel.BottomSearchViewEvent.SelfWriteDone -> {
                    dialog?.dismiss()
                }
            }
            writePostViewModel.bottomSearchConsumeViewEvent(viewEvent)
        }
    }
}



