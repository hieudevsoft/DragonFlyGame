package com.androidnative.flappybird

import android.content.Context

class GameController {
    companion object{
        lateinit var gameEngine:GameEngine
        fun initialization(context:Context){
            gameEngine = GameEngine(context)
        }
        var SCREEN_WIDTH = 0f
        var SCREEN_HEIGHT = 0f
        var GRAVITY = 3f
        var VELOCITY_WHEN_TOUCH = -25f
        var gapBetweenTopAndBottomTubes = 600f
    }
}