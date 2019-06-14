package com.snakeproject

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_shop.main_menu_button

class Settings : AppCompatActivity() {

    lateinit var eraseConfirm: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val kitNumber = intent.getIntExtra("kit", 0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure? This action is irreversible.")
        builder.setNegativeButton("No") { _: DialogInterface, _: Int -> }
        builder.setPositiveButton("Yes") { _: DialogInterface, _: Int -> scoreManager.eraseScores() }

        eraseConfirm = builder.create()

        main_menu_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("kit", kitNumber)

            startActivity(intent)
        }

        erase_scores.setOnClickListener {
            eraseConfirm.show()
        }

        fun onPlay() {
            val myService = Intent(this, PlayerService::class.java)
            myService.setAction("PLAY")
            startService(myService)
        }

        musicPlay.setOnClickListener {
            onPlay()
        }

        fun onPause() {
            val myService = Intent(this, PlayerService::class.java)
            myService.setAction("PAUSE")
            startService(myService)

        }

        musicStop.setOnClickListener {
          onPause()
        }
    }
}
