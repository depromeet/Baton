package com.depromeet.baton.presentation.util

import androidx.lifecycle.MutableLiveData

class ListLiveData<T> : MutableLiveData<MutableList<T>?>() {

    init {
        value = mutableListOf()
    }

    fun add(item: T) {
        val items: MutableList<T>? = value
        if (items?.contains(item) == false)
            items.add(item)
        value = items
    }

    fun remove(item: T) {
        val items: MutableList<T>? = value
        items?.remove(item)
        value = items
    }

    fun clear() {
        val items: MutableList<T>? = value
        items?.clear()
        value = items
    }

    fun isListNotEmpty(): Boolean {
        return !value.isNullOrEmpty()
    }
}