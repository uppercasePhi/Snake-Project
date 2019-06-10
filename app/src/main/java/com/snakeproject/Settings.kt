package com.snakeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer;
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.activity_shop.main_menu_button

class Settings : AppCompatActivity() {
    lateinit var mySong : MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mySong = MediaPlayer.create(this, R.raw.ooo);

        musicon.setOnClickListener {
            mySong.start()
            mySong.isLooping = true
        }

        musicoff.setOnClickListener {
            mySong.pause()
        }

    }
}
