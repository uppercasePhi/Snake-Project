package com.snakeproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {

    var snakeGame: SnakeGame? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val level = intent.getIntExtra("level", 20)
        snakeGame = SnakeGame(this, level)

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
