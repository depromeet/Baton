package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityEmptyAccountBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.sign.AddAccountActivity
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class EmptyAccountActivity : BaseActivity<ActivityEmptyAccountBinding>(R.layout.activity_empty_account) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.emptyAcountAddBtn.setOnClickListener {
            PostAccountActivity.start(this)
            finish()
        }
        binding.appbar.setOnBackwardClick{onBackPressed()}
    }
    companion object {
        fun start(context: Context){
            val intent = Intent(context, EmptyAccountActivity::class.java)
            context.startActivity(intent)
        }
    }
}
