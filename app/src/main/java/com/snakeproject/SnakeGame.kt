package com.snakeproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

class SnakeGame(context : Context, screenSizeInPixels : Point) : View(context) {
    // поля и инициализация некоторых из них

    // enum с возможными направлениями змейки
    private enum class Heading {UP, RIGHT, LEFT, DOWN}

    // нынешнее направление змейки
    private var curHeading = Heading.UP

    var canvas = Canvas()
    val paint = Paint()


    // координаты еды
    var food : Point = Point(0, 0)

    // размер ячейки
    var cellSize : Int

    private val screenSizeInCells = Point(20, 20) // TODO: сделать возможность менять х и y в зависимости от уровня

    // длина змейки (удобнее хранить её, чем использовать длину snakePoints)
    private var snakeLength : Int = 0
    private val maxSnakeLength = 200

    // массив Point-ов с координатами змейки
    //var snakePoints = Array<Point>(maxSnakeLength){Point(0,0)}
    var snakeXs : Array<Int> = Array<Int>(maxSnakeLength){0}
    var snakeYs : Array<Int> = Array<Int>(maxSnakeLength){0}

    // количество очков игрока
    private var score : Int = 0

    // на паузе ли игра?
    private var isPaused : Boolean = true

    // параметры для регуляции обновления кадров
    private var fps = 1
    private var timeUntilUpdate : Long = 0
    private val MILLIS_PER_SEC = 1000

    // инициализация оставшихся полей, которые зависят от других и требуют мат. операции

    init {
        cellSize = min(screenSizeInPixels.x, screenSizeInPixels.y) / screenSizeInCells.x
        newGame()
    }


    fun resetSnake()
    {
        for(i in 0 until maxSnakeLength) {
            snakeXs[i] = 0
            snakeYs[i] = 0
        }

        snakeLength = 1
        snakeXs[0] = screenSizeInCells.x / 2
        snakeYs[0] = screenSizeInCells.y / 2
    }


    fun newGame() {
        resetSnake()
        isPaused = false
        score = 0
        spawnFood()
        timeUntilUpdate = System.currentTimeMillis()
        run()

    }


//    fun run() {
//        draw(canvas)
//    }


    fun run() {
        draw(canvas)
        GlobalScope.launch {
            while (!isPaused) {
                if (isUpdateRequired()) {
                    GlobalScope.launch {
                        Log.d("TEST", "UPDATE")
                        //moveSnake()
                        snakeXs[0]++
                        draw(canvas)
                        //canvas.drawColor(Color.YELLOW)
                        //invalidate()
                        //super.draw(canvas)
                    }
                }
            }
        }
    }


    fun isUpdateRequired() : Boolean {
        if(System.currentTimeMillis() >= timeUntilUpdate) {
            timeUntilUpdate += MILLIS_PER_SEC / fps
            return true
        }

        return false
    }


    var switch : Boolean = true

    override fun onDraw(canvas: Canvas) {
        Log.d("TEST", "draw")

        canvas.drawColor(Color.BLUE)
        paint.color = Color.WHITE

        for(i in 0 until snakeLength) {
            canvas.drawRect((snakeXs[i] * cellSize).toFloat(),
                (snakeYs[i] * cellSize + cellSize).toFloat(),
                (snakeXs[i] * cellSize + cellSize).toFloat(),
                (snakeYs[i] * cellSize).toFloat(), paint)
        }

        paint.color = Color.RED


        canvas.drawRect((food.x * cellSize).toFloat(),
            (food.y * cellSize + cellSize).toFloat(),
            (food.x * cellSize + cellSize).toFloat(),
            (food.y * cellSize).toFloat(), paint)

        Log.d("TEST", "drawfinal")
        //invalidate()
    }

    // методы для спавна и обработки поедания еды

    fun spawnFood() {
        food.x = Random.nextInt()
        food.y = Random.nextInt()
    }


    fun eatFood() {
        score += 10
        snakeLength++

    }

    // изменение точек змейки

    fun moveSnake() {

        // ставим всем точкам змейки кроме головы координаты следующей точки

        for (i in snakeLength downTo 1) {
            snakeXs[i] = snakeXs[i - 1]
            snakeYs[i] = snakeYs[i - 1]
        }

        // ставим голове координаты в зависимости от направления

        when(curHeading) {
            Heading.UP -> snakeYs[0]++
            Heading.DOWN -> snakeYs[0]--
            Heading.LEFT -> snakeXs[0]--
            Heading.RIGHT -> snakeXs[0]++
        }
    }
}