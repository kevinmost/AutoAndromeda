package com.kevinmost.autoandromeda

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.topjohnwu.superuser.Shell

private val TAG = DeviceBootReceiver::class.java.simpleName

const val PREF_LAST_BOOT_TEXT = "last_boot_text"
const val PREF_LAST_BOOT_SUCCESS = "last_boot_success"

class DeviceBootReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null || intent == null) return
    if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

    val appName = context.getString(R.string.app_name)

    Log.i(TAG, "Device booted, going to run $appName")

    val requirements = context.requirementsFulfilled()

    if (requirements.allGood) {
      // we're rooted, the apps are installed, we can run our magic script
      val scriptText = context.readAssetsFileText("andromeda_local.sh")
      val callback = Shell.Async.Callback { out: List<String>?, err: List<String>? ->
        val success = err == null || err.isEmpty()
        val text = buildString {
          appendln("stdout:")
          appendln(out.orEmpty().joinToString(separator = "\n"))
          appendln()
          appendln()
          appendln("stderr:")
          appendln(err.orEmpty().joinToString(separator = "\n"))
        }
        context.sharedPrefs.edit {
          putBoolean(PREF_LAST_BOOT_SUCCESS, success)
          putString(PREF_LAST_BOOT_TEXT, text)
        }
      }
      Shell.Async.su(callback, scriptText)
    } else {
      // one of our requirements is not met!
      context.sharedPrefs.edit {
        putBoolean(PREF_LAST_BOOT_SUCCESS, false)
        putString(PREF_LAST_BOOT_TEXT, "Requirements failed: $requirements")
      }
    }
  }
}