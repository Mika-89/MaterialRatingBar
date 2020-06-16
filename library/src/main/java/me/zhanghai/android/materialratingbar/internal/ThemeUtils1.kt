/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar.internal

import android.content.Context

object ThemeUtils {
    fun getColorFromAttrRes(attrRes: Int, context: Context): Int {
        val a = context.obtainStyledAttributes(intArrayOf(attrRes))
        return try {
            a.getColor(0, 0)
        } finally {
            a.recycle()
        }
    }

    fun getFloatFromAttrRes(attrRes: Int, context: Context): Float {
        val a = context.obtainStyledAttributes(intArrayOf(attrRes))
        return try {
            a.getFloat(0, 0f)
        } finally {
            a.recycle()
        }
    }
}