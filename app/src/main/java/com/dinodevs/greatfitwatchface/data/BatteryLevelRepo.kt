package com.dinodevs.greatfitwatchface.data

fun interface BatteryLevelRepo {
    fun getBatteryLevel(): Int
}