package com.snakeproject

import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min

object scoreManager {
    val file = File(ArchLifecycleApp.ctx.filesDir, "scores")


    @Synchronized
    fun addScore(score: Int) {
        FileOutputStream(file, true).bufferedWriter().use {
            it.appendln(score.toString())
        }
    }


    fun getTopScores(top: Int): List<Int> {
        if (!file.exists()) {
            return emptyList()
        }
        val sc = Scanner(file.bufferedReader())
        val l = ArrayList<Int>()
        while (sc.hasNext()) {
            l.add(sc.nextInt())
        }

        l.sort()
        l.reverse()

        return l.subList(0, min(l.size, top))
    }

}