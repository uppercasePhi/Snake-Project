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
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.min
import kotlin.random.Random

class SnakeGame(context: Context, level: Int) : View(context) {
    companion object {
        //val TICK_DURATION = TimeUnit.SECONDS.toMillis(0.2)
    }

    private enum class Direction { UP, RIGHT, LEFT, DOWN }

    //constructor(context: Context?) : super(context)
    //constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    //constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val screenSizeInCells = level
    private var TICK_DURATION = 200L

    private var viewWidth = -1
    private var viewHeight = -1
    private var cellSize: Int = -1

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
        cellSize = min(viewWidth, viewHeight) / screenSizeInCells
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

    public lateinit var deadCallback: () -> Unit


    init {
        resetSnake()
        score = 0
        spawnFood()

        setOnTouchListener(object : OnSwipeTouchListener(this.context) {
            override fun onSwipeDown() {
                if (curHeadingDirection != Direction.UP) {
                    curHeadingDirection = Direction.DOWN
                }
            }

            override fun onSwipeLeft() {
                if (curHeadingDirection != Direction.RIGHT) {
                    curHeadingDirection = Direction.LEFT
                }
            }

            override fun onSwipeRight() {
                if (curHeadingDirection != Direction.LEFT) {
                    curHeadingDirection = Direction.RIGHT
                }
            }

            override fun onSwipeUp() {
                if (curHeadingDirection != Direction.DOWN) {
                    curHeadingDirection = Direction.UP
                }
            }
        })
    }


    private fun resetSnake() {
        for (i in 0 until maxSnakeLength) {
            snakeXs[i] = 0
            snakeYs[i] = 0
        }

        snakeLength = 3
        snakeXs[0] = screenSizeInCells / 2
        snakeYs[0] = screenSizeInCells / 2
    }

    val tickTimer = Timer()
    lateinit var timerTask: TimerTask


    public fun resumeGame() {
        timerTask = tickTimer.scheduleAtFixedRate(0, TICK_DURATION) {
            if (handler != null) {
                handler.post(::tick)
            }
        }
    }

    public fun pauseGame() {
        timerTask.cancel()
    }


    @MainThread
    private fun tick() {
        moveSnake()
        checkDeath()
        foodCheck()
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        paint.color = Color.LTGRAY

        canvas.drawRect(
            0f,
            (cellSize * screenSizeInCells).toFloat(),
            (cellSize * screenSizeInCells).toFloat(),
            0f,
            paint
        )

        paint.color = Color.BLACK
        paint.textSize = 90F
        canvas.drawText("Score: $score", 0f, (cellSize * (screenSizeInCells + 3)).toFloat(), paint)

        paint.color = Color.BLUE

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
    }

    // методы для спавна и обработки поедания еды

    fun spawnFood() {
        food.x = Random.nextInt(screenSizeInCells)
        food.y = Random.nextInt(screenSizeInCells)
    }


    fun eatFood() {
        score += 10
        snakeLength++
        spawnFood()

    }


    fun death() {
        deadCallback()
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
            Direction.UP -> snakeYs[0]--
            Direction.DOWN -> snakeYs[0]++
            Direction.LEFT -> snakeXs[0]--
            Direction.RIGHT -> snakeXs[0]++
        }
    }


    fun checkDeath() {
        if (snakeXs[0] >= screenSizeInCells || snakeYs[0] >= screenSizeInCells ||
            snakeXs[0] < 0 || snakeYs[0] < 0
        ) {
            death()
        }

        if (snakeLength >= 4)
            for (i in 3 until snakeLength) {
                if (snakeXs[0] == snakeXs[i] && snakeYs[0] == snakeYs[i]) {
                    death()
                }
            }
    }


    fun foodCheck() {
        if (snakeXs[0] == food.x && snakeYs[0] == food.y) {
            eatFood()
        }
    }
}