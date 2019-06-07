package com.snakeproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    var snakeGame : SnakeGame? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }
}
