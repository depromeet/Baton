package com.depromeet.baton.presentation.util

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Throws(Exception::class)
private fun encrypt(raw: ByteArray, clear: ByteArray): ByteArray? {
    val skeySpec = SecretKeySpec(raw, "AES")
    val cipher: Cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
    return cipher.doFinal(clear)
}

@Throws(Exception::class)
private fun decrypt(raw: ByteArray, encrypted: ByteArray): ByteArray? {
    val skeySpec = SecretKeySpec(raw, "AES")
    val cipher: Cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, skeySpec)
    return cipher.doFinal(encrypted)
}