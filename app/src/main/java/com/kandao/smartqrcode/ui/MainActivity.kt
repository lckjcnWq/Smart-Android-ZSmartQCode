package com.kandao.smartqrcode.ui

import android.Manifest
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.R.dimen
import com.kandao.smartqrcode.R.drawable
import com.kandao.smartqrcode.R.string
import com.kandao.smartqrcode.databinding.ActivityMainBinding
import com.kandao.smartqrcode.ui.adapter.MainTabPageAdapter
import com.kandao.smartqrcode.ui.adapter.Pager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder
import com.theswitchbot.common.constant.RouterParams
import com.theswitchbot.common.livedata.LiveDataBus
import com.theswitchbot.common.livedata.LiveDataConst
import com.theswitchbot.common.ui.BaseActivity
import com.theswitchbot.common.util.ARouterUtil
import com.theswitchbot.common.util.dp2Px
import com.theswitchbot.common.util.getDimen
import com.theswitchbot.common.util.initTitleBar
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity<ActivityMainBinding>() {

  companion object {
    const val REQ_PERMISSION = 1
  }

  private lateinit var tabAdapter: MainTabPageAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_SwitchBot)
    super.onCreate(savedInstanceState)
  }

  override fun setup() {
    QMUIStatusBarHelper.setStatusBarLightMode(this)
    checkPermissions()
    initTabs()
    initPagers()
  }

  private fun checkPermissions() {
    val permissions = arrayOf(
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.CAMERA
    )
    if (!EasyPermissions.hasPermissions(this, *permissions)) {
      ActivityCompat.requestPermissions(this, permissions, REQ_PERMISSION)
    }
  }

  private fun initTabs() {
    val builder = binding.mainTabs.tabBuilder()
    builder.setTypeface(null, Typeface.DEFAULT_BOLD)
    builder.setSelectedIconScale(1f)
      .setTextSize(getDimen(dimen.t2), getDimen(dimen.t2))
      .setIconTextGap(7.dp2Px())
      .setDynamicChangeIconColor(false)
      .skinChangeWithTintColor(false)
      .setNormalIconSizeInfo(
        getDimen(dimen.home_tab_icon_width),
        getDimen(dimen.home_tab_text_height)
      )
    val scanTab = builder
      .setNormalDrawable(
        ContextCompat.getDrawable(
          this,
          drawable.icon_scan_normal
        )
      )
      .setSelectedDrawable(
        ContextCompat.getDrawable(
          this,
          drawable.icon_scan_focus
        )
      )
      .setText(getString(string.main_menu_title_scan))
      .build(this)
    val makeCodeTab = builder
      .setNormalDrawable(ContextCompat.getDrawable(this, drawable.icon_make_normal))
      .setSelectedDrawable(
        ContextCompat.getDrawable(
          this,
          drawable.icon_make_focus
        )
      )
      .setText(getString(string.main_menu_title_make))
      .build(this)
    val setTab = builder
      .setNormalDrawable(ContextCompat.getDrawable(this, drawable.icon_set_normal))
      .setSelectedDrawable(
        ContextCompat.getDrawable(
          this,
          drawable.icon_set_focus
        )
      )
      .setText(getString(string.main_menu_title_set))
      .build(this)

    binding.mainTabs.apply {
      addTab(scanTab)
      addTab(makeCodeTab)
      addTab(setTab)
    }
  }

  private fun initPagers() {
    val mPages = listOf(Pager.SCAN,Pager.MAKE, Pager.SET)
    tabAdapter = MainTabPageAdapter(mPages,supportFragmentManager)
    binding.mainPager.offscreenPageLimit=3
    binding.mainPager.adapter = tabAdapter
    binding.mainTabs.setupWithViewPager(binding.mainPager, false)
    binding.mainPager.setSwipeable(false)
  }

}