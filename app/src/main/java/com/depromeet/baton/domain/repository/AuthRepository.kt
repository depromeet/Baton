package com.depromeet.baton.domain.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.depromeet.baton.domain.api.user.TokenApi
import com.depromeet.baton.domain.model.AuthInfo
import com.depromeet.baton.remote.user.TokenService
import com.depromeet.baton.util.SerializedPref
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sign

@Singleton
class AuthRepository @Inject constructor(
    @ApplicationContext context: Context,
    moshi: Moshi,
) {

    private val pref: SharedPreferences by lazy {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        EncryptedSharedPreferences.create(
            PREF,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    var authInfo by SerializedPref(pref, moshi.adapter(AuthInfo::class.java))

    fun setAuthInfo(accessToken:String, refreshToken :String){
        authInfo?.let{
            it.accessToken = accessToken
            it.refreshToken = refreshToken
        }
    }

    suspend fun isLoggedIn() : Boolean {
        return authInfo != null
    }

    fun logout(){
        authInfo = null
    }

    companion object {
        private const val PREF = "auth_"
    }
}
