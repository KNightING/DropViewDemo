package com.knighting.dropviewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.SurfaceTexture
import android.util.AttributeSet
import android.view.TextureView

/**
 * Create by KNightING on 2018/12/22
 */

class DropTextureView(private val ctx: Context, val attrs: AttributeSet) : TextureView(ctx, attrs),
    TextureView.SurfaceTextureListener {

    private val dropItemList = mutableListOf<DropItem>()

    private var playing = false

    init {
        val ta = ctx.obtainStyledAttributes(attrs, R.styleable.DropTextureView)
        val quantity = ta.getInteger(R.styleable.DropTextureView_quantity, 20)
        val autoPlay = ta.getBoolean(R.styleable.DropTextureView_auto, false)
        if (autoPlay) playing = false
        (1..quantity).forEach {
            dropItemList.add(DropItem(ta))
        }
        ta.recycle()

        isOpaque = false
        surfaceTextureListener = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        dropItemList.forEach {
            it.init(w, h)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        //call when TextureView size changed.
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        //call when TextureView update (when call the unlockCanvasAndPost)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        //call when TextureView destroyed, should be stop to draw.
        end()
        return true
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        //call when TextureView init, and app back to background than TextureView will init again.
        start()
    }

    fun start() {
        if (playing) return
        playing = true
        playThread.start()
    }

    fun end() {
        if (!playing) return
        playing = false
        playThread.interrupt()
        playThread.join()
    }

    private val playThread = Thread {
        var canvas: Canvas? = null
        try {
            while (playing) {
                draw()
            }

            canvas = lockCanvas()
            canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        } catch (ex: Exception) {

        } finally {
            if (canvas != null)
                unlockCanvasAndPost(canvas)
        }
    }

    fun draw() {
        lockCanvas()?.run {
            drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            dropItemList.forEach { it.draw(this) }
            unlockCanvasAndPost(this)
        }
    }
}