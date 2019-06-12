package com.snakeproject

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.media.MediaPlayer;


class PlayerService : Service() {
    lateinit var player: MediaPlayer

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.music)
        player.isLooping = true // Set looping
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("PLAY")) {
                player.start()
            }
            if (intent.getAction().equals("PAUSE")) {
                player.pause()
            }
        }

        return START_STICKY
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    override fun onDestroy() {
        println("service stopped")
        player.stop()
        player.release()
    }

}