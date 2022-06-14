package com.depromeet.baton.presentation.util

fun distanceFormatUtil (distance: Double) :String{
    if(distance < 1000) return "${(distance).toInt()}m"
    else return "${(distance/1000).toInt()}km"
}