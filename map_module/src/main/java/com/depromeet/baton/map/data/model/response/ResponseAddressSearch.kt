package com.depromeet.baton.map.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Keep
@Xml(name = "results")
data class AddressResult(
    @Keep
    @Element
    val common: Common?,
    @Keep
    @Element
    val juso: List<Juso>?,
)
@Keep
@Xml(name = "common")
data class Common(
    @Keep
    @PropertyElement(name="totalCount")
    val totalCount: String?,
    @Keep
    @PropertyElement(name="currentPage")
    val currentPage: Int?,
    @Keep
    @PropertyElement(name="countPerPage")
    val countPerPage: Int?,
    @Keep
    @PropertyElement(name="errorCode")
    val errorCode: String?,
    @Keep
    @PropertyElement(name="errorMessage")
    val errorMessage: String?,
)
@Keep
@Xml(name = "juso")
data class Juso(
    @Keep
    @PropertyElement(name="roadAddr")
    val roadAddr: String?,
    @Keep
    @PropertyElement(name="roadAddrPart1")
    val roadAddrPart1: String?,
    @Keep
    @PropertyElement(name="roadAddrPart2")
    val roadAddrPart2: String?,
    @Keep
    @PropertyElement(name="jibunAddr")
    val jibunAddr: String?,
    @Keep
    @PropertyElement(name="engAddr")
    val engAddr: String?,
    @Keep
    @PropertyElement(name="zipNo")
    val zipNo: String?,
    @Keep
    @PropertyElement(name="admCd")
    val admCd: String?,
    @Keep
    @PropertyElement(name="rnMgtSn")
    val rnMgtSn: String?,
    @Keep
    @PropertyElement(name="bdMgtSn")
    val bdMgtSn: String?,
    @Keep
    @PropertyElement(name="detBdNmList")
    val detBdNmList: String?,
    @Keep
    @PropertyElement(name="bdNm")
    val bdNm: String?,
    @Keep
    @PropertyElement(name="bdKdcd")
    val bdKdcd: String?,
    @Keep
    @PropertyElement(name="siNm")
    val siNm: String?,
    @Keep
    @PropertyElement(name="sggNm")
    val sggNm: String?,
    @Keep
    @PropertyElement(name="emdNm")
    val emdNm: String?,
    @Keep
    @PropertyElement(name="liNm")
    val liNm: String?,
    @Keep
    @PropertyElement(name="rn")
    val rn : String?,
    @Keep
    @PropertyElement(name="udrtYn")
    val udrtYn: Int?,
    @Keep
    @PropertyElement(name="buldMnnm")
    val buldMnnm: Int?,
    @Keep
    @PropertyElement(name="buldSlno")
    val buldSlno: Int?,
    @Keep
    @PropertyElement(name="mtYn")
    val mtYn: String?,
    @Keep
    @PropertyElement(name="lnbrMnnm")
    val lnbrMnnmm: Int?,
    @Keep
    @PropertyElement(name="lnbrSlno")
    val lnbrSlno: Int?,
    @Keep
    @PropertyElement(name="emdNo")
    val emdNo: String?,
    @Keep
    @PropertyElement(name="hstryYn")
    val hstryYn: String?,
    @Keep
    @PropertyElement(name="relJibun")
    val relJibun: String?,
    @Keep
    @PropertyElement(name="hemdNm")
    val hemdNm: String?,
    )