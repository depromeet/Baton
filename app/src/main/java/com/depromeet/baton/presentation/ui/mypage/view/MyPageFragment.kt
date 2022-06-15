package com.depromeet.baton.presentation.ui.mypage.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
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
import com.depromeet.bds.component.DialogType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel  by  viewModels<MyPageViewModel>()

    private val saleHistoryFragment by lazy{ SaleHistoryFragment()}
    private val purchaseHistoryFragment by lazy { PurchaseHistoryFragment() }
    private val likeTicketFragment by lazy{ LikeTicketFragment() }
    private val profileEditFragment by lazy{ ProfileFragment() }
    private val notificationFragment by lazy{ NotificationFragment() }
    private val serviceTermFragment by lazy { ServiceTermFragment() }

    private lateinit var logoutDialog: BdsDialog
    private lateinit var withdrawalDialog: BdsDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setBackPressed()
        setDialog()
    }

    override fun onStart() {
        super.onStart()
        myPageViewModel.getProfile()
    }

    private fun initView(){
        with(binding){
            mypageSaleHistoryCd.setOnClickListener {
                replaceFragment(saleHistoryFragment)
            }
            mypagePurchaseCd.setOnClickListener {
                replaceFragment(purchaseHistoryFragment)
            }
            mypageLikeCd.setOnClickListener {
                replaceFragment(likeTicketFragment)
            }
            mypageProfileIv.setOnClickListener {
                replaceFragment(profileEditFragment,"profileFragment")
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    private fun onClickCancel(){

    }

    private fun onClickWithdrawalConfirm(){
        withdrawalDialog.dismiss()
    }

    private fun setObserver(){
        myPageViewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState ->
                run{
                    binding.uiState =uiState
                }
            }
            .launchIn(viewLifecycleScope)
    }


    private fun startAccountView(){
        if(myPageViewModel.uiState.value.account==null) EmptyAccountActivity.start(requireContext())  //계좌정보 추가
        else EditAccountActivity.start(requireContext(), myPageViewModel.uiState.value.account!!)
    }


    private fun setBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 눌렀을 때 동작할 코드
               if(this@MyPageFragment.isAdded && childFragmentManager.fragments.isNotEmpty()) childFragmentManager.popBackStack()
            }
        })
    }


    private fun replaceFragment(fragment: Fragment, tag :String?=null){
        childFragmentManager.beginTransaction().add(R.id.fragment_container_view,fragment,tag).addToBackStack(null).commit()
    }

}