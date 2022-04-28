package com.depromeet.baton.presentation.ui.address

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddressBinding
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.UiState
import com.depromeet.baton.presentation.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.NaverMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class AddressActivity  : BaseActivity<ActivityAddressBinding>(R.layout.activity_address) {
    companion object {
        val PERMISSION_REQUEST = 99
        lateinit var naverMap: NaverMap
        var permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        //권한 가져오기
    }

    val mapViewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListener()

    }


    private fun isPermitted(): Boolean {
        for (perm in permissions)
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) return false
        return true
    }//권한을 허락 받아야함

    fun setListener(){
        binding.currentLocationBtn.setOnClickListener {
            if (isPermitted()){
                binding.currentLocationBtn.isClickable=true
                val intent = Intent(this,MyLocationActivity::class.java)
                startActivity(intent)
            }
            else{
                requestPermissions(permissions, PERMISSION_REQUEST)
                showDialogToGetPermission()
            }//권한 확인
        }
    }

  fun showDialogToGetPermission(){
      val builder = AlertDialog.Builder(this)
      builder.setTitle("위치 권한설정").setMessage("현재 위치를 파악하기 위해 위치 권한 접근이 필요합니다")
          .setPositiveButton("확인"){ dialog, i->
              val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startActivity(intent)
          }

      builder.setNegativeButton("취소"){ dialog, i -> }
      val dialog = builder.create()
      dialog.show()

  }
}
