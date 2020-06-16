/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.util.Log

internal class ClipDrawableCompat(private val mDrawable: Drawable?, gravity: Int, orientation: Int) : ClipDrawable(mDrawable, gravity, orientation), TintableDrawable {
    private val mConstantState = DummyConstantState()
    override fun getDrawable(): Drawable? {
        return mDrawable
    }

    override fun setTint(tintColor: Int) {
        if (mDrawable is TintableDrawable?) {
            (mDrawable as TintableDrawable?)?.setTint(tintColor)
        } else {
            Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below" +
                    " Lollipop")
            super.setTint(tintColor)
        }
    }

    override fun setTintList(tint: ColorStateList?) {
        if (mDrawable is TintableDrawable?) {
            (mDrawable as TintableDrawable?)?.setTintList(tint)
        } else {
            Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below" +
                    " Lollipop")
            super.setTintList(tint)
        }
    }

    override fun setTintModeLegacy(tintMode: PorterDuff.Mode) {
        if (mDrawable is TintableDrawable?) {
            (mDrawable as TintableDrawable?)?.setTintModeLegacy(tintMode)
        } else {
            Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below" +
                    " Lollipop")
        }
    }

    // Workaround LayerDrawable.ChildDrawable which calls getConstantState().newDrawable()
    // without checking for null.
    // We are never inflated from XML so the protocol of ConstantState does not apply to us. In
    // order to make LayerDrawable happy, we return ourselves from DummyConstantState.newDrawable().
    override fun getConstantState(): ConstantState? {
        return mConstantState
    }

    private inner class DummyConstantState : ConstantState() {
        override fun getChangingConfigurations(): Int {
            return 0
        }

        override fun newDrawable(): Drawable {
            return this@ClipDrawableCompat
        }
    }

    companion object {
        private val TAG = ClipDrawableCompat::class.java.simpleName
    }

}