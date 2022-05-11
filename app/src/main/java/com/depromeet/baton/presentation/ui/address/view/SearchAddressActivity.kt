package com.depromeet.baton.presentation.ui.address.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
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
import com.depromeet.baton.util.saveAddress
import com.depromeet.bds.component.BdsSearchBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchAddressActivity : BaseActivity<ActivitySearchAddressBinding>(R.layout.activity_search_address) {
    private val searchAddressViewModel  by viewModels<SearchAddressViewModel>()

    private lateinit var listAdapter: SearchAddressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = searchAddressViewModel
        initView()
        setListener()
        setAdapter()
        setObserver()
    }
    private fun initView(){
        binding.addressToolbar.titleTv.text="위치검색"
        binding.addressToolbar.nextTv.visibility= View.GONE

    }

    private fun setListener(){
        binding.addressSearchBackIv.setOnClickListener {
            val intent = Intent(this, MyLocationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



    private  fun setObserver() {
        searchAddressViewModel.searchAddress("")
        binding.searchAddressEt.textListener= object : BdsSearchBar.TextListener {
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
                    Timber.e("NewItem!!!")
                    if(!it.isEmpty()){
                        listAdapter.submitList(it)
                    }
                }
            }
        }
    }


    private fun setAdapter(){
        listAdapter = SearchAddressAdapter(searchAddressViewModel) { selectedItem: AddressInfo ->
            listItemClicked(
                selectedItem
            )
        }
        binding.searchAddressRecycler.adapter = listAdapter
    }

    private fun listItemClicked(item : AddressInfo){
        saveAddress(item.roadAddress, item.address)
        val intent = Intent(this, MyLocationDetailActivity::class.java)
        startActivity(intent)
    }

}