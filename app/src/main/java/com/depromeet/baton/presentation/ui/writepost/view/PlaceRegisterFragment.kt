package com.depromeet.baton.presentation.ui.writepost.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentPlaceRegisterBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.address.SearchShopRvAdapter
import com.depromeet.baton.presentation.ui.writepost.adapter.PhotoRvAdapter
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.camera.CameraModule
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlaceRegisterFragment : BaseFragment<com.depromeet.baton.databinding.FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()
    private lateinit var photoRvAdapter: PhotoRvAdapter

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment=this
        binding.writePostViewModel=writePostViewModel
        setSearchBar()
        selectedShopObserve()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setPhotoRvAdapter()
        setHashTagClickListener()
        setSelectedPhtoObserve()
    }

    //SearchBar 초기 상태 세팅
    private fun setSearchBar() {
        with(binding.includeBdsSearchbarOne) {
            searchBarEt.text = Editable.Factory.getInstance().newEditable("헬스장 이름이나 도로명 주소를 검색해주세요")
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(Color.GRAY)
            searchBarCancelIc.visibility = View.GONE
        }
        with(binding.includeBdsSearchbarTwo) {
            searchBarEt.isFocusable = false
            searchBarEt.setTextColor(Color.GRAY)
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
        writePostViewModel.selectedShopInfo.observe(this) { selectedShopInfo ->
            binding.tvPlaceRegister.text = "이름"
            binding.tvPlaceRegisterExplain.text = "주소"

            with(binding.includeBdsSearchbarOne) {
                searchBarSearchIc.visibility = View.GONE
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopName)
            }
            with(binding.includeBdsSearchbarTwo) {
                ctlSearchBarContainer.visibility = View.VISIBLE
                searchBarEt.text = Editable.Factory.getInstance().newEditable(selectedShopInfo.shopAddress)
                ctlSearchBarContainer.setBackgroundResource(com.depromeet.bds.R.drawable.temp_bg_search_bar)
                searchBarSearchIc.visibility = View.GONE

            }
        }
    }

    //TODO 해시태그 선택 adapter로 구현
    private fun setHashTagClickListener() {
        binding.includeChip.tvHashtagSelectExplain.text = "(선택사항)"

        with(binding.includeChip) {
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

    //TODO 1.선택하기 누르면
    private fun setPictureSelectClickListener() {
        binding.ibtnPlaceRegister.setOnClickListener {
            //TODO 2.권한 체크
            checkPermission()
        }
    }

    private fun checkPermission() {
        val cameraPermission =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            //프로그램 진행
            Timber.e("접근권한 있음")
            startProcess()
        } else {
            //권한요청
            Timber.e("접근권한 없음")
            requestPermission()
        }
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
            folderTitle = "사진 등록",
            limitMessage = "ㅋㅋㅋ"
        )
        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            writePostViewModel.setSelectedPhotoList(images.map { it.uri }.toMutableList())
        }
    }

    private fun setPhotoRvAdapter() {
        photoRvAdapter = PhotoRvAdapter(requireContext())
        binding.rvPlaceRegister.adapter = photoRvAdapter
    }

    private fun setSelectedPhtoObserve() {
        writePostViewModel.selectedPhotoList.observe(viewLifecycleOwner) {
            photoRvAdapter.submitList(it)
        }
    }


    //TODO 5.권한 없으면 받아오기
    private fun requestPermission() {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            when (isGranted) {
                true -> startProcess() // 권한이 있다면 진행
                false -> Toast.makeText(requireContext(), "갤러리 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
}
