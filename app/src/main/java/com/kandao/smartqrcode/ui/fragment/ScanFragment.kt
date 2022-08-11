package com.kandao.smartqrcode.ui.fragment

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.zxing.Result
import com.kandao.smartqrcode.R
import com.kandao.smartqrcode.R.string
import com.kandao.smartqrcode.constant.AppContants
import com.kandao.smartqrcode.ui.activity.ScanResultActivity
import com.king.zxing.CaptureFragment
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import com.king.zxing.config.ResolutionCameraConfig
import com.king.zxing.util.CodeUtils
import com.theswitchbot.common.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.Timer
import java.util.TimerTask

/**
 * <pre>
 * author : Administrator
 * time : 2022/08/05
 * </pre>
 */
class ScanFragment : CaptureFragment() {

  private var isFirstGo: Boolean = true
  private var timer: Timer? = null
  private var state: Int = WARN2
  private val intervalTime: Long = 5000

  companion object {
    @JvmStatic
    fun newInstance() = ScanFragment()
    const val WARN1 = 1
    const val WARN2 = 2
    const val WARN3 = 3
    const val RC_READ_PHOTO = 0X02
    const val REQUEST_CODE_PHOTO = 0X02
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

  private fun setListener() {
    rootView.findViewById<ImageView>(R.id.btnLight).setOnClickListener {
      toggleLightState()
    }
    rootView.findViewById<ImageView>(R.id.btnPhoto).setOnClickListener {
      checkExternalStoragePermissions()
    }
  }

  private fun circleShowTips() {
    val tips = listOf(
      getString(R.string.qr_code_tips1),
      getString(R.string.qr_code_tips2),
      getString(R.string.qr_code_tips3)
    )
    timer?.schedule(object : TimerTask() {
      override fun run() {
        requireActivity().runOnUiThread {
          rootView.findViewById<TextView>(R.id.tvCodeTip).text = tips[state - 1]
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
    }, intervalTime, intervalTime)
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
    val decodeConfig = DecodeConfig().apply {
      hints = DecodeFormatManager.ALL_HINTS
      isSupportVerticalCode = true
      isSupportLuminanceInvert = true
      areaRectRatio = 0.8f
    }
    cameraScan.setPlayBeep(true) //设置是否播放音效，默认为false
      .setVibrate(true) //设置是否震动，默认为false
      .setCameraConfig(ResolutionCameraConfig(requireContext())) //设置CameraConfig，可以根据自己的需求去自定义配置
      .setNeedAutoZoom(true) //二维码太小时可自动缩放，默认为false
      .setNeedTouchZoom(true) //支持多指触摸捏合缩放，默认为true
      .setOnScanResultCallback(this)
      .setAnalyzer(MultiFormatAnalyzer(decodeConfig))
      .setAnalyzeImage(true)
  }

  @AfterPermissionGranted(RC_READ_PHOTO)
  private fun checkExternalStoragePermissions() {
    val perms = arrayOf(permission.READ_EXTERNAL_STORAGE, permission.WRITE_EXTERNAL_STORAGE)
    if (EasyPermissions.hasPermissions(requireContext(), *perms)) { //有权限
      startPhotoCode()
    } else {
      EasyPermissions.requestPermissions(
        this,
        getString(string.permission_external_storage),
        RC_READ_PHOTO,
        *perms
      )
    }
  }

  private fun startPhotoCode() {
    val pickIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
    pickIntent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*")
    startActivityForResult(pickIntent, REQUEST_CODE_PHOTO)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && data != null) {
      when (requestCode) {
        REQUEST_CODE_PHOTO -> parsePhoto(data)
      }
    }
  }

  private fun parsePhoto(data: Intent) {
    try {
      val bitmap = Media.getBitmap(requireActivity().contentResolver, data.data)
      lifecycleScope.launch(Dispatchers.IO) {
        val result = CodeUtils.parseCodeResult(bitmap, DecodeFormatManager.ALL_HINTS)
        launch(Dispatchers.Main) {
          goToResult(result)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun goToResult(result: Result?) {
    if (isFirstGo && result != null && result.text!=null && result.text.isNotEmpty() ) {
      isFirstGo = false
      Toast.makeText(context, result.text, Toast.LENGTH_SHORT).show()
      val intent = Intent(requireActivity(), ScanResultActivity::class.java)
      intent.putExtra(AppContants.KEY_BITMAP,result.barcodeFormat.toString())
      intent.putExtra(AppContants.KEY_CONTENT,result.text.toString())
      startActivity(intent)
    }
  }

  override fun onPause() {
    super.onPause()
    isFirstGo = false
  }

  override fun onResume() {
    super.onResume()
    cameraScan.setAnalyzeImage(true)
    isFirstGo = true
  }

  override fun onDestroy() {
    super.onDestroy()
    timer?.cancel()
  }

  override fun onScanResultCallback(result: Result): Boolean {
    Logger.d("onScanResultCallback result text:${result.text},result type:${result.barcodeFormat}")
    cameraScan.setAnalyzeImage(false)
    goToResult(result)
    return true
  }
}