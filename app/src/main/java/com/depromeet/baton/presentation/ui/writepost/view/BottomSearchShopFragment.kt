package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.core.view.isNotEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomSearchShopBinding
import com.depromeet.baton.presentation.base.BaseBottomDialogFragment
import com.depromeet.baton.presentation.ui.address.SearchShopRvAdapter
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.bds.component.BdsSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


@AndroidEntryPoint
class BottomSearchShopFragment : BaseBottomDialogFragment<FragmentBottomSearchShopBinding>(R.layout.fragment_bottom_search_shop) {

    private val writePostViewModel: WritePostViewModel by activityViewModels()
    private lateinit var searchShopRvAdapter: SearchShopRvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.writePostViewModel = writePostViewModel

        initView()
    }

    private fun initView() {
        setSearchShopRvAdapter()
        setInputField()
        setObserve()
    }

    private fun setObserve() {
        writePostViewModel.bottomSearchUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)

        //리사이클러뷰에서 선택한 경우
        writePostViewModel.isShopSelected.observe(this) {
            writePostViewModel.bottomSearchUiState.value.setBottomDialogDismiss.invoke()
        }
    }

    private fun setSearchShopRvAdapter() {
        searchShopRvAdapter = SearchShopRvAdapter(writePostViewModel)
        binding.rvBottomPlace.adapter = searchShopRvAdapter
        searchShopRvAdapter.setQueryListener(object : SearchShopRvAdapter.SearchColorListener {
            override fun getQuery(): String {
                return binding.bdsSearchbarBottomPlace.getText()
            }
        })
    }

    private fun setInputField() {
        writePostViewModel.searchPlace("")
        with(binding.bdsSearchbarBottomPlace) {
            textListener = object : BdsSearchBar.TextListener {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString()
                    if (query.isNotEmpty()) writePostViewModel.searchPlace(query)
                    if (query == "") writePostViewModel.searchPlace("")
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            KeyboardVisibilityEvent.setEventListener(requireActivity()) {
                searchBarKeyBoardListener(it)
            }

            setHint("헬스장 이름이나 도로명 주소 검색")

           lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    writePostViewModel.shopInfoList.collect { shopInfoList ->
                        if (isNotEmpty()) searchShopRvAdapter.submitList(shopInfoList)
                    }
                }
            }
        }
    }
}