package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_level_select.*

class LevelSelect : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level_select)
        val kitNumber = intent.getIntExtra("kit", 0)

        val tag = "level"

        start_button_10.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(tag, 10)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        start_button_15.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(tag, 15)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        start_button_20.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(tag, 20)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }
    }
}
