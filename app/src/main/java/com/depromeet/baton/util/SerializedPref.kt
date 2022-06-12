package com.depromeet.baton.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.JsonAdapter
import kotlin.reflect.KProperty

class SerializedPref<T : Any>(
    private val pref: SharedPreferences,
    private val jsonAdapter: JsonAdapter<T>
) {
    private var updated = false
    private var cached: T? = null

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T? {
        if (!updated && cached != null) return cached
        updated = false

        return pref.getString(prop.name, null)
            ?.let { jsonAdapter.fromJson(it) }
            .also {
                cached = it
            }
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, newValue: T?) {
        pref.edit { putString(prop.name, newValue?.let { jsonAdapter.toJson(it) }) }
        updated = true
    }
}
