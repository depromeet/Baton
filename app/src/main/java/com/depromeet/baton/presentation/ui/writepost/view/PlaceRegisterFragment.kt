package com.depromeet.baton.presentation.ui.writepost.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPlaceRegisterBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.adapter.PhotoRvAdapter
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.presentation.util.shortToast
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceRegisterFragment : BaseFragment<FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()
    private lateinit var photoRvAdapter: PhotoRvAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment = this
        binding.writePostViewModel = writePostViewModel

        setInitLayout()
        selectedShopObserve()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setPhotoRvAdapter()
        setHashTagClickListener()
        setSelectedPhotoObserve()
    }

    //SearchBar 초기 레이아웃 상태 세팅
    private fun setInitLayout() {
   /*     with(binding.includeChip) {
            tvHashtagSelectExplain.text = "(선택사항)"
            tvHashtagSelectExplain.setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
            tvHashtagSelect.setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
        }
*/
        with(binding.includeBdsSearchbarOne) {
            searchBarEt.text = Editable.Factory.getInstance().newEditable("헬스장 이름이나 도로명 주소를 검색해주세요")
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
            searchBarCancelIc.visibility = View.GONE
        }
        with(binding.includeBdsSearchbarTwo) {
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
            searchBarCancelIc.visibility = View.GONE
        }
    }

    //장소등록->바텀싯
    private fun setPlaceRegisterClickListener() {
        binding.includeBdsSearchbarOne.searchBarEt.setOnClickListener {
            val bottomFilterFragment = BottomSearchShopFragment()
            bottomFilterFragment.show(
                childFragmentManager,
                bottomFilterFragment.tag
            )
        }
    }

    //TODO textField Bds 적용
    private fun selectedShopObserve() {
        writePostViewModel.selectedShopInfo.observe(viewLifecycleOwner) { selectedShopInfo ->
            binding.tvPlaceRegister.text = "이름"
            binding.tvPlaceRegisterAddress.visibility = View.VISIBLE

            with(binding.includeBdsSearchbarOne) {
                searchBarSearchIc.visibility = View.GONE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopName)
                searchBarEt.isEnabled = false
            }
            with(binding.includeBdsSearchbarTwo) {
                ctlSearchBarContainer.visibility = View.VISIBLE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarSearchIc.visibility = View.GONE
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopAddress)
            }
        }
    }

    //TODO 해시태그 선택 bindingAdapter로 구현 + ViewModel로 옮기기
    private fun setHashTagClickListener() {
        with(binding) {
            bdschoiceHashtagKindTeacher.setOnClickListener {
                bdschoiceHashtagKindTeacher.setOnAndShape(true, 0)
            }
            bdschoiceHashtagSystematicLesson.setOnClickListener {
                bdschoiceHashtagSystematicLesson.setOnAndShape(true, 0)
            }
            bdschoiceHashtagCustomizedCare.setOnClickListener {
                bdschoiceHashtagCustomizedCare.setOnAndShape(true, 0)
            }
            bdschoiceHashtagVariousInstruments.setOnClickListener {
                bdschoiceHashtagVariousInstruments.setOnAndShape(true, 0)
            }
            bdschoiceHashtagQuietAtmosphere.setOnClickListener {
                bdschoiceHashtagQuietAtmosphere.setOnAndShape(true, 0)
            }
            bdschoiceHashtagWideFacility.setOnClickListener {
                bdschoiceHashtagWideFacility.setOnAndShape(true, 0)
            }
            bdschoiceHashtagPleasantEnvironment.setOnClickListener {
                bdschoiceHashtagPleasantEnvironment.setOnAndShape(true, 0)
            }
        }
    }

    private fun setPictureSelectClickListener() {
        binding.ibtnPlaceRegister.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        val galleryPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        if (galleryPermission == PackageManager.PERMISSION_GRANTED) startProcess()
        else requestPermission()
    }

    private fun startProcess() {
        val config = ImagePickerConfig(
            statusBarColor = "#ffffff",
            isLightStatusBar = true,
            toolbarColor = "#ffffff",  //툴바칼라
            toolbarTextColor = "#25272B", //툴바 텍스트 칼라
            toolbarIconColor = "#25272B",
            backgroundColor = "#ffffff", //배경칼라
            selectedIndicatorColor = "#0066FF", //선택된 인디케이터 칼라
            isFolderMode = false, //폴더로 보일꺼냐
            isMultipleMode = true,
            doneTitle = "확인",
            isShowNumberIndicator = true,
            maxSize = 5,
        )
        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            writePostViewModel.setSelectedPhotoList(images.map { it.uri }.toMutableList())
        }
    }

    private fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            when (isGranted) {
                true -> startProcess()
                false -> requireContext().shortToast("갤러리 권한을 허용해주세요.")
            }
        }

    private fun setPhotoRvAdapter() {
        photoRvAdapter = PhotoRvAdapter(requireContext())
        binding.rvPlaceRegister.adapter = photoRvAdapter
    }

    //TODO 사진 재 선택시 로직
    private fun setSelectedPhotoObserve() {
        writePostViewModel.selectedPhotoList.observe(viewLifecycleOwner) {
            photoRvAdapter.submitList(it)
        }
    }
}
