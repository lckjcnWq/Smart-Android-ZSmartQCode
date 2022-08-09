package com.theswitchbot.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.fragment.LazyFragment
import com.theswitchbot.common.util.ViewBindingUtils
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseFragment<T : ViewBinding> : LazyFragment(), BaseView by BaseViewDelegate(),
    EasyPermissions.PermissionCallbacks{
    protected lateinit var binding: T
    private val TAG = BaseFragment::class.simpleName
    var isNotDestroy: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = binding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isNotDestroy = isAdded
        setup()
    }


    /**
     *  使用EasyPermissions，记得复写这个方法，并且修改注解中的参数
     */
    @AfterPermissionGranted(-1000)
    open fun requestPermissions() {

    }

    override fun onPermissionsGranted(requestCode: Int, @NonNull perms: List<String>) {
        //申请权限被拒绝后的回调
    }

    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
        //申请权限被拒绝后的回调
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    protected open fun binding(inflater: LayoutInflater,container: ViewGroup?,attach: Boolean):T{
        return ViewBindingUtils.binding(this, inflater, container, attach)
    }

    abstract fun setup()

}