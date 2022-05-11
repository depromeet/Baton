package com.depromeet.baton.presentation.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.depromeet.baton.BatonApp.Companion.TAG
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivitySampleBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.CheckItem
import com.depromeet.baton.presentation.sample.SampleViewModel.ViewEvent
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.reflect.jvm.internal.impl.util.Check

@AndroidEntryPoint
class SampleActivity : BaseActivity<ActivitySampleBinding>(R.layout.activity_sample) {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEvent()

        val list = ArrayList<CheckItem<String>>()
        list.add(CheckItem("hello",false))
        list.add(CheckItem("hello2",false))
        list.add(CheckItem("hello3",false))
        binding.btn.setOnClickListener {
            val bottomSheetFragment: BottomSheetFragment = BottomSheetFragment(list) {
                Toast.makeText(this, list.get(it).data, Toast.LENGTH_SHORT).show()
                Timber.e("activity listen")
            }
            bottomSheetFragment.show(supportFragmentManager, TAG)
        }
    }

    private fun initEvent() {
        viewModel.viewEvent.observe(this) { event ->
            when (event) {
                is ViewEvent.ShowToast -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
