package com.kandao.smartqrcode.ui.fragment

import android.os.Bundle
import android.view.View
import com.google.zxing.Result
import com.kandao.smartqrcode.R
import com.king.zxing.CaptureFragment
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.config.ResolutionCameraConfig
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.theswitchbot.common.ext.showToast
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.initTitleBar

/**
 * <pre>
 * author : Administrator
 * time : 2022/08/05
 * </pre>
 */
class ScanFragment : CaptureFragment() {

  companion object {
    @JvmStatic
    fun newInstance() = ScanFragment()
  }

  override fun getLayoutId(): Int {
    return R.layout.fragment_scan
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initTitleBar(rootView.findViewById(R.id.topbar), centerText = getString(R.string.main_menu_title_scan))
  }

  override fun initCameraScan() {
    super.initCameraScan()
    //初始化解码配置
    val decodeConfig = DecodeConfig()
    decodeConfig.setHints(DecodeFormatManager.ALL_HINTS)
      .setSupportVerticalCode(true)
      .setSupportLuminanceInvert(true)
      .setAreaRectRatio(0.8f)

    //获取CameraScan，里面有扫码相关的配置设置。CameraScan里面包含部分支持链式调用的方法，即调用返回是CameraScan本身的一些配置建议在startCamera之前调用。
    cameraScan.setPlayBeep(true) //设置是否播放音效，默认为false
      .setVibrate(true) //设置是否震动，默认为false
      .setCameraConfig(ResolutionCameraConfig(requireContext())) //设置CameraConfig，可以根据自己的需求去自定义配置
      .setNeedAutoZoom(true) //二维码太小时可自动缩放，默认为false
      .setNeedTouchZoom(true) //支持多指触摸捏合缩放，默认为true
      .setDarkLightLux(45f) //设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
      .setBrightLightLux(100f) //设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
      .bindFlashlightView(ivFlashlight) //绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
      .setOnScanResultCallback(this) //设置扫码结果回调，需要自己处理或者需要连扫时，可设置回调，自己去处理相关逻辑
      .setAnalyzer(MultiFormatAnalyzer(decodeConfig))
      .setAnalyzeImage(true)
  }

  override fun onScanResultCallback(result: Result): Boolean {
    Logger.d("onScanResultCallback result:${result.text}")
    cameraScan.setAnalyzeImage(false)
    return true
  }
}