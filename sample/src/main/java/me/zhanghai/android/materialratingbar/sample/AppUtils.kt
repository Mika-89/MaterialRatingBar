/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */
package me.zhanghai.android.materialratingbar.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder

object AppUtils {
    fun getPackageInfo(context: Context): PackageInfo {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            // Should never happen.
            throw RuntimeException(e)
        }
    }

    // From http://developer.android.com/training/implementing-navigation/ancestral.html#NavigateUp .
    @JvmOverloads
    fun navigateUp(activity: Activity?, extras: Bundle? = null) {
        activity ?: return
        val upIntent = NavUtils.getParentActivityIntent(activity)
        if (upIntent != null) {
            extras?.let {
                upIntent.putExtras(extras)
            }
            if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(activity) // Add all of this activity's parents to the back stack.
                        .addNextIntentWithParentStack(upIntent) // Navigate up to the closest parent.
                        .startActivities()
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                // According to http://stackoverflow.com/a/14792752/2420519
                //NavUtils.navigateUpTo(activity, upIntent);
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                activity.startActivity(upIntent)
            }
        }
        activity.finish()
    }
}