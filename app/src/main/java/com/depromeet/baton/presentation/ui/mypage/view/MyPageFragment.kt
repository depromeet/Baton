package com.depromeet.baton.presentation.ui.mypage.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentMyPageBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.MyPageViewModel
import com.depromeet.baton.presentation.ui.mypage.viewmodel.ProfileViewModel
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

    private var alertDialog: AlertDialog?  = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
       setObserver()
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
                replaceFragment(profileEditFragment)
            }
            mypageNotification.setOnClickListener {
                replaceFragment(notificationFragment)
            }

        }
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



    private fun replaceFragment(fragment: Fragment){
        childFragmentManager.beginTransaction().add(R.id.fragment_container_view,fragment).addToBackStack(null).commit()
    }

    private fun setLogoutDialog(){
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.dialog_mypage, null)
        alertDialog = AlertDialog.Builder(context, com.depromeet.bds.R.style.MyPageAlertDialog)
            .setView(view)
            .create()
        val buttonCancel = view.findViewById<Button>(R.id.dialog_cancel)
        val buttonConfirm = view.findViewById<Button>(R.id.dialog_delete)

        buttonCancel.setOnClickListener {
            alertDialog?.dismiss()
        }

        buttonConfirm.setOnClickListener {
            //TODO 삭제 API
        }
    }
}