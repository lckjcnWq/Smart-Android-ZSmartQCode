package com.theswitchbot.common.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.io.*


object ToolUtil {
    //合并两个byte数组
    fun byteMerger(bt1: ByteArray, bt2: ByteArray): ByteArray? {
        val bt3 = ByteArray(bt1.size + bt2.size)
        var i = 0
        for (bt in bt1) {
            bt3[i] = bt
            i++
        }
        for (bt in bt2) {
            bt3[i] = bt
            i++
        }
        return bt3
    }

    /**
     * 获取byte[]的前几位
     * @param sourceByte
     * @param num
     * @return
     */
    fun getSomeByte(sourceByte: ByteArray, num: Int): ByteArray? {
        if (num > sourceByte.size) {
            return null
        }
        val desByte = ByteArray(num)
        for (i in 0 until num) {
            desByte[i] = sourceByte[i]
        }
        return desByte
    }

    fun getTextColorFromThemeAttr(context: Context, resid: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(resid))
        val color = a.getColor(0 /*index*/, 0 /*defValue*/)
        a.recycle()
        return color
    }

    /**
     * 存放16进制字符串,字符串2为 2为存
     * @param hexString
     * @return
     */
    fun parseArrayList(hexString: String): List<String> {
        val strings: MutableList<String> = ArrayList()
        for (i in 0 until hexString.length / 2) {
            val index = i * 2
            val `var` = hexString.substring(index, index + 2)
            strings.add(`var`)
        }
        return strings
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    fun showSoftInputFromWindow(activity: Activity, editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()

        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    /**
     * 拷贝复杂的数据
     */
    fun copyObject(orig: Any?): Any? {
        var obj: Any? = null
        try {
            val bos = ByteArrayOutputStream()
            val out = ObjectOutputStream(bos)
            out.writeObject(orig)
            out.flush()
            out.close()
            val `in` = ObjectInputStream(
                ByteArrayInputStream(bos.toByteArray())
            )
            obj = `in`.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }


    /**
     * 将图片保存到sd卡
     */
    fun savePhotoToSD(mbitmap: Bitmap?, originpath: String?): String? {
        var outStream: FileOutputStream? = null
        var fileName: String? = ""
        return if (mbitmap == null) {
            originpath
        } else if (TextUtils.isEmpty(originpath)) {
            originpath
        } else {
            fileName = originpath
            val var5: Any?
            try {
                outStream = FileOutputStream(fileName)
                mbitmap.compress(CompressFormat.JPEG, 100, outStream)
                return fileName
            } catch (var15: java.lang.Exception) {
                var15.printStackTrace()
                var5 = null
            } finally {
                try {
                    outStream?.close()
                    if (mbitmap != null) {
                        mbitmap.recycle()
                    }
                } catch (var14: java.lang.Exception) {
                    var14.printStackTrace()
                }
            }
            var5 as String?
        }
    }
}