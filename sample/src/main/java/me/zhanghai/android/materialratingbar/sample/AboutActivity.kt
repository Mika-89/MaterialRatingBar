/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    private var mVersionText: TextView? = null
    private var mGithubText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val version = getString(R.string.about_version_format, BuildConfig.VERSION_NAME)

        mVersionText = findViewById(R.id.version)
        mGithubText = findViewById(R.id.github)

        mVersionText?.text = version
        mGithubText?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                AppUtils.navigateUp(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}