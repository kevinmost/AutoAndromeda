package com.kevinmost.autoandromeda

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_debug.*

class DebugActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_debug)

    run {
      val lastBootSuccess = sharedPrefs.getBoolean(PREF_LAST_BOOT_SUCCESS, false)
      tv_debug_was_successful.setText(if (lastBootSuccess) {
        R.string.debug_success_yes
      } else {
        R.string.debug_success_no
      })
    }

    run {
      val lastBootText = sharedPrefs.getString(PREF_LAST_BOOT_TEXT, "")
      tv_debug_output.text = lastBootText
    }
  }
}