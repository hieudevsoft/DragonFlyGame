package com.androidnative.flappybird

data class GameEntity(
    var coordinateX:Float,
    var coordinateY:Float,
    var with:Float,
    var height:Float,
    var currentFrame:Int = 0,
    var velocity:Int = 0
){
    companion object{
        var maxFrame:Int = 3
    }

    init {
        if(GameController.SCREEN_WIDTH>0 && GameController.SCREEN_HEIGHT>0){
            coordinateX = GameController.SCREEN_WIDTH/2 - with/2
            coordinateY = GameController.SCREEN_HEIGHT/2 - height/2
            currentFrame = 0
            maxFrame = 3
        }
    }
}