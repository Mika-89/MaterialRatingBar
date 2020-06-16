/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.widget.TintTypedArray
import me.zhanghai.android.materialratingbar.internal.DrawableCompat
import kotlin.math.roundToInt

// AppCompatRatingBar will add undesired measuring behavior.
@SuppressLint("AppCompatCustomView")
class MaterialRatingBar : RatingBar {
    private val mProgressTintInfo: TintInfo? = TintInfo()
    private var mDrawable: MaterialRatingDrawable? = null
    /**
     * Get the listener that is listening for rating change events.
     *
     * @return The listener, may be null.
     */
    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    var onRatingChangeListener: OnRatingChangeListener? = null
    private var mLastKnownRating = 0f

    constructor(context: Context?) : super(context) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    @SuppressWarnings("RestrictedApi")
    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val context = context
        val typedArray = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.MaterialRatingBar, defStyleAttr, 0)
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_progressTint)) {
            mProgressTintInfo?.mProgressTintList = typedArray.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_progressTint)
            mProgressTintInfo?.mHasProgressTintList = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_progressTintMode)) {
            mProgressTintInfo?.mProgressTintMode = DrawableCompat.parseTintMode(typedArray.getInt(
                    R.styleable.MaterialRatingBar_mrb_progressTintMode, -1), PorterDuff.Mode.SRC_ATOP)
            mProgressTintInfo?.mHasProgressTintMode = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_secondaryProgressTint)) {
            mProgressTintInfo?.mSecondaryProgressTintList = typedArray.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_secondaryProgressTint)
            mProgressTintInfo?.mHasSecondaryProgressTintList = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_secondaryProgressTintMode)) {
            mProgressTintInfo?.mSecondaryProgressTintMode = DrawableCompat.parseTintMode(typedArray.getInt(
                    R.styleable.MaterialRatingBar_mrb_secondaryProgressTintMode, -1), PorterDuff.Mode.SRC_ATOP)
            mProgressTintInfo?.mHasSecondaryProgressTintMode = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_progressBackgroundTint)) {
            mProgressTintInfo?.mProgressBackgroundTintList = typedArray.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_progressBackgroundTint)
            mProgressTintInfo?.mHasProgressBackgroundTintList = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_progressBackgroundTintMode)) {
            mProgressTintInfo?.mProgressBackgroundTintMode = DrawableCompat.parseTintMode(typedArray.getInt(
                    R.styleable.MaterialRatingBar_mrb_progressBackgroundTintMode, -1), PorterDuff.Mode.SRC_ATOP)
            mProgressTintInfo?.mHasProgressBackgroundTintMode = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_indeterminateTint)) {
            mProgressTintInfo?.mIndeterminateTintList = typedArray.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_indeterminateTint)
            mProgressTintInfo?.mHasIndeterminateTintList = true
        }
        if (typedArray.hasValue(R.styleable.MaterialRatingBar_mrb_indeterminateTintMode)) {
            mProgressTintInfo?.mIndeterminateTintMode = DrawableCompat.parseTintMode(typedArray.getInt(
                    R.styleable.MaterialRatingBar_mrb_indeterminateTintMode, -1), PorterDuff.Mode.SRC_ATOP)
            mProgressTintInfo?.mHasIndeterminateTintMode = true
        }
        val fillBackgroundStars = typedArray.getBoolean(
                R.styleable.MaterialRatingBar_mrb_fillBackgroundStars, isIndicator)
        typedArray.recycle()
        mDrawable = MaterialRatingDrawable(getContext(), fillBackgroundStars)
        mDrawable?.setStarCount(numStars)
        progressDrawable = mDrawable
    }

    override fun setNumStars(numStars: Int) {
        super.setNumStars(numStars)
        // mDrawable can be null during super class initialization.
        mDrawable?.setStarCount(numStars)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = (height.times(mDrawable?.tileRatio ?: 0f).times(numStars)).roundToInt()
        setMeasuredDimension(View.resolveSizeAndState(width, widthMeasureSpec, 0), height)
    }

    override fun setProgressDrawable(d: Drawable) {
        super.setProgressDrawable(d)

        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null) {
            applyProgressTints()
        }
    }

    override fun setIndeterminateDrawable(d: Drawable) {
        super.setIndeterminateDrawable(d)

        // mProgressTintInfo can be null during super class initialization.
        if (mProgressTintInfo != null) {
            applyIndeterminateTint()
        }
    }

    @Deprecated("Use {@link #getSupportProgressTintList()} instead.")
    override fun getProgressTintList(): ColorStateList? {
        // Samsung Android 10 might call this in super class constructor.
        if (mProgressTintInfo == null) {
            return null
        }
        logRatingBarTintWarning()
        return supportProgressTintList
    }

    @Deprecated("Use {@link #setSupportProgressTintList(ColorStateList)} instead.")
    override fun setProgressTintList(tint: ColorStateList?) {
        logRatingBarTintWarning()
        supportProgressTintList = tint
    }

    @Deprecated("Use {@link #getSupportProgressTintMode()} instead.")
    override fun getProgressTintMode(): PorterDuff.Mode? {
        logRatingBarTintWarning()
        return supportProgressTintMode
    }

    @Deprecated("Use {@link #setSupportProgressTintMode(PorterDuff.Mode)} instead.")
    override fun setProgressTintMode(tintMode: PorterDuff.Mode?) {
        logRatingBarTintWarning()
        supportProgressTintMode = tintMode
    }

    @Deprecated("Use {@link #getSupportSecondaryProgressTintList()} instead.")
    override fun getSecondaryProgressTintList(): ColorStateList? {
        logRatingBarTintWarning()
        return supportSecondaryProgressTintList
    }

    @Deprecated("Use {@link #setSupportSecondaryProgressTintList(ColorStateList)} instead.")
    override fun setSecondaryProgressTintList(tint: ColorStateList?) {
        logRatingBarTintWarning()
        supportSecondaryProgressTintList = tint
    }

    @Deprecated("Use {@link #getSupportSecondaryProgressTintMode()} instead.")
    override fun getSecondaryProgressTintMode(): PorterDuff.Mode? {
        logRatingBarTintWarning()
        return supportSecondaryProgressTintMode
    }

    @Deprecated("Use {@link #setSupportSecondaryProgressTintMode(PorterDuff.Mode)} instead.")
    override fun setSecondaryProgressTintMode(tintMode: PorterDuff.Mode?) {
        logRatingBarTintWarning()
        supportSecondaryProgressTintMode = tintMode
    }

    @Deprecated("Use {@link #getSupportProgressBackgroundTintList()} instead.")
    override fun getProgressBackgroundTintList(): ColorStateList? {
        logRatingBarTintWarning()
        return supportProgressBackgroundTintList
    }

    @Deprecated("Use {@link #setSupportProgressBackgroundTintList(ColorStateList)} instead.")
    override fun setProgressBackgroundTintList(tint: ColorStateList?) {
        logRatingBarTintWarning()
        supportProgressBackgroundTintList = tint
    }

    @Deprecated("Use {@link #getSupportProgressBackgroundTintMode()} instead.")
    override fun getProgressBackgroundTintMode(): PorterDuff.Mode? {
        logRatingBarTintWarning()
        return supportProgressBackgroundTintMode
    }

    @Deprecated("Use {@link #setSupportProgressBackgroundTintMode(PorterDuff.Mode)} instead.")
    override fun setProgressBackgroundTintMode(tintMode: PorterDuff.Mode?) {
        logRatingBarTintWarning()
        supportProgressBackgroundTintMode = tintMode
    }

    @Deprecated("Use {@link #getSupportIndeterminateTintList()} instead.")
    override fun getIndeterminateTintList(): ColorStateList? {
        logRatingBarTintWarning()
        return supportIndeterminateTintList
    }

    @Deprecated("Use {@link #setSupportIndeterminateTintList(ColorStateList)} instead.")
    override fun setIndeterminateTintList(tint: ColorStateList?) {
        logRatingBarTintWarning()
        supportIndeterminateTintList = tint
    }

    @Deprecated("Use {@link #getSupportIndeterminateTintMode()} instead.")
    override fun getIndeterminateTintMode(): PorterDuff.Mode? {
        logRatingBarTintWarning()
        return supportIndeterminateTintMode
    }

    @Deprecated("Use {@link #setSupportIndeterminateTintMode(PorterDuff.Mode)} instead.")
    override fun setIndeterminateTintMode(tintMode: PorterDuff.Mode?) {
        logRatingBarTintWarning()
        supportIndeterminateTintMode = tintMode
    }

    private fun logRatingBarTintWarning() {
        Log.w(TAG, "Non-support version of tint method called, this is error-prone and will crash" +
                " below Lollipop if you are calling it as a method of RatingBar instead of" +
                " MaterialRatingBar")
    }

    /**
     * @see RatingBar.getProgressTintList
     */
    /**
     * @see RatingBar.setProgressTintList
     */
    var supportProgressTintList: ColorStateList?
        get() = mProgressTintInfo?.mProgressTintList
        set(tint) {
            mProgressTintInfo?.mProgressTintList = tint
            mProgressTintInfo?.mHasProgressTintList = true
            applyPrimaryProgressTint()
        }

    /**
     * @see RatingBar.getProgressTintMode
     */
    /**
     * @see RatingBar.setProgressTintMode
     */
    var supportProgressTintMode: PorterDuff.Mode?
        get() = mProgressTintInfo?.mProgressTintMode
        set(tintMode) {
            mProgressTintInfo?.mProgressTintMode = tintMode
            mProgressTintInfo?.mHasProgressTintMode = true
            applyPrimaryProgressTint()
        }

    /**
     * @see RatingBar.getSecondaryProgressTintList
     */
    /**
     * @see RatingBar.setSecondaryProgressTintList
     */
    var supportSecondaryProgressTintList: ColorStateList?
        get() = mProgressTintInfo?.mSecondaryProgressTintList
        set(tint) {
            mProgressTintInfo?.mSecondaryProgressTintList = tint
            mProgressTintInfo?.mHasSecondaryProgressTintList = true
            applySecondaryProgressTint()
        }

    /**
     * @see RatingBar.getSecondaryProgressTintMode
     */
    /**
     * @see RatingBar.setSecondaryProgressTintMode
     */
    var supportSecondaryProgressTintMode: PorterDuff.Mode?
        get() = mProgressTintInfo?.mSecondaryProgressTintMode
        set(tintMode) {
            mProgressTintInfo?.mSecondaryProgressTintMode = tintMode
            mProgressTintInfo?.mHasSecondaryProgressTintMode = true
            applySecondaryProgressTint()
        }

    /**
     * @see RatingBar.getProgressBackgroundTintList
     */
    /**
     * @see RatingBar.setProgressBackgroundTintList
     */
    var supportProgressBackgroundTintList: ColorStateList?
        get() = mProgressTintInfo?.mProgressBackgroundTintList
        set(tint) {
            mProgressTintInfo?.mProgressBackgroundTintList = tint
            mProgressTintInfo?.mHasProgressBackgroundTintList = true
            applyProgressBackgroundTint()
        }

    /**
     * @see RatingBar.getProgressBackgroundTintMode
     */
    /**
     * @see RatingBar.setProgressBackgroundTintMode
     */
    var supportProgressBackgroundTintMode: PorterDuff.Mode?
        get() = mProgressTintInfo?.mProgressBackgroundTintMode
        set(tintMode) {
            mProgressTintInfo?.mProgressBackgroundTintMode = tintMode
            mProgressTintInfo?.mHasProgressBackgroundTintMode = true
            applyProgressBackgroundTint()
        }

    /**
     * @see RatingBar.getIndeterminateTintList
     */
    /**
     * @see RatingBar.setIndeterminateTintList
     */
    var supportIndeterminateTintList: ColorStateList?
        get() = mProgressTintInfo?.mIndeterminateTintList
        set(tint) {
            mProgressTintInfo?.mIndeterminateTintList = tint
            mProgressTintInfo?.mHasIndeterminateTintList = true
            applyIndeterminateTint()
        }

    /**
     * @see RatingBar.getIndeterminateTintMode
     */
    /**
     * @see RatingBar.setIndeterminateTintMode
     */
    var supportIndeterminateTintMode: PorterDuff.Mode?
        get() = mProgressTintInfo?.mIndeterminateTintMode
        set(tintMode) {
            mProgressTintInfo?.mIndeterminateTintMode = tintMode
            mProgressTintInfo?.mHasIndeterminateTintMode = true
            applyIndeterminateTint()
        }

    private fun applyProgressTints() {
        progressDrawable ?: return
        applyPrimaryProgressTint()
        applyProgressBackgroundTint()
        applySecondaryProgressTint()
    }

    private fun applyPrimaryProgressTint() {
        progressDrawable ?: return
        mProgressTintInfo ?: return

        if (mProgressTintInfo.mHasProgressTintList || mProgressTintInfo.mHasProgressTintMode) {
            val target = getTintTargetFromProgressDrawable(android.R.id.progress, true)
            target?.let {
                applyTintForDrawable(target, mProgressTintInfo.mProgressTintList,
                        mProgressTintInfo.mHasProgressTintList, mProgressTintInfo.mProgressTintMode,
                        mProgressTintInfo.mHasProgressTintMode)
            }
        }
    }

    private fun applySecondaryProgressTint() {
        progressDrawable ?: return
        mProgressTintInfo ?: return

        if (mProgressTintInfo.mHasSecondaryProgressTintList
                || mProgressTintInfo.mHasSecondaryProgressTintMode) {
            val target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress,
                    false)
            target?.let {
                applyTintForDrawable(target, mProgressTintInfo.mSecondaryProgressTintList,
                        mProgressTintInfo.mHasSecondaryProgressTintList,
                        mProgressTintInfo.mSecondaryProgressTintMode,
                        mProgressTintInfo.mHasSecondaryProgressTintMode)
            }
        }
    }

    private fun applyProgressBackgroundTint() {
        progressDrawable ?: return
        mProgressTintInfo ?: return

        if (mProgressTintInfo.mHasProgressBackgroundTintList
                || mProgressTintInfo.mHasProgressBackgroundTintMode) {
            val target = getTintTargetFromProgressDrawable(android.R.id.background, false)
            target?.let {
                applyTintForDrawable(target, mProgressTintInfo.mProgressBackgroundTintList,
                        mProgressTintInfo.mHasProgressBackgroundTintList,
                        mProgressTintInfo.mProgressBackgroundTintMode,
                        mProgressTintInfo.mHasProgressBackgroundTintMode)
            }
        }
    }

    private fun getTintTargetFromProgressDrawable(layerId: Int, shouldFallback: Boolean): Drawable? {
        progressDrawable ?: return null

        progressDrawable.mutate()
        var layerDrawable: Drawable? = null
        if (progressDrawable is LayerDrawable?) {
            layerDrawable = (progressDrawable as LayerDrawable?)?.findDrawableByLayerId(layerId)
        }
        if (layerDrawable == null && shouldFallback) {
            layerDrawable = progressDrawable
        }
        return layerDrawable
    }

    private fun applyIndeterminateTint() {
        indeterminateDrawable ?: return
        mProgressTintInfo ?: return

        if (mProgressTintInfo.mHasIndeterminateTintList
                || mProgressTintInfo.mHasIndeterminateTintMode) {
            indeterminateDrawable.mutate()
            applyTintForDrawable(indeterminateDrawable, mProgressTintInfo.mIndeterminateTintList,
                    mProgressTintInfo.mHasIndeterminateTintList,
                    mProgressTintInfo.mIndeterminateTintMode,
                    mProgressTintInfo.mHasIndeterminateTintMode)
        }
    }

    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private fun applyTintForDrawable(drawable: Drawable?, tintList: ColorStateList?,
                                     hasTintList: Boolean?, tintMode: PorterDuff.Mode?,
                                     hasTintMode: Boolean?) {
        hasTintList ?: return
        hasTintMode ?: return
        if (hasTintList || hasTintMode) {
            if (hasTintList) {
                if (drawable is TintableDrawable?) {
                    (drawable as TintableDrawable?)?.setTintList(tintList)
                } else {
                    logDrawableTintWarning()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable?.setTintList(tintList)
                    }
                }
            }
            if (hasTintMode) {
                if (drawable is TintableDrawable?) {
                    tintMode?.let { (drawable as TintableDrawable?)?.setTintModeLegacy(it) }
                } else {
                    logDrawableTintWarning()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable?.setTintMode(tintMode)
                    }
                }
            }

            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable?.isStateful == true) {
                drawable.state = drawableState
            }
        }
    }

    private fun logDrawableTintWarning() {
        Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below" +
                " Lollipop")
    }

    @Synchronized
    override fun setSecondaryProgress(secondaryProgress: Int) {
        super.setSecondaryProgress(secondaryProgress)

        // HACK: Check and call our listener here because this method is always called by
        // updateSecondaryProgress() from onProgressRefresh().
        val rating = rating
        onRatingChangeListener?.let {
            if (rating != mLastKnownRating) {
                onRatingChangeListener?.onRatingChanged(this, rating)
            }
        }
        mLastKnownRating = rating
    }

    /**
     * A callback that notifies clients when the rating has been changed. This includes changes that
     * were initiated by the user through a touch gesture or arrow key/trackball as well as changes
     * that were initiated programmatically. This callback **will** be called
     * continuously while the user is dragging, different from framework's
     * [OnRatingBarChangeListener].
     */
    interface OnRatingChangeListener {
        /**
         * Notification that the rating has changed. This **will** be called
         * continuously while the user is dragging, different from framework's
         * [OnRatingBarChangeListener].
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating The current rating. This will be in the range 0..numStars.
         */
        fun onRatingChanged(ratingBar: MaterialRatingBar?, rating: Float)
    }

    private class TintInfo {
        var mProgressTintList: ColorStateList? = null
        var mProgressTintMode: PorterDuff.Mode? = null
        var mHasProgressTintList = false
        var mHasProgressTintMode = false
        var mSecondaryProgressTintList: ColorStateList? = null
        var mSecondaryProgressTintMode: PorterDuff.Mode? = null
        var mHasSecondaryProgressTintList = false
        var mHasSecondaryProgressTintMode = false
        var mProgressBackgroundTintList: ColorStateList? = null
        var mProgressBackgroundTintMode: PorterDuff.Mode? = null
        var mHasProgressBackgroundTintList = false
        var mHasProgressBackgroundTintMode = false
        var mIndeterminateTintList: ColorStateList? = null
        var mIndeterminateTintMode: PorterDuff.Mode? = null
        var mHasIndeterminateTintList = false
        var mHasIndeterminateTintMode = false
    }

    companion object {
        private val TAG = MaterialRatingBar::class.java.simpleName
    }
}