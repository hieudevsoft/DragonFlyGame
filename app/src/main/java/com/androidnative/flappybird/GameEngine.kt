package com.androidnative.flappybird

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.Log
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import kotlin.random.Random


class GameEngine(private val context: Context) {
    sealed class GAMESTATE {
        object IDLE : GAMESTATE()
        object START : GAMESTATE()
        object GAMEOVER : GAMESTATE()
    }

    private lateinit var bitmapImage: Bitmap
    private lateinit var bitmapBackground: BitmapBackground
    private lateinit var dragon: GameEntity
    private lateinit var arrayBitmapResult: Array<Bitmap?>
    private lateinit var tubeTop: Bitmap
    private lateinit var tubeBottom: Bitmap
    private lateinit var tubes: MutableList<Tube>
    private var score = 0
    private var tubeIndexScore = 0

    companion object {
        var gameState:GAMESTATE = GAMESTATE.IDLE
        var numberOfTubes = 3
        var tubeVelocity = 12
        var distanceBetweenTubes = 0f
        var maxTubeOffsetY = 0f
        var minTubeOffsetY = 0f
    }

    private fun initComponent() {
        bitmapImage = BitmapHelper.scaledBitmap(
            context,
            BitmapHelper.getBitMapFromResource(context.resources, R.drawable.main_bg_game)
        )
        bitmapBackground = BitmapBackground(0f, 0f, 10f)
        val arrayBitmapDragon = BitmapHelper.getBitmapArray(
            BitmapHelper.getBitMapFromResource(
                context.resources,
                R.drawable.flying_dragon
            ), 3, 4, 0.5f
        )
        arrayBitmapResult = arrayOfNulls(3)
        if (arrayBitmapDragon.size == 12) {
            arrayBitmapResult[0] = arrayBitmapDragon[3]
            arrayBitmapResult[1] = arrayBitmapDragon[4]
            arrayBitmapResult[2] = arrayBitmapDragon[5]
            arrayBitmapResult[0]?.let {
                dragon = GameEntity(
                    0f,
                    0f,
                    arrayBitmapResult[0]!!.width.toFloat(),
                    arrayBitmapResult[0]!!.height.toFloat(),
                )
            }
            tubes = mutableListOf()
        }
        tubeTop = BitmapHelper.scaledBitmap(context, BitmapHelper.getBitMapFromResource(context.resources, R.drawable.tube_top), 1f)
        tubeBottom = BitmapHelper.scaledBitmap(context, BitmapHelper.getBitMapFromResource(context.resources, R.drawable.tube_bottom), 1f)
        minTubeOffsetY = GameController.gapBetweenTopAndBottomTubes / 2
        maxTubeOffsetY = GameController.SCREEN_HEIGHT - minTubeOffsetY - dragon.height * 2
        distanceBetweenTubes = GameController.SCREEN_WIDTH * 4 / 5
        repeat(numberOfTubes) { index ->
            val tubeX = GameController.SCREEN_WIDTH + index * distanceBetweenTubes
            val topTubeOffsetY =
                minTubeOffsetY + Random.nextInt((maxTubeOffsetY - minTubeOffsetY + 1).toInt())
            tubes.add(
                Tube(
                    tubeX.toInt(),
                    topTubeOffsetY.toInt(),
                    tubeTop.height,
                    (dragon.height + Random.nextInt(200)+50).toInt()
                )
            )
        }
    }

    init {
        initComponent()
    }

    fun updateAndDrawTubes(canvas: Canvas) {
        if (gameState == GAMESTATE.START) {
            if (tubes[tubeIndexScore].coordinateX < dragon.coordinateX + dragon.with
                && (tubes[tubeIndexScore].getTopTubeY() > dragon.coordinateY || tubes[tubeIndexScore].getBottomTubeY() < dragon.coordinateY + dragon.height)
            ) {
                gameState = GAMESTATE.GAMEOVER
                context.startActivity(Intent(context,MainActivity::class.java))
            }
            if (tubes[tubeIndexScore].coordinateX < dragon.coordinateX - tubeTop.width) {
                score++
                tubeIndexScore++;
                if (tubeIndexScore > numberOfTubes - 1) {
                    tubeIndexScore = 0
                }
            }
            repeat(numberOfTubes) { index ->
                val tube = tubes[index]
                if (tube.coordinateX < -tubeTop.width) {
                    tube.coordinateX =
                        (numberOfTubes * distanceBetweenTubes + tube.coordinateX).roundToInt()
                    val topTubeOffsetY =
                        minTubeOffsetY + Random.nextInt((maxTubeOffsetY - minTubeOffsetY + 1).toInt())
                    tube.topTupOffsetY = topTubeOffsetY.roundToInt()
                }
                tube.coordinateX = tube.coordinateX - tubeVelocity
                canvas.drawBitmap(
                    tubeTop,
                    tube.coordinateX.toFloat(),
                    tube.getTopTubeY().toFloat(),
                    null
                )
                canvas.drawBitmap(
                    tubeBottom,
                    tube.coordinateX.toFloat(),
                    tube.getBottomTubeY().toFloat(),
                    null
                )
            }
            val paint = Paint()
            paint.apply {
                strokeJoin = Paint.Join.ROUND
                strokeCap = Paint.Cap.ROUND
                isAntiAlias = true
                textSize = 100f
                color = ContextCompat.getColor(context, R.color.white)
                textAlign = Paint.Align.LEFT
                flags = Paint.ANTI_ALIAS_FLAG
                strokeWidth = 10f
            }
            Log.d("TAG", "updateAndDrawTubes: $score")
            canvas.drawText("Point: $score", 0f, 100f, paint)
        }
    }

    fun updateAndDrawBackGround(canvas: Canvas) {
        bitmapBackground.coordinateX = bitmapBackground.coordinateX - bitmapBackground.velocityX
        if (bitmapBackground.coordinateX < -bitmapImage.width) {
            bitmapBackground.coordinateX = 0f
        }
        if (bitmapBackground.coordinateX < GameController.SCREEN_WIDTH - bitmapImage.width) {
            canvas.drawBitmap(
                bitmapImage,
                bitmapBackground.coordinateX + bitmapImage.width,
                bitmapBackground.coordinateY,
                null
            )
        }
        canvas.drawBitmap(
            bitmapImage,
            bitmapBackground.coordinateX,
            bitmapBackground.coordinateY,
            null
        )
    }

    fun updateAndDrawPlayer(canvas: Canvas) {
        if (arrayBitmapResult.isEmpty()) return
        var currentFrame = dragon.currentFrame
        if (gameState == GAMESTATE.START) {
            if (dragon.coordinateY < GameController.SCREEN_HEIGHT - dragon.height && dragon.coordinateY > 0f) {
                dragon.velocity = (dragon.velocity + GameController.GRAVITY).toInt()
                dragon.coordinateY = dragon.coordinateY + dragon.velocity
            }
        }
        arrayBitmapResult[currentFrame]?.let {
            canvas.drawBitmap(it, dragon.coordinateX, dragon.coordinateY, null)
        }
        currentFrame++
        if (currentFrame == GameEntity.maxFrame) {
            currentFrame = 0
        }
        dragon.currentFrame = currentFrame
    }


    fun getGameEntity() = dragon

}