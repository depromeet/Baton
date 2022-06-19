package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.MyPageViewModel
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
import com.depromeet.baton.presentation.ui.routing.RoutingActivity
import com.depromeet.baton.presentation.ui.sign.AddAccountActivity
import com.depromeet.baton.presentation.ui.sign.SignActivity
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.bds.component.BdsDialog
import com.depromeet.bds.component.BdsToast
import com.depromeet.bds.component.DialogType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel  by  viewModels<MyPageViewModel>()

    private var saleHistoryFragment = SaleHistoryFragment()
    private val purchaseHistoryFragment by lazy { PurchaseHistoryFragment() }
    private var likeTicketFragment = LikeTicketFragment()
    private val profileEditFragment by lazy{ ProfileFragment() }
    private val notificationFragment by lazy{ NotificationFragment() }
    private val serviceTermFragment by lazy { ServiceTermFragment() }

    private lateinit var logoutDialog: BdsDialog
    private lateinit var withdrawalDialog: BdsDialog
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setDialog()
    }


    private fun initView(){
        myPageViewModel.getProfile()
        with(binding){
            mypageSaleHistoryCd.setOnClickListener {
            
                replaceFragment(saleHistoryFragment)
            }
            mypagePurchaseCd.setOnClickListener {
                replaceFragment(purchaseHistoryFragment)
            }
            mypageLikeCd.setOnClickListener {
                if(likeTicketFragment.isAdded){
                    requireActivity().supportFragmentManager.beginTransaction().remove(likeTicketFragment).commit()
                    likeTicketFragment= LikeTicketFragment()
                }
                replaceFragment(likeTicketFragment)
            }
            mypageProfileIv.setOnClickListener {
               val bundle =  bundleOf("nickName" to myPageViewModel.uiState.value.nickName,
                    "phoneNumber" to myPageViewModel.uiState.value.phoneNumber ,
                    "profileImg" to myPageViewModel.uiState.value.profileImage.toString() )
                replaceFragment( ProfileFragment.newInstance(bundle),"profileFragment")
            }
            mypageNotification.setOnClickListener {
                replaceFragment(notificationFragment)
            }
            mypageLogout.setOnClickListener {
                logoutDialog?.show()
            }
            mypageWithdrawal.setOnClickListener { withdrawalDialog.show() }
            mypageServiceTerm.setOnClickListener { replaceFragment(serviceTermFragment) }
            mypageAccount.setOnClickListener { startAccountView() }
        }
    }

    private fun setDialog(){
        logoutDialog = BdsDialog(requireContext(), DialogType.SECONDARY)
        logoutDialog.setVerticalDialog(::onClickLogoutConfirm , ::onClickCancel,"로그아웃")
        logoutDialog.setTitle("정말 로그아웃 하시나요?")
        logoutDialog.setImage(com.depromeet.bds.R.drawable.ic_img_empty_filter)

        withdrawalDialog =  BdsDialog(requireContext(), DialogType.SECONDARY)
        withdrawalDialog.setHorizonDialog(::onClickWithdrawalConfirm , ::onClickCancel,"회원탈퇴")
        withdrawalDialog.setTitle("정말 탈퇴하시겠어요?")
        withdrawalDialog.setContent("회원을 탈퇴하면\n앱 내 모든 정보가 삭제됩니다.")
        withdrawalDialog.setImage(com.depromeet.bds.R.drawable.ic_img_empty_warning)

    }

    private fun onClickLogoutConfirm(){
        //TODO logout logic
        myPageViewModel.logout() //sharedPreference 모두 삭제
        logoutDialog.dismiss()
        val intent = Intent(requireActivity(), SignActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
    private fun onClickCancel(){

    }

    private fun onClickWithdrawalConfirm(){
        myPageViewModel.deleteUser()
        withdrawalDialog.dismiss()
        val intent = Intent(requireActivity(), SignActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun setObserver(){
        myPageViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState ->
                run{
                    binding.uiState =uiState
                    Glide.with(requireContext())
                        .load(uiState.profileImage)
                        .error(com.depromeet.bds.R.drawable.img_profile_basic_smile_56)
                        .transform(CircleCrop())
                        .into(binding.mypageProfileIv)
                }
            }
            .launchIn(viewLifecycleScope)

        myPageViewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private fun handleViewEvents( events: List<MyPageViewModel.ViewEvent>) {
        events.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
               is MyPageViewModel.ViewEvent.EventWithdrawal ->{
                   requireContext().BdsToast(viewEvent.msg).show()
               }
            }
           myPageViewModel.consumeViewEvent(viewEvent)
        }
    }


    private fun startAccountView(){
        if(myPageViewModel.uiState.value.account==null) EmptyAccountActivity.start(requireContext())  //계좌정보 추가
        else EditAccountActivity.start(requireContext(), myPageViewModel.uiState.value.account!!)
    }


    private fun setBackPressed(){
        callback= object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 눌렀을 때 동작할 코드
              if( this@MyPageFragment.isAdded
                  &&  requireActivity().supportFragmentManager.fragments.map { it.tag }.contains("myPageFragment")
                  && requireActivity().supportFragmentManager.fragments.size>2){
                  requireActivity().supportFragmentManager.popBackStack()
               }else{
                  requireActivity().finish()
               }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this@MyPageFragment, callback)
    }


    private fun replaceFragment(fragment: Fragment, tag :String?=null){
       requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view,fragment,tag)
            .addToBackStack(null).commit()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        setBackPressed()
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}