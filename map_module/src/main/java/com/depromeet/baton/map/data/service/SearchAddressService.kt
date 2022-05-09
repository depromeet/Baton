package com.depromeet.baton.map.data.service

import com.depromeet.baton.map.data.model.AddressResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

class  SearchAddressService {
    val client = SearchAddressClient.instance?.create (SearchAddressAPI::class.java)
}

interface SearchAddressAPI{
    @GET("/addrlink/addrLinkApi.do")
    suspend fun searchAddress(
        @Query("confmKey") key: String,
        @Query("keyword") keyword: String,
    ) : Response<AddressResult>
}
