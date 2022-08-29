package com.androidnative.flappybird

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapHelper {

    fun getBitMapFromResource(res:Resources,id:Int):Bitmap{
        return BitmapFactory.decodeResource(res,id)
    }

    fun getBitmapArray(bitmap: Bitmap,numImageOfWidth:Int,numImageOfHeight:Int,scaled: Float = 1f):Array<Bitmap?>{
        val arrayResult = arrayOfNulls<Bitmap>(numImageOfWidth*numImageOfHeight)
        val widthBitmap = bitmap.width
        val heightBitmap = bitmap.height
        val sizeImageWith = widthBitmap/numImageOfWidth
        val sizeImageHeight = heightBitmap/numImageOfHeight
        var index = 0
        for(i in 0 until numImageOfHeight){
            for(j in 0 until numImageOfWidth){
                val bitmap = Bitmap.createBitmap(bitmap,sizeImageWith*j,sizeImageHeight*i,sizeImageWith,sizeImageHeight)
                arrayResult[index] = Bitmap.createScaledBitmap(bitmap, (bitmap.width*scaled).toInt(), (bitmap.height*scaled).toInt(),false)
                index+=1
            }
        }
        return arrayResult
    }

    fun scaledBitmap(context: Context,bitmap: Bitmap,scaled:Float = 1f):Bitmap{
        val ratio = bitmap.width.toFloat()/bitmap.height
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        GameController.SCREEN_HEIGHT = screenHeight.toFloat()
        GameController.SCREEN_WIDTH = screenWidth.toFloat()
        return Bitmap.createScaledBitmap(bitmap, (screenHeight*scaled*ratio).toInt(), (screenHeight*scaled).toInt(),false)
    }

    fun scaledBitmap(context: Context,bitmap: Bitmap,scaledWidth:Float = 1f,scaleHeight:Float =1f):Bitmap{
        val ratio = bitmap.width.toFloat()/bitmap.height
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        GameController.SCREEN_HEIGHT = screenHeight.toFloat()
        GameController.SCREEN_WIDTH = screenWidth.toFloat()
        return Bitmap.createScaledBitmap(bitmap, (screenHeight*scaledWidth*ratio).toInt(), (screenHeight*scaleHeight).toInt(),false)
    }

}