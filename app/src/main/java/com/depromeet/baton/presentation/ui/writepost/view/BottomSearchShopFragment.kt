package com.depromeet.baton.presentation.ui.writepost.view

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isNotEmpty
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.writePostViewModel = writePostViewModel
        setSearchShopRvAdapter()
        setShopSelectedObserve()
        setCloseBtnOnClickListener()
        setInputField()
        goToSelfWriteFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), R.style.BottomSheetDialog).apply {
            setOnShowListener { setupFullHeight(it as BottomSheetDialog) }
        }
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setSearchShopRvAdapter() {
        searchShopRvAdapter = SearchShopRvAdapter(writePostViewModel)
        binding.rvBottomPlace.adapter = searchShopRvAdapter
    }

    private fun setCloseBtnOnClickListener() {
        binding.ivBottomSearchClose.setOnClickListener {
            writePostViewModel.setSearchShopPosition(WritePostViewModel.DIALOG_DISMISS)
        }
    }

    private fun setShopSelectedObserve() {
        writePostViewModel.isShopSelected.observe(this) {
            writePostViewModel.setSearchShopPosition(WritePostViewModel.DIALOG_DISMISS)
        }
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

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    writePostViewModel.shopInfoList.collect { shopInfoList ->
                        if (isNotEmpty()) searchShopRvAdapter.submitList(shopInfoList)
                    }
                }
            }
        }
    }

    private fun goToSelfWriteFragment() {
        with(binding) {
            bdsBtnBottomSearchNoResult.setOnClickListener {
                writePostViewModel?.setSearchShopPosition(WritePostViewModel.SELF_WRITE)
            }
            bdsBtnBottomSearchSelfWrite.setOnClickListener {
                writePostViewModel?.setSearchShopPosition(WritePostViewModel.SELF_WRITE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        writePostViewModel.shopInfoList.value.clear()
        _binding = null
    }
<<<<<<< HEAD
}

=======
}
>>>>>>> dabin/home-filter
