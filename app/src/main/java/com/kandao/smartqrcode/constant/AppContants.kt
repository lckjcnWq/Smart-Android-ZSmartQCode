package com.kandao.smartqrcode.constant

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import com.google.zxing.BarcodeFormat
import com.google.zxing.BarcodeFormat.CODE_128
import com.kandao.smartqrcode.R
import com.king.zxing.util.CodeUtils
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.util.TipUtil
import com.theswitchbot.common.web.CustomTabActivityHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object AppContants {
  const val KEY_BITMAP = "key_bitmap"
  const val KEY_CONTENT = "key_content"

  //所有一维码格式
  val getOneDimensionalFormats = listOf(
    BarcodeFormat.CODABAR, BarcodeFormat.CODE_39, BarcodeFormat.CODE_93,
    BarcodeFormat.CODE_128, BarcodeFormat.EAN_8, BarcodeFormat.EAN_13,
    BarcodeFormat.ITF, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED,
    BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.UPC_EAN_EXTENSION
  ).map { it.toString() }

  //所有二维码格式
  val getTwoDimensionalFormats = listOf(
    BarcodeFormat.AZTEC, BarcodeFormat.DATA_MATRIX, BarcodeFormat.MAXICODE,
    BarcodeFormat.PDF_417, BarcodeFormat.QR_CODE
  ).map { it.toString() }

  /**
   * 生成二维码
   * @param content
   */
  fun createQRCode(content: String?, imageView: ImageView,logo: Bitmap?) {
    if(content==null || content.isEmpty()) return
    Logger.d("createQRCode content=$content")
    MainScope().launch(Dispatchers.IO) {
      val bitmap = CodeUtils.createQRCode(content, 1080, logo)
      launch(Dispatchers.Main) {
        Logger.d("createQRCode bitmap=$bitmap")
        imageView.setImageBitmap(bitmap)
      }
    }
  }

  /**
   * 生成条形码
   * @param content
   */
  fun createBarCode(content: String?, imageView: ImageView) {
    if(content==null || content.isEmpty()) return
    MainScope().launch(Dispatchers.IO) {
      val bitmap = CodeUtils.createBarCode(content, CODE_128, 1080,270, null, true)
      launch(Dispatchers.Main) {
        imageView.setImageBitmap(bitmap)
      }
    }
  }

  /**
   * 复制到剪切板
   * */
  fun copyTextToKeyBoard(context: Context,content:String){
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val mClipData = ClipData.newPlainText("Label", content)
    cm.setPrimaryClip(mClipData)
    TipUtil.showToast(context,R.string.scan_has_copy)
  }

  /**
   * 打开外链
   * */
  fun openUrl(activity: Activity,url:String){
    val uri =Uri.parse(url)
    val customTabsIntent1 = CustomTabsIntent.Builder().build()
    CustomTabActivityHelper.openCustomTab(activity, customTabsIntent1, uri, null)
  }
}