package com.depromeet.baton.presentation.ui.writepost.view

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomSearchShopBinding
import com.depromeet.baton.presentation.ui.address.SearchShopRvAdapter
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.bds.component.BdsSearchBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class BottomSearchShopFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSearchShopBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")
    private val writePostViewModel: WritePostViewModel by activityViewModels()
    private lateinit var searchShopRvAdapter: SearchShopRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_search_shop, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchShopRvAdapter()
        setInputField()
        setShopSelectedObserve()
        setCloseBtnOnClickListener()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
    }

    private fun setSearchShopRvAdapter() {
        searchShopRvAdapter = SearchShopRvAdapter(writePostViewModel)
        binding.rvBottomPlace.adapter = searchShopRvAdapter
    }

    private fun setCloseBtnOnClickListener() {
        binding.ivBottomSearchClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    //다이어로그
    private fun setShopSelectedObserve() {
        writePostViewModel.isShopSelected.observe(this) {
            dialog?.dismiss()
        }
    }

    //검색해서 뷰모델한테 넘기기
    private fun setInputField() {
        with(binding.bdsSearchbarBottomPlace) {
            textListener = object : BdsSearchBar.TextListener {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val query = s.toString()
                    if (query.isNotEmpty()) {
                        writePostViewModel.searchPlace(query)
                        clearFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            }

            //TODO 검색결과 데이터 collect
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    writePostViewModel.shopInfoList.collect {
                        if (it.isNotEmpty()) {
                            searchShopRvAdapter.submitList(it)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


