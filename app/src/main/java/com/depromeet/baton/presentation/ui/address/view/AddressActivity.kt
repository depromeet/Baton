package com.depromeet.baton.presentation.ui.address.view


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddressBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.address.viewmodel.AddressViewModel
import com.depromeet.baton.presentation.ui.address.viewmodel.DistanceType
import com.depromeet.baton.util.BatonSpfManager
import com.depromeet.bds.component.BdsDialog
import com.depromeet.bds.component.BdsToast
import com.depromeet.bds.component.DialogType
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class AddressActivity : BaseActivity<ActivityAddressBinding>(R.layout.activity_address) {

    @Inject lateinit var spfManager: BatonSpfManager
    private val dialog by lazy {  BdsDialog(this,DialogType.SECONDARY) }
    private val addressViewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = addressViewModel

        setContentView(binding.root)
        initView()
        setListener()

    }

    override fun onResume() {
        super.onResume()
        initView()
    }


    private fun initView() {
        binding.addressToolbar.titleTv.text = "위치설정"
        binding.addressDistanceTv.text.apply {
            val distance =spfManager.getMaxDistance().getMaxDistanceWithUnit()
            if( distance=="500m") "" else distance
        }
        binding.distanceSeekBar.setPadding(0, 0, 0, 0)

        binding.distanceSeekBar.setProgress(addressViewModel.setDistanceProgress(spfManager.getMaxDistance()!!))
        if (this.intent.getBooleanExtra("isChanged", false)) binding.addressDoneBtn.visibility = View.VISIBLE
    }


    private fun setListener() {
        /*현재 위치 설정*/
        binding.currentLocationBtn.setOnClickListener {
            if (isPermitted()) {
                binding.currentLocationBtn.isClickable = true
                val intent = Intent(this, MyLocationActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }//권한 확인
        }

        /*위치 검색 설정*/
        binding.searchLocationBtn.setOnClickListener {
            val intent = Intent(this, SearchAddressActivity::class.java)
            startActivity(intent)
        }

        binding.addressDoneBtn.setOnClickListener {
            onBackPressed()
        }

        binding.addressToolbar.backBtn.setOnClickListener {
            onBackPressed()
        }


        binding.distanceSeekBar.addSeekbarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (progress <= 1000) {
                    addressViewModel.distanceCalculator(progress, DistanceType.MAX1KM)
                } else if (progress in 1001..2000) {
                    addressViewModel.distanceCalculator(progress, DistanceType.MAX3KM)
                } else {
                    addressViewModel.distanceCalculator(progress, DistanceType.MAX5KM)
                }

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }


    private fun isPermitted(): Boolean {
        for (perm in permissions)
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) return false
        return true
    }//권한을 허락 받아야함

    /*
        shuldSHowRequestPermissionRationale
        •True일 경우: 사용자가 거부한 권한이 존재
        •False일 경우: 사용자가 권한 허용을 거부하고 다시 묻지 않음까지 선택

    */

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST -> { // 권한이 비어있는 경우 에러
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
                        requestPermissions(deniedPermission.toArray(arrayOfNulls<String>(deniedPermission.size)), PERMISSION_REQUEST)
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
            ACTION_APPLICATION_DETAILS_SETTINGS,
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
            val intent = Intent(context, AddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}

