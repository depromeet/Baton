package com.depromeet.baton.presentation.ui.mypage.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.depromeet.baton.R
import com.depromeet.baton.presentation.ui.mypage.adapter.ProfileIconAdapter
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIconItem
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.util.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.model.ImagePickerConfig
import com.nguyenhoanglam.imagepicker.ui.imagepicker.registerImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@AndroidEntryPoint
class ProfileBottomFragment(): BottomSheetDialogFragment() {

    private val profileViewModel by viewModels<ProfileViewModel>(ownerProducer = {requireActivity()})

    private val profileAdapter: ProfileIconAdapter by lazy {
        ProfileIconAdapter(list, ::onClickCamera, ::onClickEmotion)
    }

    private val list : ArrayList<ProfileIconItem> by lazy { initList() }

    private var nowImgUrl : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            com.depromeet.bds.R.style.BdsBottomSheetDialogTheme
        );

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View=inflater.inflate(R.layout.fragment_profile_bottom,container,false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        initAdapter()
        setObserver()
        setCheckBtnOnClickListener()
        setBackBtnOnClickListener()
        setRemoveBtnOnClickListener()

    }

    private fun setObserver(){
        //TODO CHECK
        profileViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> setImage(uiState.profileImage) }
            .launchIn(viewLifecycleScope)
    }


    private fun initAdapter(){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.profile_bottom_rv)
        recyclerView?.apply{
            adapter = profileAdapter
            val mLayoutManager = GridLayoutManager(requireContext(),4,GridLayoutManager.VERTICAL,false)
            layoutManager = mLayoutManager
            addItemDecoration(ProfileIconDecoration())
            itemAnimator=null
        }
        profileAdapter!!.submitList(list)
    }

    private fun initList(): ArrayList<ProfileIconItem>{
        val iconList = ProfileIcon.values().map{ i -> ProfileIconItem.EmotionIcon(i)}
        val list = ArrayList<ProfileIconItem>()
        list.add(ProfileIconItem.CameraIcon())
        list.addAll(iconList)
        list.removeAt(list.lastIndex)
        return list
    }

    private fun setImage(uri : Uri?){
        nowImgUrl.apply {
            if(this==null && profileViewModel.uiState.value.profileImage ==null)
                uriConverter(requireContext(), com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_96)
            else uri
        }

        Glide.with(requireContext())
            .load(nowImgUrl?:uri)
            .error(com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_96)
            .transform(CircleCrop())
            .into(view?.findViewById<ImageView>(R.id.profile_bottom_my_iv)!!)
    }

    private fun onClickCamera(){
        checkPermission()
    }

    private fun onClickEmotion(profileIcon: ProfileIcon, pos: Int){
        view?.findViewById<ImageView>(R.id.profile_bottom_my_iv)?.setImageResource(profileIcon.size56)
        view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.isEnabled=true
        nowImgUrl = Uri.parse(profileIcon.url)
    }

    private fun setRemoveBtnOnClickListener(){
        view?.findViewById<Button>(R.id.profile_bottom_remove_btn)?.setOnClickListener {
            val empty = uriConverter(requireContext(), com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_96)
            with(empty){
                view?.findViewById<ImageView>(R.id.profile_bottom_my_iv)?.setImageURI(this)
                nowImgUrl= null
            }
            view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.isEnabled=true
        }
    }

    private fun setCheckBtnOnClickListener(){
        view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.setOnClickListener {
            profileViewModel.submitProfileImg(nowImgUrl)
            dialog?.dismiss()
        }
    }

    private fun setBackBtnOnClickListener(){
        view?.findViewById<ImageView>(R.id.profile_bottom_back_btn)?.setOnClickListener {
            dialog?.dismiss()
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
            isShowNumberIndicator = false,
            maxSize = 1,
        )
        launcher.launch(config)
    }

    private val launcher = registerImagePicker { images ->
        if (images.isNotEmpty()) {
            setPhotoImage(images[0])
        }
    }

    private fun setPhotoImage(image: Image){
        nowImgUrl = image.uri
        view?.findViewById<ImageView>(R.id.profile_bottom_my_iv)?.setImageURI(nowImgUrl)
        view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.isEnabled=true
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

}