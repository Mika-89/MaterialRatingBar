/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlin.math.roundToInt

internal class TileDrawable(var drawable: Drawable?) : BaseDrawable() {
    private var mTileCount = -1

    var tileCount: Int
        get() = mTileCount
        set(tileCount) {
            mTileCount = tileCount
            invalidateSelf()
        }

    override fun mutate(): Drawable {
        drawable = drawable?.mutate()
        return this
    }

    override fun onDraw(canvas: Canvas, width: Int, height: Int) {
        drawable?.alpha = mAlpha
        val colorFilter = colorFilterForDrawing
        drawable?.colorFilter = colorFilter
        val tileHeight = drawable?.intrinsicHeight
        val scale = height.toFloat().div(tileHeight ?: 0)
        canvas.scale(scale, scale)
        val scaledWidth = width / scale
        if (mTileCount < 0) {
            val tileWidth = drawable?.intrinsicWidth
            var x = 0
            while (x < scaledWidth) {
                drawable?.setBounds(x, 0, x.plus(tileWidth ?: 0), tileHeight ?: 0)
                drawable?.draw(canvas)
                x += tileWidth ?: 0
            }
        } else {
            val tileWidth = scaledWidth / mTileCount
            for (i in 0 until mTileCount) {
                val drawableWidth = drawable?.intrinsicWidth
                val tileCenter = tileWidth * (i + 0.5f)
                val drawableWidthHalf = drawableWidth?.toFloat()?.div(2)
                drawable?.setBounds((tileCenter.minus(drawableWidthHalf ?: 0f)).roundToInt(), 0,
                        tileCenter.plus(drawableWidthHalf ?: 0f).roundToInt(), tileHeight ?: 0)
                drawable?.draw(canvas)
            }
        }
    }

}