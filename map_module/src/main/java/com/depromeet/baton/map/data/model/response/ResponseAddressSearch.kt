package com.depromeet.baton.map.data.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "results")
data class AddressResult(
    @Element
    val common: Common?,
    @Element
    val juso: List<Juso>?,
)

@Xml(name = "common")
data class Common(
    @PropertyElement(name="totalCount")
    val totalCount: String?,
    @PropertyElement(name="currentPage")
    val currentPage: Int?,
    @PropertyElement(name="countPerPage")
    val countPerPage: Int?,
    @PropertyElement(name="errorCode")
    val errorCode: String?,
    @PropertyElement(name="errorMessage")
    val errorMessage: String?,
)
@Xml(name = "juso")
data class Juso(
    @PropertyElement(name="roadAddr")
    val roadAddr: String?,
    @PropertyElement(name="roadAddrPart1")
    val roadAddrPart1: String?,
    @PropertyElement(name="roadAddrPart2")
    val roadAddrPart2: String?,
    @PropertyElement(name="jibunAddr")
    val jibunAddr: String?,
    @PropertyElement(name="engAddr")
    val engAddr: String?,
    @PropertyElement(name="zipNo")
    val zipNo: String?,
    @PropertyElement(name="admCd")
    val admCd: String?,
    @PropertyElement(name="rnMgtSn")
    val rnMgtSn: String?,
    @PropertyElement(name="bdMgtSn")
    val bdMgtSn: String?,
    @PropertyElement(name="detBdNmList")
    val detBdNmList: String?,
    @PropertyElement(name="bdNm")
    val bdNm: String?,
    @PropertyElement(name="bdKdcd")
    val bdKdcd: String?,
    @PropertyElement(name="siNm")
    val siNm: String?,
    @PropertyElement(name="sggNm")
    val sggNm: String?,
    @PropertyElement(name="emdNm")
    val emdNm: String?,
    @PropertyElement(name="liNm")
    val liNm: String?,
    @PropertyElement(name="rn")
    val rn : String?,
    @PropertyElement(name="udrtYn")
    val udrtYn: Int?,
    @PropertyElement(name="buldMnnm")
    val buldMnnm: Int?,
    @PropertyElement(name="buldSlno")
    val buldSlno: Int?,
    @PropertyElement(name="mtYn")
    val mtYn: String?,
    @PropertyElement(name="lnbrMnnm")
    val lnbrMnnmm: Int?,
    @PropertyElement(name="lnbrSlno")
    val lnbrSlno: Int?,
    @PropertyElement(name="emdNo")
    val emdNo: String?,
    @PropertyElement(name="hstryYn")
    val hstryYn: String?,
    @PropertyElement(name="relJibun")
    val relJibun: String?,
    @PropertyElement(name="hemdNm")
    val hemdNm: String?,
    )