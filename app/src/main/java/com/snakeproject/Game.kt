package com.snakeproject

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import kotlinx.android.synthetic.main.activity_game.*


class Game : AppCompatActivity() {

    var snakeGame : SnakeGame? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_game)

        val display: Display = windowManager.defaultDisplay
        val size = Point()

        display.getSize(size)

        snakeGame = SnakeGame(this, size)

        //snakeGame = SnakeGame(this, size)

        setContentView(snakeGame)
    }
}
