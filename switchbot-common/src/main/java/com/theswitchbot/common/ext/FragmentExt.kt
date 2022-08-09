package com.theswitchbot.common.ext

import android.app.Activity
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.theswitchbot.common.util.TipUtil


/****************** fragment 相关操作 ************************/

/**
 * 添加fragment
 */
fun Fragment.addFragment(fragment: Fragment?, @IdRes frameIdRes: Int, pageTag: String? = null) {
    if (fragment?.isAdded == true) {
        if (fragment is DialogFragment) {
            fragment.dismiss()
        }
        showFragment(fragment)
        return
    }

    fragment?.let { frg ->
        doOnTransaction(this.childFragmentManager) {
            if (pageTag.isNullOrBlank()) {
                it.add(frameIdRes, frg)
            } else {
                it.add(frameIdRes, frg, pageTag)
            }
            it.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        }
    }
}

/**
 * 显示fragment
 */
fun Fragment.showFragment(fragment: Fragment?) {
    fragment?.let { frg ->
        doOnTransaction(this.childFragmentManager) {
            it.show(frg)
            it.setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        }
    }
}

/**
 * 隐藏fragment
 */
fun Fragment.hideFragment(
        fragment: Fragment?) {
    fragment?.let { frg ->
        doOnTransaction(this.childFragmentManager) {
            it.hide(frg)
            it.setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        }
    }
}


/**
 * 替换fragment
 */
fun Fragment.replaceFragment(fragment: Fragment?, @IdRes frameIdRes: Int, tag: String? = null) {
    fragment?.let { frag ->
        doOnTransaction(this.childFragmentManager) {
            it.replace(frameIdRes, frag, tag)
        }
    }
}


/**
 * 移除fragment
 */
fun Fragment.removeFragment(fragment: Fragment?) {
    fragment?.let { frag ->
        doOnTransaction(this.childFragmentManager) {
            it.remove(frag)
        }
    }
}


fun Fragment.doOnTransaction(fragmentManager: androidx.fragment.app.FragmentManager,
                             action: (transaction: androidx.fragment.app.FragmentTransaction) -> Unit) {
    val transaction = fragmentManager.beginTransaction()
    action(transaction)
    transaction.commitAllowingStateLoss()
}