package com.depromeet.baton.presentation.ui.address.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivitySearchAddressBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.address.SearchAddressAdapter
import com.depromeet.baton.presentation.ui.address.viewmodel.SearchAddressViewModel
import com.depromeet.baton.presentation.ui.address.model.AddressInfo
import com.depromeet.baton.util.BatonSpfManager
import com.depromeet.baton.util.gpsConverter
import com.depromeet.bds.component.BdsDialog
import com.depromeet.bds.component.BdsSearchBar
import com.depromeet.bds.component.BdsToast
import com.depromeet.bds.component.DialogType
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchAddressActivity : BaseActivity<ActivitySearchAddressBinding>(R.layout.activity_search_address) {
    private val searchAddressViewModel by viewModels<SearchAddressViewModel>()
    @Inject lateinit var spfManager: BatonSpfManager
    private val dialog by lazy {  BdsDialog(this, DialogType.SECONDARY) }
    private lateinit var listAdapter: SearchAddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = searchAddressViewModel
        initView()
        setListener()
        setAdapter()
        setObserver()
    }

    private fun initView() {
        binding.addressToolbar.titleTv.text = "위치검색"

    }

    private fun setListener() {
        binding.searchAddressSetLocation.setOnClickListener {
            if (isPermitted()) {
                val intent = Intent(this, MyLocationActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                requestPermissions(AddressActivity.permissions, AddressActivity.PERMISSION_REQUEST)
            }//권한

                //TODO PERMISSION CHEck
        }

        KeyboardVisibilityEvent.setEventListener(this) {
            binding?.searchAddressEt.searchBarKeyBoardListener(it)
        }

    }

    private  fun setObserver() {
        searchAddressViewModel.searchAddress("")
        binding.searchAddressEt.textListener = object : BdsSearchBar.TextListener {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                searchAddressViewModel.searchAddress(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchAddressViewModel.items.collect{
                    if(!it.isEmpty()){
                        listAdapter.submitList(it)
                    }
                }
            }
        }
    }


    private fun setAdapter() {
        listAdapter = SearchAddressAdapter { selectedItem: AddressInfo ->
            listItemClicked(selectedItem)
        }
        listAdapter.setQueryListener(object: SearchAddressAdapter.SearchColorListener{
            override fun getQuery(): String {
                return binding.searchAddressEt.getText()
            }
        })
        binding.searchAddressRecycler.adapter = listAdapter
    }

    private fun listItemClicked(item: AddressInfo) {
        spfManager.saveAddress(item.roadAddress, item.address)
        spfManager.saveLocation(gpsConverter(this,item.roadAddress))

        val intent = Intent(this, MyLocationDetailActivity::class.java)
        startActivity(intent)
    }

    private fun isPermitted(): Boolean {
        for (perm in AddressActivity.permissions)
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) return false
        return true
    }//권한을 허락 받아야함


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AddressActivity.PERMISSION_REQUEST -> { // 권한이 비어있는 경우 에러
                if (grantResults.isEmpty()) {
                    throw RuntimeException("Empty Permission Result")
                } // 거부된 권한이 있는지 확인한다
                var isPermitted = true
                val deniedPermission = ArrayList<String>()
                for ((id, result) in grantResults.withIndex()) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isPermitted = false
                        deniedPermission.add(permissions[id])
                    }
                }
                if (isPermitted) {
                    val intent = Intent(this, MyLocationActivity::class.java)
                    startActivity(intent)
                } else {
                    //거부만 선택
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])
                    ) {
                        // 권한이 필요하다는 토스트 메시지를 띄운다
                        this.BdsToast(resources.getString(R.string.locaton_permission_alert)).show()
                        // 권한을 다시 요청한다
                        requestPermissions(deniedPermission.toArray(arrayOfNulls<String>(deniedPermission.size)),
                            AddressActivity.PERMISSION_REQUEST
                        )
                    }
                    // 거부 및 다시보지 않기를 선택한 경우
                    else {
                        // 권한 설정으로 이동할 수 있도록 알림창을 띄운다
                        showDialogToGetPermission()
                    }
                }
            }
        }

    }


    private fun showDialogToGetPermission() {
        dialog.setHorizonDialog(::onClickConfirm, ::onClickCancel)
        dialog.setTitle("위치 권한 설정이 필요합니다.")
        dialog.setContent("현재 위치를 기반으로 거래 가능한 양도권을 보여드립니다.")
        dialog.setImage(com.depromeet.bds.R.drawable.ic_img_empty_warning)
        dialog.show()
    }

    private fun onClickConfirm(){
        dialog.dismiss()
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    private fun onClickCancel(){}

    companion object {
        val PERMISSION_REQUEST = 99
        var permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        //권한 가져오기

        fun start(context: Context) {
            val intent = Intent(context, SearchAddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}
