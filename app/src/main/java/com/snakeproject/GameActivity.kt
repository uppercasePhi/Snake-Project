package com.snakeproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    var snakeGame: SnakeGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val level = intent.getIntExtra("level", 20)
        var kitNumber = intent.getIntExtra("kit", 1)

        snakeGame = SnakeGame(this, level, kitNumber)

        setContentView(snakeGame)

        snakeGame!!.deadCallback = ::onDeath
    }

    public fun onDeath() {
        snakeGame!!.pauseGame()
        finish()
    }

    override fun onResume() {
        super.onResume()
        snakeGame!!.post {
            snakeGame!!.resumeGame()
        }
    }

    override fun onPause() {
        super.onPause()
        snakeGame!!.pauseGame()
    }
}
