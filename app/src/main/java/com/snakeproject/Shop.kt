package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_shop.*

class Shop : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        val kitNumber = intent.getIntExtra("kit", 0)

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        theme_of_game_one.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", 0)

            startActivity(intent)
        }

        theme_of_game_two.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", 1)

            startActivity(intent)
        }

        theme_of_game_three.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", 2)

            startActivity(intent)
        }

        theme_of_game_four.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", 3)

            startActivity(intent)
        }
    }
}
