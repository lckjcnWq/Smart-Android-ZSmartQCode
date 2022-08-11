package com.kandao.smartqrcode.ui.activity

import android.view.View
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.constant.AppContants
import com.kandao.smartqrcode.databinding.ActivityScanResultBinding
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.ui.BaseActivity
import com.theswitchbot.common.util.initTitleBar

class ScanResultActivity : BaseActivity<ActivityScanResultBinding>() {

  override fun setup() {
    initTitleBar(binding.topbar, centerTextRes = R.string.scan_result)
    initQrCode()
    setListener()
  }

  private fun initQrCode() {
    val barFormat = intent.getStringExtra(AppContants.KEY_BITMAP) ?: ""
    val scanResult = intent.getStringExtra(AppContants.KEY_CONTENT) ?: ""
    Logger.d("initQrCode barFormat=$barFormat ,scanResult=$scanResult")
    binding.tvScanResult.text = scanResult
    if (startWithHttp(scanResult)) {
      binding.btnOpen.visibility = View.VISIBLE
    }
    when (barFormat) {
      in AppContants.getTwoDimensionalFormats -> {
        AppContants.createQRCode(scanResult, binding.ivScan, null)
      }
      in AppContants.getOneDimensionalFormats -> {
        AppContants.createBarCode(scanResult, binding.ivScan)
      }
    }
  }

  private fun setListener() {
    binding.btnShare.setOnClickListener {

    }
    binding.btnCopy.setOnClickListener {
      AppContants.copyTextToKeyBoard(this, binding.tvScanResult.text.toString())
    }
    binding.btnOpen.setOnClickListener {
      if (startWithHttp(binding.tvScanResult.text.toString())) {
        AppContants.openUrl(this, binding.tvScanResult.text.toString())
      }
    }
  }

  private fun startWithHttp(url: String): Boolean {
    return url.startsWith("https://") || url.startsWith("http://")
  }
}