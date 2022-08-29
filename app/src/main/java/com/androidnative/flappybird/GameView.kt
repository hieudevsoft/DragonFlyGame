package com.androidnative.flappybird

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context:Context) : SurfaceView(context),SurfaceHolder.Callback {
    companion object{
        val TAG = "GameView"
    }
    private lateinit var mGameThread:GameThread
    init {
        initView()
    }

    private fun initView() {
        val mHolder = holder
        mHolder.addCallback(this)
        isFocusable = true
        mGameThread = GameThread(mHolder)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated")
        if(!mGameThread.isRunning){
            mGameThread = GameThread(holder)
            mGameThread.start()
        } else{
            mGameThread.start()
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        Log.d(TAG, "surfaceChanged: $p0 $p1 $p2")
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: $p0")
        if(mGameThread.isRunning){
            mGameThread.isRunning = false
            var retry = true
            while (retry){
                try {
                    mGameThread.join()
                    retry = false
                }catch (e:InterruptedException){
                    Log.d(TAG, "surfaceDestroyed: ${e.message}")
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                if(GameEngine.gameState != GameEngine.GAMESTATE.START){
                    GameEngine.gameState = GameEngine.GAMESTATE.START
                }
                GameController.gameEngine.getGameEntity().velocity = GameController.VELOCITY_WHEN_TOUCH.toInt()
            }
        }
        return true
    }
}