package com.snakeproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.MainThread
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_game.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.math.min
import kotlin.random.Random
import com.snakeproject.OnSwipeTouchListener

class SnakeGame : View {
    companion object {
        const val SCREEN_SIZE_IN_CELLS = 20
        //val TICK_DURATION = TimeUnit.SECONDS.toMillis(0.2)
        val TICK_DURATION = 200L
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

    public lateinit var deadCallback: () -> Unit



    init {
        resetSnake()
        score = 0
        spawnFood()

        setOnTouchListener(object : OnSwipeTouchListener(this.context) {
            override public fun onSwipeDown() {
                if (curHeadingDirection != Direction.UP) {
                    curHeadingDirection = Direction.DOWN
                }
            }

            override public fun onSwipeLeft() {
                if (curHeadingDirection != Direction.RIGHT) {
                    curHeadingDirection = Direction.LEFT
                }
            }

            override public fun onSwipeRight() {
                if (curHeadingDirection != Direction.LEFT) {
                    curHeadingDirection = Direction.RIGHT
                }
            }

            override public fun onSwipeUp() {
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
        collision()
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
        food.x = Random.nextInt(20)
        food.y = Random.nextInt(20)
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


    fun collision() {
        if (snakeXs[0] == food.x && snakeYs[0] == food.y) {
            eatFood()
        }

        if (snakeXs[0] >= SCREEN_SIZE_IN_CELLS || snakeYs[0] >= SCREEN_SIZE_IN_CELLS ||
            snakeXs[0] < 0 || snakeYs[0] < 0
        ) {
            death()
        }

        for (i in 1 until snakeLength) {
            if (snakeXs[0] == snakeXs[i] && snakeYs[0] == snakeYs[i]) {
                death()
            }
        }
    }
}