package com.snakeproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class GameActivity : AppCompatActivity() {

    private lateinit var snakeGame: SnakeGame
    private lateinit var pause: AlertDialog
    private var kitNumber = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kitNumber = intent.getIntExtra("kit", 0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game paused")
        builder.setNegativeButton("Resume") { _: DialogInterface, _: Int -> onResume() }
        builder.setPositiveButton("Quit") { _: DialogInterface, _: Int -> snakeGame.death() }

        pause = builder.create()

        val level = intent.getIntExtra("level", 20)
        val kitNumber = intent.getIntExtra("kit", 0)

        snakeGame = SnakeGame(this, level, kitNumber)

        setContentView(snakeGame)

        snakeGame.deadCallback = ::onDeath
    }

    override fun onBackPressed() {
        onPause()
        pause.show()
    }

    private fun onDeath() {
        snakeGame.pauseGame()
        val intent = Intent(this, DeathScreenActivity::class.java)
        intent.putExtra("score", snakeGame.score)
        intent.putExtra("kit", kitNumber)
        startActivity(intent)
        //finish()
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
