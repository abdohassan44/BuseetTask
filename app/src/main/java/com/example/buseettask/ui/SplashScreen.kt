package com.example.buseettask.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.buseettask.R
import com.example.buseettask.ui.main.MainActivity
import kotlin.concurrent.thread

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter, R.anim.exit)
        setContentView(R.layout.activity_spalsh_screen)
        thread {
            Thread.sleep(2000)
            finish()
            startActivity(Intent(this, MainActivity::class.java))

        }
    }
}