package com.snakeproject

import android.app.Application
import android.content.Intent
import androidx.lifecycle.*

class MyObserver : LifecycleObserver

class ArchLifecycleApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        println("OUT")
        val myService = Intent(this, PlayerService::class.java)
        myService.setAction("PAUSE")
        startService(myService)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        println("IN")

    }

}