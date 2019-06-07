package com.snakeproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import androidx.annotation.MainThread
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.min
import kotlin.random.Random

class SnakeGame : View {
    companion object {
        const val SCREEN_SIZE_IN_CELLS = 20
        val TICK_DURATION = TimeUnit.SECONDS.toMillis(1)
    }

    private enum class Direction { UP, RIGHT, LEFT, DOWN }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var viewWidth = -1
    private var viewHeight = -1
    private var cellSize: Int = -1

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
        cellSize = min(viewWidth, viewHeight) / SCREEN_SIZE_IN_CELLS
    }

    private var curHeadingDirection = Direction.UP

    val paint = Paint()

    var food: Point = Point(0, 0)

    // длина змейки (удобнее хранить её, чем использовать длину snakePoints)
    private var snakeLength: Int = 0
    private val maxSnakeLength = 200

    var snakeXs = Array(maxSnakeLength) { 0 }
    var snakeYs = Array(maxSnakeLength) { 0 }

    // количество очков игрока
    private var score: Int = 0

    // на паузе ли игра?
    private var isPaused: Boolean = true

    // параметры для регуляции обновления кадров
    private var fps = 1
    private var timeUntilUpdate: Long = 0

    // инициализация оставшихся полей, которые зависят от других и требуют мат. операции


    private fun resetSnake() {
        for (i in 0 until maxSnakeLength) {
            snakeXs[i] = 0
            snakeYs[i] = 0
        }

        snakeLength = 1
        snakeXs[0] = SCREEN_SIZE_IN_CELLS / 2
        snakeYs[0] = SCREEN_SIZE_IN_CELLS / 2
    }

    val tickTimer = Timer()

    public fun startGame() {
        resetSnake()
        isPaused = false
        score = 0
        spawnFood()
        timeUntilUpdate = System.currentTimeMillis()
        tickTimer.scheduleAtFixedRate(0, TICK_DURATION) {
            handler.post(::tick)
        }
    }


    @MainThread
    private fun tick() {
        snakeXs[0] += 1
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLUE)
        paint.color = Color.WHITE

        for (i in 0 until snakeLength) {
            canvas.drawRect(
                (snakeXs[i] * cellSize).toFloat(),
                (snakeYs[i] * cellSize + cellSize).toFloat(),
                (snakeXs[i] * cellSize + cellSize).toFloat(),
                (snakeYs[i] * cellSize).toFloat(), paint
            )
        }

        paint.color = Color.RED


        canvas.drawRect(
            (food.x * cellSize).toFloat(),
            (food.y * cellSize + cellSize).toFloat(),
            (food.x * cellSize + cellSize).toFloat(),
            (food.y * cellSize).toFloat(), paint
        )
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

        when (curHeadingDirection) {
            Direction.UP -> snakeYs[0]++
            Direction.DOWN -> snakeYs[0]--
            Direction.LEFT -> snakeXs[0]--
            Direction.RIGHT -> snakeXs[0]++
        }
    }
}