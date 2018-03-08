package com.kevinmost.autoandromeda

import android.app.Activity
import android.os.Bundle
import com.topjohnwu.superuser.Shell
import kotlinx.android.synthetic.main.activity_main.*

const val PACKAGE_NAME_ANDROMEDA = "projekt.andromeda"
const val PACKAGE_NAME_SUBSTRATUM = "projekt.substratum"
const val URL_MAGISK_THREAD = "https://forum.xda-developers.com/apps/magisk/official-magisk-v7-universal-systemless-t3473445"

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun onResume() {
    super.onResume()

    val requirements = requirementsFulfilled()

    run {
      tv_is_rooted.setText(if (requirements.rootGranted) {
        R.string.is_rooted_yes
      } else {
        R.string.is_rooted_no
      })
      btn_more_magisk_info.visible = !requirements.rootGranted
      btn_more_magisk_info.setOnClickListener {
        startActivity(websiteIntent(URL_MAGISK_THREAD))
      }
    }

    run {
      tv_is_substratum_installed.setText(if (requirements.substratumInstalled) {
        R.string.is_substratum_installed_yes
      } else {
        R.string.is_substratum_installed_no
      })
      btn_install_substratum.setOnClickListener {
        startActivity(playStoreIntent(PACKAGE_NAME_SUBSTRATUM))
      }
      btn_install_substratum.visible = !requirements.substratumInstalled
    }

    run {
      tv_is_andromeda_installed.setText(if (requirements.andromedaInstalled) {
        R.string.is_andromeda_installed_yes
      } else {
        R.string.is_andromeda_installed_no
      })
      btn_install_andromeda.setOnClickListener {
        startActivity(playStoreIntent(PACKAGE_NAME_ANDROMEDA))
      }
      btn_install_andromeda.visible = !requirements.andromedaInstalled
    }


    vs_good_to_go.displayedChild = if (requirements.allGood) 1 else 0
    btn_show_debug_screen.setOnClickListener {
      startActivity(intentFor<DebugActivity>())
    }
  }
}