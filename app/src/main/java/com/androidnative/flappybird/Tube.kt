package com.androidnative.flappybird

import kotlin.random.Random

data class Tube(
    var coordinateX:Int,
    var topTupOffsetY:Int,
    var height:Int,
    var gapBetweenTopAndBottom:Int,
){
    fun getTopTubeY() = topTupOffsetY-height
    fun getBottomTubeY() = topTupOffsetY + gapBetweenTopAndBottom
}
