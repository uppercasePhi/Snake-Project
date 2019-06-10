package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pause.*
import kotlinx.android.synthetic.main.activity_pause.main_menu_button
import kotlinx.android.synthetic.main.activity_shop.*

class Pause : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)

        return_button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        level_select_button.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            startActivity(intent)
        }

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }
}
