package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_shop.main_menu_button

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val kitNumber = intent.getIntExtra("kit", 0)

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        fun onPlay() {
            val myService = Intent(this, PlayerService::class.java)
            myService.setAction("PLAY")
            startService(myService)
        }

        musicPlay.setOnClickListener {
            onPlay()
        }

        fun onPause() {
            val myService = Intent(this, PlayerService::class.java)
            myService.setAction("PAUSE")
            startService(myService)

        }

        musicStop.setOnClickListener {
          onPause()
        }
    }
}
