package com.depromeet.baton.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.depromeet.baton.R

fun AppCompatActivity.replace(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.fcv_main, fragment, null)
        .commit()
}