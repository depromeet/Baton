package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
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
import com.depromeet.bds.component.BdsSearchBar
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SearchAddressActivity : BaseActivity<ActivitySearchAddressBinding>(R.layout.activity_search_address) {
    private val searchAddressViewModel by viewModels<SearchAddressViewModel>()
    @Inject
    lateinit var spfManager: BatonSpfManager
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
            val intent = Intent(this, MyLocationActivity::class.java)
            startActivity(intent)
            finish()
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

}
