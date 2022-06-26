package com.depromeet.baton.presentation.util

object RegexConstant {
    val ONLY_COMPLETE_HANGLE = Regex("[가-힝]*")
    val ONLY_NUMBERS = Regex("[0-9]*")
    val ONLY_COMPLETE_HANGLE_SPACE = Regex("[가-힝 ]*")
    val NICKNAME_REGEX = Regex("^[가-힣ㄱ-ㅎa-zA-Z0-9._ -]{1,}\$")
}
