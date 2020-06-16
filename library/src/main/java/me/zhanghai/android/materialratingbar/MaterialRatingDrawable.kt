/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import me.zhanghai.android.materialratingbar.internal.ThemeUtils

class MaterialRatingDrawable(context: Context, fillBackgroundStars: Boolean) : LayerDrawable(arrayOf(
        createLayerDrawableWithTintAttrRes(if (fillBackgroundStars) R.drawable.mrb_star_icon_black_36dp else R.drawable.mrb_star_border_icon_black_36dp, if (fillBackgroundStars) R.attr.colorControlHighlight else R.attr.colorControlNormal, context),
        if (fillBackgroundStars) createClippedLayerDrawableWithTintColor(
                R.drawable.mrb_star_icon_black_36dp, Color.TRANSPARENT, context) else createClippedLayerDrawableWithTintAttrRes(
                R.drawable.mrb_star_border_icon_black_36dp, R.attr.colorControlActivated,
                context),
        createClippedLayerDrawableWithTintAttrRes(R.drawable.mrb_star_icon_black_36dp,
                R.attr.colorControlActivated, context)
)) {
    val tileRatio: Float
        get() {
            val drawable = getTileDrawableByLayerId(android.R.id.progress)?.drawable
            return drawable?.intrinsicWidth?.toFloat()?.div(drawable.intrinsicHeight) ?: 0f
        }

    fun setStarCount(count: Int) {
        getTileDrawableByLayerId(android.R.id.background)?.tileCount = count
        getTileDrawableByLayerId(android.R.id.secondaryProgress)?.tileCount = count
        getTileDrawableByLayerId(android.R.id.progress)?.tileCount = count
    }

    @SuppressLint("NewApi")
    private fun getTileDrawableByLayerId(id: Int): TileDrawable? {
        val layerDrawable = findDrawableByLayerId(id)
        return when (id) {
            android.R.id.background -> layerDrawable as TileDrawable?
            android.R.id.secondaryProgress, android.R.id.progress -> {
                val clipDrawable = layerDrawable as ClipDrawableCompat?
                clipDrawable?.drawable as TileDrawable?
            }
            else -> throw RuntimeException()
        }
    }

    companion object {
        private fun createLayerDrawableWithTintColor(tileRes: Int, tintColor: Int,
                                                     context: Context): Drawable? {
            val drawable = TileDrawable(AppCompatResources.getDrawable(context,
                    tileRes))
            drawable.mutate()
            (drawable as TintableDrawable).setTint(tintColor)
            return drawable
        }

        private fun createLayerDrawableWithTintAttrRes(tileRes: Int, tintAttrRes: Int,
                                                       context: Context): Drawable? {
            val tintColor = ThemeUtils.getColorFromAttrRes(tintAttrRes, context)
            return createLayerDrawableWithTintColor(tileRes, tintColor, context)
        }

        @SuppressLint("RtlHardcoded")
        private fun createClippedLayerDrawableWithTintColor(tileResId: Int, tintColor: Int,
                                                            context: Context): Drawable? {
            return ClipDrawableCompat(createLayerDrawableWithTintColor(tileResId, tintColor,
                    context), Gravity.LEFT, ClipDrawable.HORIZONTAL)
        }

        @SuppressLint("RtlHardcoded")
        private fun createClippedLayerDrawableWithTintAttrRes(tileResId: Int,
                                                              tintAttrRes: Int,
                                                              context: Context): Drawable? {
            return ClipDrawableCompat(createLayerDrawableWithTintAttrRes(tileResId, tintAttrRes,
                    context), Gravity.LEFT, ClipDrawable.HORIZONTAL)
        }

        @SuppressLint("RtlHardcoded")
        private fun createClippedTransparentLayerDrawable(): Drawable {
            return ClipDrawableCompat(TileDrawable(ColorDrawable(Color.TRANSPARENT)),
                    Gravity.LEFT, ClipDrawable.HORIZONTAL)
        }
    }

    init {
        setId(0, android.R.id.background)
        setId(1, android.R.id.secondaryProgress)
        setId(2, android.R.id.progress)
    }
}