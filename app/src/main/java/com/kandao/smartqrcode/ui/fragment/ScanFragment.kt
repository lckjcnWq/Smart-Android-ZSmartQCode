package com.kandao.smartqrcode.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.zxing.Result
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.utils.StatusBarUtils
import com.king.zxing.CaptureFragment
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.config.ResolutionCameraConfig
import com.theswitchbot.common.logger.Logger

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
    val toolbar: Toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
    StatusBarUtils.immersiveStatusBar(requireActivity(), toolbar, 0.2f)
    val tvTitle = rootView.findViewById<TextView>(R.id.tvTitle)
    tvTitle.text = getString(R.string.main_menu_title_scan)
    rootView.findViewById<ImageView>(R.id.ivLeft).setOnClickListener { requireActivity().finish() }
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
//      .setDarkLightLux(45f) //设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
//      .setBrightLightLux(100f) //设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
//      .bindFlashlightView(ivFlashlight) //绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
      .setOnScanResultCallback(this)
      .setAnalyzer(MultiFormatAnalyzer(decodeConfig))
      .setAnalyzeImage(true)
  }

  override fun onScanResultCallback(result: Result): Boolean {
    Logger.d("onScanResultCallback result:${result.text}")
//    cameraScan.setAnalyzeImage(false)
    return true
  }
}