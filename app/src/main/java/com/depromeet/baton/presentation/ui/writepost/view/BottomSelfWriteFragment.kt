package com.depromeet.baton.presentation.ui.writepost.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentBottomSelfWriteBinding
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Field


@AndroidEntryPoint
class BottomSelfWriteFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSelfWriteBinding? = null
    private val binding get() = _binding ?: error("View를 참조하기 위해 binding이 초기화되지 않았습니다.")
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_self_write, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setBackBtnClickListener()
        setCloseBtnClickListener()
        setCitySpinner()
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

    private fun setBackBtnClickListener() {
        binding.bdsAppbarSelfWrite.setOnBackwardClick {
            writePostViewModel.setSearchShopPosition(WritePostViewModel.SEARCH_SHOP)
        }
    }

    private fun setCloseBtnClickListener() {
        binding.bdsAppbarSelfWrite.setOnIconClick {
            writePostViewModel.setSearchShopPosition(WritePostViewModel.DIALOG_DISMISS)
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setCitySpinner() {
        val items = requireContext().resources.getStringArray(R.array.spinner_region)
        val spinnerAdapter = object : ArrayAdapter<String>(requireContext(), R.layout.item_region_spinner) {
            @SuppressLint("CutPasteId")
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val tv = super.getView(position, convertView, parent)

                if (position == count) {
                    (tv.findViewById<View>(R.id.tvItemSpinner) as TextView).text = ""
                    (tv.findViewById<View>(R.id.tvItemSpinner) as TextView).hint = getItem(count)
                }
                return tv
            }

            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }

        spinnerAdapter.addAll(items.toMutableList())
        spinnerAdapter.add("시/도")


        binding.spinnerSelfWriteCity.adapter = spinnerAdapter
        binding.spinnerSelfWriteCity.setSelection(spinnerAdapter.count)

        binding.spinnerSelfWriteCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> setRegionSpinner(R.array.spinner_region_seoul)
                    1 -> setRegionSpinner(R.array.spinner_region_busan)
                    2 -> setRegionSpinner(R.array.spinner_region_daegu)
                    3 -> setRegionSpinner(R.array.spinner_region_incheon)
                    4 -> setRegionSpinner(R.array.spinner_region_gwangju)
                    5 -> setRegionSpinner(R.array.spinner_region_daejeon)
                    6 -> setRegionSpinner(R.array.spinner_region_ulsan)
                    7 -> setRegionSpinner(R.array.spinner_region_sejong)
                    8 -> setRegionSpinner(R.array.spinner_region_gyeonggi)
                    9 -> setRegionSpinner(R.array.spinner_region_gangwon)
                    10 -> setRegionSpinner(R.array.spinner_region_chung_buk)
                    11 -> setRegionSpinner(R.array.spinner_region_chung_nam)
                    12 -> setRegionSpinner(R.array.spinner_region_jeon_buk)
                    13 -> setRegionSpinner(R.array.spinner_region_jeon_nam)
                    14 -> setRegionSpinner(R.array.spinner_region_gyeong_buk)
                    15 -> setRegionSpinner(R.array.spinner_region_gyeong_nam)
                    16 -> setRegionSpinner(R.array.spinner_region_jeju)
                    else -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun setRegionSpinner(array: Int) {
        binding.tvSelfWriteRegion.visibility = View.INVISIBLE
        val items = requireContext().resources.getStringArray(array)
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_region_spinner, items)
        binding.spinnerSelfWriteRegion.adapter = spinnerAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}