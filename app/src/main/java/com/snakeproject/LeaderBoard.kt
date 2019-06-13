package com.snakeproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_leader_board.*

class LeaderBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)
        val kitNumber = intent.getIntExtra("kit", 0)

        main_menu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }
    }
}
