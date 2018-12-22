package com.knighting.dropviewdemo

import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.*

/**
 * Create by KNightING on 2018/12/22
 */

class DropItem(val ta: TypedArray) {

    private var parentWidth = 0
    private var parentHeight = 0

    private var x = 0f
    private var y = 0f

    private val random = Random()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = Color.WHITE
        paint.alpha = random.nextInt(255) + 230
        paint.strokeWidth = 1.8f
    }

    fun init(w: Int, h: Int) {
        parentWidth = w
        parentHeight = h
        initPosition()
    }

    private fun initPosition() {
        x = (random.nextInt(parentWidth + 1) - 1).toFloat()
        y = (random.nextInt(parentHeight + 1) - parentHeight).toFloat()
    }

    fun draw(canvas: Canvas) {
        canvas.run {
            drawCircle(x, y, 20f, paint)
            y += 0.5f
        }

        if (x > parentWidth || y > parentHeight)
            initPosition()
    }

}