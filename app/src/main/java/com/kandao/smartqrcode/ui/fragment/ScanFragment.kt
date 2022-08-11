package com.kandao.smartqrcode.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.Result
import com.kandao.smartqrcode.R
import com.king.zxing.CaptureFragment
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.config.ResolutionCameraConfig
import com.theswitchbot.common.logger.Logger
import java.util.Timer
import java.util.TimerTask

/**
 * <pre>
 * author : Administrator
 * time : 2022/08/05
 * </pre>
 */
class ScanFragment : CaptureFragment() {

  private var timer: Timer?=null
  private var state:Int = WARN2
  private val intervalTime:Long = 5000

  companion object {
    @JvmStatic
    fun newInstance() = ScanFragment()
    const val WARN1=1
    const val WARN2=2
    const val WARN3=3
  }


  override fun getLayoutId(): Int {
    return R.layout.fragment_scan
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    timer = Timer()
    setListener()
    circleShowTips()
  }

  private fun setListener(){
    rootView.findViewById<ImageView>(R.id.btnLight).setOnClickListener {
      toggleLightState()
    }
    rootView.findViewById<ImageView>(R.id.btnPhoto).setOnClickListener {

    }
  }

  private fun circleShowTips(){
    val tips = listOf(getString(R.string.qr_code_tips1),getString(R.string.qr_code_tips2),getString(R.string.qr_code_tips3))
    timer?.schedule(object :TimerTask(){
      override fun run() {
        requireActivity().runOnUiThread {
          rootView.findViewById<TextView>(R.id.tvCodeTip).text = tips[state-1]
        }
        when (state) {
          WARN1 -> {
            state = WARN2
          }
          WARN2 -> {
            state = WARN3
          }
          WARN3 -> {
            state = WARN1
          }
        }
      }
    },intervalTime,intervalTime)
  }


  /**
   * 切换闪光灯状态（开启/关闭）
   */
  private fun toggleLightState() {
    if (cameraScan != null) {
      val isTorch = cameraScan.isTorchEnabled
      cameraScan.enableTorch(!isTorch)
      rootView.findViewById<ImageView>(R.id.btnLight).isSelected = !isTorch
    }
  }

  override fun initCameraScan() {
    super.initCameraScan()
    //初始化解码配置
    val decodeConfig = DecodeConfig()
    decodeConfig.setHints(DecodeFormatManager.ALL_HINTS)
      .setSupportVerticalCode(true)
      .setSupportLuminanceInvert(true)
      .setAreaRectRatio(0.8f)

    cameraScan.setPlayBeep(true) //设置是否播放音效，默认为false
      .setVibrate(true) //设置是否震动，默认为false
      .setCameraConfig(ResolutionCameraConfig(requireContext())) //设置CameraConfig，可以根据自己的需求去自定义配置
      .setNeedAutoZoom(true) //二维码太小时可自动缩放，默认为false
      .setNeedTouchZoom(true) //支持多指触摸捏合缩放，默认为true
      .setOnScanResultCallback(this)
      .setAnalyzer(MultiFormatAnalyzer(decodeConfig))
      .setAnalyzeImage(true)
  }

  override fun onDestroy() {
    super.onDestroy()
    timer?.cancel()
  }

  override fun onScanResultCallback(result: Result): Boolean {
    Logger.d("onScanResultCallback result:${result.text}")
//    cameraScan.setAnalyzeImage(false)
    return true
  }
}