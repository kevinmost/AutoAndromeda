package com.kevinmost.autoandromeda

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.preference.PreferenceManager
import android.view.View
import com.topjohnwu.superuser.Shell
import org.intellij.lang.annotations.Subst

fun Context.isAppInstalled(packageName: String): Boolean {
  return try {
    packageManager.getPackageInfo(packageName, 0)
    true
  } catch (notFound: PackageManager.NameNotFoundException) {
    false
  }
}

fun Context.playStoreIntent(packageName: String) = websiteIntent("market://details?id=$packageName")

fun Context.websiteIntent(website: String): Intent = Intent(Intent.ACTION_VIEW).apply {
  data = Uri.parse(website)
}

inline fun <reified A: Activity> Context.intentFor(): Intent = Intent(this, A::class.java)

var View.visible: Boolean
   get() = visibility == View.VISIBLE
   set(visible) {
     visibility = if (visible) View.VISIBLE else View.INVISIBLE
   }

val Context.sharedPrefs: SharedPreferences
  get() = PreferenceManager.getDefaultSharedPreferences(this)


class PrefEditor(editor: SharedPreferences.Editor) : SharedPreferences.Editor by editor {

  class EditAbortedException: RuntimeException()

  fun abort(): Nothing {
    throw EditAbortedException()
  }
}

inline fun SharedPreferences.edit(edit: PrefEditor.() -> Unit) {
  val editor = edit()
  try {
    edit(PrefEditor(editor))
    editor.apply()
  } catch (abort: PrefEditor.EditAbortedException) {}
}

fun Context.requirementsFulfilled(): Requirements {
  return Requirements(
      rootGranted = Shell.rootAccess(),
      substratumInstalled = isAppInstalled(PACKAGE_NAME_SUBSTRATUM),
      andromedaInstalled = isAppInstalled(PACKAGE_NAME_ANDROMEDA)
  )
}

fun Context.readAssetsFileText(filename: String): String = assets
    .open(filename)
    .bufferedReader()
    .use { it.readText() }

data class Requirements(
    val rootGranted: Boolean,
    val substratumInstalled: Boolean,
    val andromedaInstalled: Boolean
) {
  val allGood = rootGranted && substratumInstalled && andromedaInstalled
}
