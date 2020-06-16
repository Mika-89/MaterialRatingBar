/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/**
 * A `Drawable` that is tintable.
 */
interface TintableDrawable {
    /**
     * Specifies tint color for this drawable.
     *
     *
     * A Drawable's drawing content will be blended together with its tint
     * before it is drawn to the screen. This functions similarly to
     * [Drawable.setColorFilter].
     *
     *
     *
     * To clear the tint, pass `null` to
     * [.setTintList].
     *
     *
     * **Note:** Setting a color filter via
     * [Drawable.setColorFilter] or
     * [Drawable.setColorFilter] overrides tint.
     *
     *
     * @param tintColor Color to use for tinting this drawable
     * @see .setTintList
     * @see .setTintMode
     */
    fun setTint(@ColorInt tintColor: Int)

    /**
     * Specifies tint color for this drawable as a color state list.
     *
     *
     * A Drawable's drawing content will be blended together with its tint
     * before it is drawn to the screen. This functions similarly to
     * [Drawable.setColorFilter].
     *
     *
     * **Note:** Setting a color filter via
     * [Drawable.setColorFilter] or
     * [Drawable.setColorFilter] overrides tint.
     *
     *
     * @param tint Color state list to use for tinting this drawable, or
     * `null` to clear the tint
     * @see .setTint
     * @see .setTintMode
     */
    fun setTintList(tint: ColorStateList?)

    /**
     * Specifies a tint blending mode for this drawable.
     *
     *
     * Defines how this drawable's tint color should be blended into the drawable
     * before it is drawn to screen. Default tint mode is [PorterDuff.Mode.SRC_IN].
     *
     *
     * **Note:** Setting a color filter via
     * [Drawable.setColorFilter] or
     * [Drawable.setColorFilter] overrides tint.
     *
     *
     * @param tintMode A Porter-Duff blending mode
     * @see .setTint
     * @see .setTintList
     */
    fun setTintModeLegacy(tintMode: PorterDuff.Mode)
}