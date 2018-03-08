package com.kevinmost.autoandromeda

import com.topjohnwu.superuser.BusyBox
import com.topjohnwu.superuser.Shell

class App : Shell.ContainerApp() {
  companion object {
    lateinit var INSTANCE: App
      private set
  }

  override fun onCreate() {
    INSTANCE = this

    super.onCreate()

    Shell.verboseLogging(BuildConfig.DEBUG)
    BusyBox.setup(this)
  }
}