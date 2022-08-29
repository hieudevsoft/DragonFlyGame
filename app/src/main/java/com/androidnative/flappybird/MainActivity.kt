package com.androidnative.flappybird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.coroutineScope
import com.androidnative.flappybird.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        GameController.initialization(this)
        setContentView(binding.root)
        val arrayImageBitmap = BitmapHelper.getBitmapArray(BitmapHelper.getBitMapFromResource(resources,R.drawable.flying_dragon),3,4)
        arrayImageBitmap.let {
            lifecycle.coroutineScope.launch(Dispatchers.Default) {
                var index = 3
                if(arrayImageBitmap.size==12){
                    while (true){
                        delay(100)
                        if(index>5) index=3
                        withContext(Dispatchers.Main){
                            arrayImageBitmap[index]?.let {
                                bitmap->
                                binding.imgDragonFlying.setImageBitmap(bitmap)
                                index+=1
                            }
                        }
                    }
                }
            }
        }
        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this,GameActivity::class.java))
            finish()
        }
    }
}