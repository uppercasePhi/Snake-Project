package com.snakeproject

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner

class MyObserver : LifecycleObserver

class ArchLifecycleApp : Application(), LifecycleObserver {

    companion object {
        @SuppressLint("StaticFieldLeak")
        public lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
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