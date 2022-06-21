package com.depromeet.baton.presentation.ui.writepost.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPlaceRegisterBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.writepost.adapter.PhotoRvAdapter
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.presentation.util.MultiPartResolver
import com.depromeet.baton.presentation.util.shortToast
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceRegisterFragment : BaseFragment<FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()
    private lateinit var photoRvAdapter: PhotoRvAdapter
    private lateinit var multiPartResolver: MultiPartResolver

    override fun onResume() {
        super.onResume()
        writePostViewModel.setNextLevelEnable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment = this
        binding.writePostViewModel = writePostViewModel

        setInitLayout()
        selectedShopObserve()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setPhotoRvAdapter()
        setSelectedPhotoObserve()
        multiPartResolver = MultiPartResolver(requireContext())
    }

    //SearchBar 초기 레이아웃 상태 세팅
    private fun setInitLayout() {
        with(binding.includeBdsSearchbarOne.searchBarEt) {
            text = Editable.Factory.getInstance().newEditable("헬스장 이름이나 도로명 주소를 검색해주세요")
            isFocusable = false
            setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
            binding.includeBdsSearchbarOne.searchBarCancelIc.visibility = View.GONE
        }
        with(binding.includeBdsSearchbarTwo.searchBarEt) {
            isFocusable = false
            setTextColor(ContextCompat.getColor(requireContext(), com.depromeet.bds.R.color.gy60))
            binding.includeBdsSearchbarTwo.searchBarCancelIc.visibility = View.GONE
        }
    }

    //장소등록->바텀싯
    private fun setPlaceRegisterClickListener() {
        binding.includeBdsSearchbarOne.searchBarEt.setOnClickListener {
            val bottomSearchContainerFragment = BottomSearchContainerFragment()
            bottomSearchContainerFragment.show(
                childFragmentManager,
                bottomSearchContainerFragment.tag
            )
        }
    }

    private fun selectedShopObserve() {
        writePostViewModel.selectedShopInfo.observe(viewLifecycleOwner) { selectedShopInfo ->
            binding.tvPlaceRegister.text = "장소명"
            binding.tvPlaceRegisterAddress.visibility = View.VISIBLE

            with(binding.includeBdsSearchbarOne) {
                searchBarSearchIc.visibility = View.GONE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopName)
                searchBarEt.isEnabled = false
            }
            with(binding.includeBdsSearchbarTwo) {
                ctlSearchBarContainer.visibility = View.VISIBLE
                searchBarSearchIc.visibility = View.GONE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopAddress)
                searchBarEt.isEnabled = false
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
            statusBarColor = "#FFFFFF",
            isLightStatusBar = true,
            toolbarColor = "#FFFFFF",
            toolbarTextColor = "#25272B",
            toolbarIconColor = "#25272B",
            backgroundColor = "#FFFFFF",
            selectedIndicatorColor = "#0066FF",
            isFolderMode = false,
            isMultipleMode = true,
            doneTitle = "확인",
            limitMessage = "5개까지 선택할 수 있어요.",
            isShowNumberIndicator = true,
            maxSize = 5,
        )
        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            writePostViewModel.setSelectedPhotoUriList(images.map { it.uri }.toMutableList())
            writePostViewModel.setSelectedPhotoMultiPartList(images.map { multiPartResolver.createImgMultiPart(it.uri) }.toMutableList())
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
        photoRvAdapter = PhotoRvAdapter(writePostViewModel, requireContext())
        binding.rvPlaceRegister.adapter = photoRvAdapter
    }

    private fun setSelectedPhotoObserve() {
        writePostViewModel.selectedPhotoList.observe(viewLifecycleOwner) {
            photoRvAdapter.submitList(it)
            photoRvAdapter.notifyDataSetChanged()
        }
    }
}
