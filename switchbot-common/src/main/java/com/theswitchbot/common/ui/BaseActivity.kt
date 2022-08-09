package com.theswitchbot.common.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.theswitchbot.common.util.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity(), BaseView by BaseViewDelegate(),
    EasyPermissions.PermissionCallbacks{
    protected lateinit var binding: T
    var isActivityVisible = false

    private var enterTime : Long = 0 // 进入页面的时间

    private val TAG = BaseActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransition()
        super.onCreate(savedInstanceState)
        // 修复Android8.0设置透明activity时崩溃
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        ARouterUtil.aRouter.inject(this@BaseActivity)

        binding()?.apply {
            binding = this
            setContentView(root)
        }

        setup()
    }



    fun switchStatusBarTextColor(isDark: Boolean) {
        when (isDark) {
            true -> QMUIStatusBarHelper.setStatusBarDarkMode(this)
            false -> QMUIStatusBarHelper.setStatusBarLightMode(this)
        }
    }

    override fun onResume() {
        super.onResume()
        isActivityVisible = true
        onSetupSystemBar()
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
    }

    override fun onStart() {
        super.onStart()
        enterTime = System.currentTimeMillis()
    }

    override fun onStop() {
        super.onStop()
        val timeDiff = System.currentTimeMillis() - enterTime
        onLeave(timeDiff)
    }


    /**
     * 当这个界面不可见/离开这个界面之后
     */
    open fun onLeave(stayTime: Long) {

    }


    /**
     *  使用EasyPermissions，记得复写这个方法，并且修改注解中的参数
     */
    @AfterPermissionGranted(-1000)
    open fun requestPermissions() {

    }

    override fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * 关闭软件盘
     */
    fun closeSoftKeyboard() {
        val v = window.peekDecorView()
        if (v != null) {
            val inputManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    fun showMessage(message: String) {
        runOnUiThread{
            TipUtil.showToast(this, message)
        }
    }

    /**
     * 设置状态栏和导航栏，可重写设置当前页面的系统栏样式
     */
    protected open fun onSetupSystemBar() {
        setupSystemBar()
    }

    /**
     * Activity需添加转场动画时可复写该方法
     */
    protected open fun onTransition() {

    }


    protected open fun binding(): T? {
        return ViewBindingUtils.binding(this, layoutInflater, null, false)
    }

    protected abstract fun setup()
}