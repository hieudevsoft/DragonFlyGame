package com.androidnative.flappybird

import android.os.SystemClock
import android.util.Log
import android.view.SurfaceHolder

class GameThread(private val holder:SurfaceHolder):Thread() {
    @Volatile
    var isRunning = false
    private var startTime = 0L
    private var loopTime = 0L
    private val FPS = 60

    init {
        isRunning = true
    }

    override fun run() {
        while (isRunning){
            startTime = SystemClock.uptimeMillis()
            val canvas = holder.lockCanvas(null)
            if(canvas!=null){
                synchronized(holder){
                        GameController.gameEngine.updateAndDrawBackGround(canvas)
                        GameController.gameEngine.updateAndDrawPlayer(canvas)
                        GameController.gameEngine.updateAndDrawTubes(canvas)
                        holder.unlockCanvasAndPost(canvas)
                }
            }
            loopTime = SystemClock.uptimeMillis()-startTime
            if(loopTime<FPS){
                try {
                    sleep(FPS-loopTime)
                }catch (e:Exception){
                    Log.d(GameView.TAG, "run: ${e.message}")
                }
            }
            Log.d(GameView.TAG, "running... ")
        }
    }
}