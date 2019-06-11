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

class SnakeGame : View {
    companion object {
        private const val SCREEN_SIZE_IN_CELLS = 20
        private var TICK_DURATION = 200L
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

    private val paint = Paint()

    private var food: Point = Point(0, 0)

    private var superFood: Point = Point(0, 0)
    private var isSuperFoodActive = false
    private var superFoodTimer = 0

    private var snakeLength: Int = 0
    private val maxSnakeLength = SCREEN_SIZE_IN_CELLS * SCREEN_SIZE_IN_CELLS
    private var snakeXs = Array(maxSnakeLength) { 0 }
    private var snakeYs = Array(maxSnakeLength) { 0 }

    private var score: Int = 0

    lateinit var deadCallback: () -> Unit
    private val tickTimer = Timer()
    private lateinit var timerTask: TimerTask


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

        snakeLength = 1
        snakeXs[0] = SCREEN_SIZE_IN_CELLS / 2
        snakeYs[0] = SCREEN_SIZE_IN_CELLS / 2
    }


    fun resumeGame() {
        timerTask = tickTimer.scheduleAtFixedRate(0, TICK_DURATION) {
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
        canvas.drawColor(Color.WHITE)
        paint.color = Color.LTGRAY

        canvas.drawRect(
            0f,
            (cellSize * SCREEN_SIZE_IN_CELLS).toFloat(),
            (cellSize * SCREEN_SIZE_IN_CELLS).toFloat(),
            0f,
            paint
        )

        paint.color = Color.BLACK
        paint.textSize = 90F
        canvas.drawText("Score: $score", 0f, (cellSize * (SCREEN_SIZE_IN_CELLS + 3)).toFloat(), paint)

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
            ((food.y + 1) * cellSize).toFloat(),
            ((food.x + 1) * cellSize).toFloat(),
            (food.y * cellSize).toFloat(), paint
        )

        if (isSuperFoodActive) {
            paint.color = Color.MAGENTA
            canvas.drawRect(
                (superFood.x * cellSize).toFloat(),
                ((superFood.y + 1) * cellSize).toFloat(),
                ((superFood.x + 1) * cellSize).toFloat(),
                (superFood.y * cellSize).toFloat(), paint
            )
        }
    }


    private fun spawnFood() {
        food.x = Random.nextInt(SCREEN_SIZE_IN_CELLS)
        food.y = Random.nextInt(SCREEN_SIZE_IN_CELLS)
    }


    private fun eatFood() {
        score += 10
        snakeLength++
        spawnFood()

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
            superFood.x = Random.nextInt(SCREEN_SIZE_IN_CELLS)
            superFood.y = Random.nextInt(SCREEN_SIZE_IN_CELLS)
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
        deadCallback()
    }


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
        if (snakeXs[0] >= SCREEN_SIZE_IN_CELLS || snakeYs[0] >= SCREEN_SIZE_IN_CELLS ||
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


    private fun foodCheck() {
        if (snakeXs[0] == food.x && snakeYs[0] == food.y) {
            eatFood()
        }
    }

    private fun superFoodCheck() {
        if (snakeXs[0] == superFood.x && snakeYs[0] == superFood.y) {
            eatSuperFood()
        }
    }
}