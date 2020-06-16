/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar.sample

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private var mDecimalRatingBars = ArrayList<RatingBar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mDecimalRatingBars.add(findViewById(R.id.framework_decimal_ratingbar))
        mDecimalRatingBars.add(findViewById(R.id.library_decimal_ratingbar))
        mDecimalRatingBars.add(findViewById(R.id.library_tinted_decimal_ratingbar))

        mDecimalRatingBars[0].startAnimation(RatingAnimation())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class RatingAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            val max: Int = mDecimalRatingBars[0].max
            val progress: Int = (interpolatedTime.times(max).roundToInt())
            mDecimalRatingBars.forEach {
                it.progress = progress
            }
        }

        override fun willChangeTransformationMatrix(): Boolean {
            return false
        }

        override fun willChangeBounds(): Boolean {
            return false
        }

        init {
            duration = (mDecimalRatingBars[0].numStars.times(4).times(resources.getInteger(android.R.integer.config_longAnimTime)).toLong())

            interpolator = LinearInterpolator()
            repeatCount = INFINITE
        }
    }
}