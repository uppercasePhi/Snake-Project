package com.snakeproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.min
import kotlin.random.Random
import kotlin.system.exitProcess

class SnakeGame1(context : Context, screenSizeInPixels : Point) : SurfaceView(context) {

    // поля и инициализация некоторых из них

    private var thread : Thread? = null

    // enum с возможными направлениями змейки
    private enum class Heading {UP, RIGHT, LEFT, DOWN}

    // нынешнее направление змейки
    private var curHeading = Heading.UP

    // размер экрана в пикселях

    // координаты еды
    var food : Point = Point(0, 0)

    var cellSize : Int

    private val screenSizeInCells = Point(20, 20) // TODO: сделать возможность менять х и y в зависимости от уровня

    // длина змейки (удобнее хранить её, чем использовать длину snakePoints)
    private var snakeLength : Int = 1
    private val maxSnakeLength = 200

    // массив Point-ов с координатами змейки
    var snakePoints = Array<Point>(maxSnakeLength){Point(0,0)}

    // количество очков игрока
    private var score : Int = 0

    // на паузе ли игра?
    private var isPaused : Boolean = false

    // всякие объекты для рисования
    private var canvas : Canvas? = null
    private val surfaceHolder : SurfaceHolder = holder
    private val paint : Paint = Paint()

    // инициализация оставшихся полей, которые зависят от других и требуют мат. операции

    init {
        cellSize = min(screenSizeInPixels.x, screenSizeInPixels.y) / screenSizeInCells.x
        newGame()
    }

    // методы для подготовки объекта для новой игры

    fun resetSnake()
    {
        for(i in 0 until maxSnakeLength) {
            snakePoints[i].x = 0
            snakePoints[i].y = 0
        }

        snakeLength = 1
        snakePoints[0].x = screenSizeInCells.x / 2
        snakePoints[0].y = screenSizeInCells.y / 2
    }


    fun newGame() {
        Log.d("TEST", "1")
        resetSnake()
        Log.d("TEST", "2")
        //score = 0
        Log.d("TEST", "3")
        spawnFood()
        Log.d("TEST", "4")
    }

    // прорисовка кадра

    fun draw() {
        Log.d("TEST", "draw")

        canvas = Canvas()

        if(canvas == null)
            exitProcess(-136)

        canvas!!.drawColor(Color.BLUE)
        paint.color = Color.WHITE

        paint.textSize = (90).toFloat() // TODO: сделать это не так тупо

        Log.d("TEST", "draw1")

        for(point in snakePoints) {
            canvas!!.drawRect((point.x * cellSize).toFloat(),
                              (point.y * cellSize + cellSize).toFloat(),
                              (point.x * cellSize + cellSize).toFloat(),
                              (point.y * cellSize).toFloat(), paint)
        }

        paint.color = Color.RED

        Log.d("TEST", "draw2")

        canvas!!.drawRect((food.x * cellSize).toFloat(),
            (food.y * cellSize + cellSize).toFloat(),
            (food.x * cellSize + cellSize).toFloat(),
            (food.y * cellSize).toFloat(), paint)

        //surfaceHolder.unlockCanvasAndPost(canvas)

        Log.d("TEST", "drawfinal")
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
            snakePoints[i].x = snakePoints[i - 1].x
            snakePoints[i].y = snakePoints[i - 1].y
        }

        // ставим голове координаты в зависимости от направления

        when(curHeading) {
            Heading.UP -> snakePoints[0].y++
            Heading.DOWN -> snakePoints[0].y--
            Heading.LEFT -> snakePoints[0].x--
            Heading.RIGHT -> snakePoints[0].x++
        }
    }

}