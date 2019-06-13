package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pause.*

class Pause : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pause)
        val kitNumber = intent.getIntExtra("kit", 0)

        return_button.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        level_select_button.setOnClickListener {
            val intent = Intent(this, LevelSelect::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }
    }
}
