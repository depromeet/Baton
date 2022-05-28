package com.depromeet.baton.presentation.ui.mypage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.baton.R
import com.depromeet.baton.presentation.ui.mypage.adapter.ProfileIconAdapter
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIconItem
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.util.ProfileIconDecoration
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileBottomFragment(private val profileViewModel: ProfileViewModel): BottomSheetDialogFragment() {

    private val profileAdapter: ProfileIconAdapter by lazy {
        ProfileIconAdapter( profileViewModel, list, ::onClickCamera, ::onClickEmotion)
    }

    private val list : ArrayList<ProfileIconItem> by lazy {
        initList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            BottomSheetDialogFragment.STYLE_NORMAL,
            com.depromeet.bds.R.style.BdsBottomSheetDialogTheme
        );

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_profile_bottom, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from<View>(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_DRAGGING
        initAdapter()
        setCheckBtnOnClickListener()
        setBackBtnOnClickListener()
    }


    private fun initAdapter(){
        val recyclerView = view?.findViewById<RecyclerView>(R.id.profile_bottom_rv)
        recyclerView?.apply{
            adapter = profileAdapter
            val mLayoutManager = GridLayoutManager(requireContext(),4,GridLayoutManager.VERTICAL,false)
            layoutManager = mLayoutManager
            addItemDecoration(ProfileIconDecoration())
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

    private fun onClickCamera(){

    }

    private fun onClickEmotion(profileIcon: ProfileIcon, pos: Int){
        view?.findViewById<ImageView>(R.id.profile_bottom_my_iv)?.setImageResource(profileIcon.size56)
        view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.isEnabled=true
    }

    private fun setCheckBtnOnClickListener(){
        view?.findViewById<Button>(R.id.profile_bottom_check_btn)?.setOnClickListener {
            profileViewModel.submitProfile()
            dialog?.dismiss()
        }
    }

    private fun setBackBtnOnClickListener(){
        view?.findViewById<ImageView>(R.id.profile_bottom_back_btn)?.setOnClickListener {

            dialog?.dismiss()
        }
    }
}