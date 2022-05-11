package com.depromeet.baton.presentation.ui.writepost.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlaceRegisterFragment : BaseFragment<FragmentPlaceRegisterBinding>(R.layout.fragment_place_register) {
    private val writePostViewModel: WritePostViewModel by activityViewModels()

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeRegisterFragment = this@PlaceRegisterFragment

        setSearchBar()
        selectedShopObserve()
        setPlaceRegisterClickListener()
        setPictureSelectClickListener()
        setHashTagClickListener()
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

    //TODO 사진 선택->갤러리
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
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        getResultText.launch(intent)  //TODO 3.선택한거 url로 바꾸기
    }

    //4.TODO  4. img->url로 바꾸는 중, 콜백 처리/중
    private val getResultText =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) // 이거 deprecated 되지 않았나 ?
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val url = result.data?.data
                Timber.e("$url")
                // TODO URL로 하고싶은거
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
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
