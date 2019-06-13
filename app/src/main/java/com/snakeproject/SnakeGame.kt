package com.snakeproject

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import androidx.annotation.MainThread
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.min
import kotlin.random.Random

@SuppressLint("ViewConstructor")
class SnakeGame(context: Context, level: Int, kitNumber: Int) : View(context) {
    private enum class Direction { UP, RIGHT, LEFT, DOWN }

    private val screenSizeInCells = level
    private var tickDuration = 200L

    private var viewWidth = -1
    private var viewHeight = -1
    private var cellSize: Int = -1
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h
        viewWidth = w
        cellSize = min(this.viewWidth, this.viewHeight) / screenSizeInCells
    }

    private var curHeadingDirection = Direction.UP

    private val paint = Paint()

    private var food: Point = Point(0, 0)

    private var superFood: Point = Point(0, 0)
    private var isSuperFoodActive = false
    private var superFoodTimer = 0

    private var snakeLength: Int = 0
    private val maxSnakeLength = screenSizeInCells * screenSizeInCells
    private var snakeXs = Array(maxSnakeLength) { 0 }
    private var snakeYs = Array(maxSnakeLength) { 0 }

    private var score: Int = 0

    lateinit var deadCallback: () -> Unit
    private val tickTimer = Timer()
    private lateinit var timerTask: TimerTask

    private var colorKit = kitNumber * 5

    init {
        startGame()
    }


    private fun startGame() {
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


    fun resumeGame() {
        timerTask = tickTimer.scheduleAtFixedRate(0, tickDuration) {
            if (handler != null) {
                handler.post(::tick)
            }
        }
    }


    fun pauseGame() {
        timerTask.cancel()
    }


    @MainThread
    private fun tick() {
        superFoodControl()
        moveSnake()
        deathCheck()
        if (isSuperFoodActive)
            superFoodCheck()
        foodCheck()
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawRGB(color((0 + colorKit), 0), color((0 + colorKit), 1), color((0 + colorKit), 2))
        paint.setARGB(255, color((1 + colorKit), 0), color((1 + colorKit), 1), color((1 + colorKit), 2))

        canvas.drawRect(
            0f,
            (cellSize * screenSizeInCells).toFloat(),
            (cellSize * screenSizeInCells).toFloat(),
            0f,
            paint
        )

        paint.setARGB(255, color((2 + colorKit), 0), color((2 + colorKit), 1), color((2 + colorKit), 2))
        paint.textSize = 90F
        canvas.drawText("Score: $score", 0f, (cellSize * (screenSizeInCells + 3)).toFloat(), paint)

        paint.setARGB(255, color((4 + colorKit), 0), color((4 + colorKit), 1), color((4 + colorKit), 2))

        for (i in 0 until snakeLength) {
            canvas.drawRect(
                (snakeXs[i] * cellSize).toFloat(),
                (snakeYs[i] * cellSize + cellSize).toFloat(),
                (snakeXs[i] * cellSize + cellSize).toFloat(),
                (snakeYs[i] * cellSize).toFloat(), paint
            )
        }

        paint.setARGB(255, color((3 + colorKit), 0), color((3 + colorKit), 1), color((3 + colorKit), 2))
        canvas.drawRect(
            (food.x * cellSize).toFloat(),
            ((food.y + 1) * cellSize).toFloat(),
            ((food.x + 1) * cellSize).toFloat(),
            (food.y * cellSize).toFloat(), paint
        )

        if (isSuperFoodActive) {
            paint.setARGB(255, color((2 + colorKit), 0), color((2 + colorKit), 1), color((2 + colorKit), 2))
            canvas.drawRect(
                (superFood.x * cellSize).toFloat(),
                ((superFood.y + 1) * cellSize).toFloat(),
                ((superFood.x + 1) * cellSize).toFloat(),
                (superFood.y * cellSize).toFloat(), paint
            )
        }
    }

    // food funs

    private fun spawnFood() {
        food.x = Random.nextInt(screenSizeInCells)
        food.y = Random.nextInt(screenSizeInCells)
    }


    private fun eatFood() {
        score += 10
        snakeLength++
        spawnFood()

    }


    private fun foodCheck() {
        if (snakeXs[0] == food.x && snakeYs[0] == food.y) {
            eatFood()
        }
    }

    // superFood funs

    private fun superFoodCheck() {
        if (snakeXs[0] == superFood.x && snakeYs[0] == superFood.y) {
            eatSuperFood()
        }
    }

    private fun eatSuperFood() {
        score += 50
        snakeLength++
        isSuperFoodActive = false
    }


    private fun spawnSuperFood() {
        if (Random.nextInt(50) == 1) {
            isSuperFoodActive = true
            superFoodTimer = 30
            superFood.x = Random.nextInt(screenSizeInCells)
            superFood.y = Random.nextInt(screenSizeInCells)
        }
    }


    private fun superFoodControl() {
        if (!isSuperFoodActive) {
            spawnSuperFood()
        } else {
            superFoodTimer--
            if (superFoodTimer == 0) {
                isSuperFoodActive = false
            }
        }
    }


    private fun death() {
        scoreManager.addScore(score)
        deadCallback()
    }

    // move and death detection

    private fun moveSnake() {
        for (i in snakeLength downTo 1) {
            snakeXs[i] = snakeXs[i - 1]
            snakeYs[i] = snakeYs[i - 1]
        }

        when (curHeadingDirection) {
            Direction.UP -> snakeYs[0]--
            Direction.DOWN -> snakeYs[0]++
            Direction.LEFT -> snakeXs[0]--
            Direction.RIGHT -> snakeXs[0]++
        }
    }


    private fun deathCheck() {
        if (snakeXs[0] >= screenSizeInCells || snakeYs[0] >= screenSizeInCells ||
            snakeXs[0] < 0 || snakeYs[0] < 0
        ) {
            death()
        }

        if (snakeLength >= 4) {
            for (i in 1 until snakeLength) {
                if (snakeXs[0] == snakeXs[i] && snakeYs[0] == snakeYs[i]) {
                    death()
                }
            }
        }
    }

    fun color(x: Int, y: Int): Int {
        var table: Array<Array<Int>> = Array(20, { Array(3, { 0 }) })
        table[0] = arrayOf(34, 34, 34)
        table[1] = arrayOf(68, 68, 68)
        table[2] = arrayOf(255, 255, 255)
        table[3] = arrayOf(0, 0, 0)
        table[4] = arrayOf(221, 221, 0)

        table[5] = arrayOf(51, 51, 68)
        table[6] = arrayOf(204, 204, 204)
        table[7] = arrayOf(255, 228, 181)
        table[8] = arrayOf(216, 27, 96)
        table[9] = arrayOf(28, 107, 114)

        table[10] = arrayOf(0, 0, 160)
        table[11] = arrayOf(111, 111, 111)
        table[12] = arrayOf(255, 255, 255)
        table[13] = arrayOf(0, 180, 239)
        table[14] = arrayOf(239, 0, 0)

        table[15] = arrayOf(13, 31, 63)
        table[16] = arrayOf(255, 255, 255)
        table[17] = arrayOf(255, 199, 2)
        table[18] = arrayOf(78, 143, 207)
        table[19] = arrayOf(143, 33, 47)

        return table[x][y]
    }
}

