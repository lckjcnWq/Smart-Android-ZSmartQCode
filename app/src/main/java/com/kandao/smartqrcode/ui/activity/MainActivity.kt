package com.kandao.smartqrcode.ui.activity

import android.Manifest
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.R.dimen
import com.kandao.smartqrcode.R.drawable
import com.kandao.smartqrcode.R.string
import com.kandao.smartqrcode.databinding.ActivityMainBinding
import com.kandao.smartqrcode.ui.adapter.MainTabPageAdapter
import com.kandao.smartqrcode.ui.adapter.Pager
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.theswitchbot.common.ui.BaseActivity
import com.theswitchbot.common.util.dp2Px
import com.theswitchbot.common.util.getDimen
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : BaseActivity<ActivityMainBinding>() {

  companion object {
    const val REQ_PERMISSION = 1
  }

  private lateinit var mPages: List<Pager>
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
    val permissions = arrayOf(Manifest.permission.CAMERA)
    if (!EasyPermissions.hasPermissions(this, *permissions)) {
      ActivityCompat.requestPermissions(this, permissions, REQ_PERMISSION)
    }
  }

  private fun initTabs() {
    val builder = binding.mainTabs.tabBuilder()
    builder.setTypeface(null, Typeface.DEFAULT_BOLD)
    builder.setSelectedIconScale(1f)
      .setTextSize(getDimen(dimen.t3), getDimen(dimen.t3))
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
      addTab(makeCodeTab)
      addTab(scanTab)
      addTab(setTab)
    }
  }

  private fun initPagers() {
    mPages = listOf(Pager.MAKE,Pager.SCAN, Pager.SET)
    tabAdapter = MainTabPageAdapter(mPages,supportFragmentManager)
    binding.mainPager.offscreenPageLimit=1
    binding.mainPager.adapter = tabAdapter
    binding.mainTabs.setupWithViewPager(binding.mainPager, false)
    binding.mainPager.setSwipeable(false)
    binding.mainPager.currentItem = getPageIndex(Pager.SCAN)
  }

  private fun getPageIndex(pager:Pager):Int{
    mPages.forEachIndexed { index, _pager ->
      if (pager == _pager)return index
    }
    return 0
  }
}