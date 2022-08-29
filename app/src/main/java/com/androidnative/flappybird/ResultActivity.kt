package com.androidnative.flappybird

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        startActivity(Intent(this,MainActivity::class.java))
    }
}