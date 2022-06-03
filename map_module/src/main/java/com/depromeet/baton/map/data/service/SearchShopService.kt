package com.depromeet.baton.map.data.service

class SearchShopService {
    val client = NaverApiClient.instance?.create(NaverMapAPI::class.java)
}