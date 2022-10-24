package com.depromeet.baton.presentation.ui.mypage.model

import com.depromeet.baton.R

enum class ProfileIcon(var size56: Int, var size40: Int,val url : String) {
    Smirk(com.depromeet.bds.R.drawable.ic_img_profile_smirk_56,
          com.depromeet.bds.R.drawable.ic_img_profile_smirk_40,
        "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_smirk_96.svg"),
    Happy(com.depromeet.bds.R.drawable.ic_img_profile_happy_56,
        com.depromeet.bds.R.drawable.ic_img_profile_happy_40,
    "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_happy_96.svg"),
    SoHappy(com.depromeet.bds.R.drawable.ic_img_profile_sohappy_56,
        com.depromeet.bds.R.drawable.ic_img_profile_sohappy_40,
        "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_sohappy_96.svg"),
    SurPrise(com.depromeet.bds.R.drawable.ic_img_profile_surprised_56,
        com.depromeet.bds.R.drawable.ic_img_profile_surprised_40,
        "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_surprised_96.svg"),
    Startled(com.depromeet.bds.R.drawable.ic_img_profile_startled_56,
        com.depromeet.bds.R.drawable.ic_img_profile_startled_40,
    "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_startled_96.svg"),
    Curiosity(com.depromeet.bds.R.drawable.ic_img_profile_curiosity_56,
        com.depromeet.bds.R.drawable.ic_img_profile_curiosity_40,
    "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_curiosity_96.svg"),
    Sulky(com.depromeet.bds.R.drawable.ic_img_profile_sulky_56,
        com.depromeet.bds.R.drawable.ic_img_profile_sulky_40,
    "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_sulky_96.svg"),
    Smile(com.depromeet.bds.R.drawable.ic_img_profile_smile_56,
        com.depromeet.bds.R.drawable.ic_img_profile_smile_40,
    "https://baton-bucket.s3.ap-northeast-2.amazonaws.com/static/user-api/mypage/img/img_profile_smile_96.svg"),
}

