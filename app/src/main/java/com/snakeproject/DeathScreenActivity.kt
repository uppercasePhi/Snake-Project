package com.snakeproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_death_screen.*

class DeathScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_death_screen)

        val score = intent.getIntExtra("score", 0)
        score_field.text = "$score"

        next_button.setOnClickListener {
            val intent = Intent(this, LeaderBoard::class.java)
            startActivity(intent)
        }
    }
}
