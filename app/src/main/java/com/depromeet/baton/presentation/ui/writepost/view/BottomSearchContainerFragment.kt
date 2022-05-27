package com.depromeet.baton.presentation.ui.writepost.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomSearchContainerBinding
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.presentation.util.Event
import com.depromeet.baton.presentation.util.shortToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSearchContainerFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSearchContainerBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")
    private val bottomSearchShopFragment = BottomSearchShopFragment()
    private val bottomSelfWriteFragment = BottomSelfWriteFragment()
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_search_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        changeFragment()
        writePostViewModel.setSearchShopPosition(WritePostViewModel.SEARCH_SHOP)
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

    private fun changeFragment() {

        writePostViewModel.viewEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled().let { event ->
                when (event) {
                    WritePostViewModel.SEARCH_SHOP -> {
                        childFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fcv_bottom_search, bottomSearchShopFragment, "friendMainCard")
                            .commit()
                    }

                    WritePostViewModel.SELF_WRITE -> {
                        childFragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fcv_bottom_search, bottomSelfWriteFragment, "friendMainCard")
                            .commit()
                    }
                    WritePostViewModel.DIALOG_DISMISS -> dialog?.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



