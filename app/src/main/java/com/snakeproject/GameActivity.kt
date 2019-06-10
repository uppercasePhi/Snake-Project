package com.snakeproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        snakeGame.deadCallback = ::onDeath
    }

    public fun onDeath() {
        snakeGame.pauseGame()
        finish()
    }

    override fun onResume() {
        super.onResume()
        snakeGame.post {
            snakeGame.resumeGame()
        }
    }

    override fun onPause() {
        super.onPause()
        snakeGame.pauseGame()
    }
}
