package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val kitNumber = intent.getIntExtra("kit", 0)

        play_button.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        shop_button.setOnClickListener {
            val intent = Intent(this, Shop::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        settings_button.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        records_button.setOnClickListener {
            val intent = Intent(this, LeaderBoard::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }
    }
}
