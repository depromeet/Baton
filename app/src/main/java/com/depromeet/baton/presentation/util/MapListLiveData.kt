package com.depromeet.baton.presentation.util

import androidx.lifecycle.MutableLiveData


class MapListLiveData<T, K> : MutableLiveData<MutableMap<T, K>>() {

    init {
        value = mutableMapOf()
    }

    fun addAll(type: T, isChecked: K) {
        val items: MutableMap<T, K>? = value
        for (item in 0 until items?.size!!) {
            items[type] = isChecked
        }
        value = items
    }

    fun setChipState(type: T, isChecked: K) {
        val items: MutableMap<T, K>? = value
        items?.set(type, isChecked)
        value = items
    }
}