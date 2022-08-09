package com.kandao.smartqrcode.ui.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kandao.smartqrcode.ui.fragment.MakeFragment
import com.kandao.smartqrcode.ui.fragment.ScanFragment
import com.kandao.smartqrcode.ui.fragment.SetFragment
import java.util.Collections
import java.util.EnumMap

class MainTabPageAdapter(private val mPages: List<Pager>, manager: FragmentManager) :
  FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

  private val fragmentMap = Collections.synchronizedMap(EnumMap<Pager, Fragment>(Pager::class.java))

  override fun getCount(): Int = mPages.size

  override fun getItem(position: Int): Fragment {
    return getFragment(position)
  }

  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
    super.destroyItem(container, position, `object`)
    val key = mPages[position]
    fragmentMap.remove(key)
  }

  private fun getFragment(position: Int): Fragment {
    val key = mPages[position]
    return if (fragmentMap[key] != null) {
      fragmentMap[key]!!
    } else {
      val fragment = when (key) {
        Pager.SCAN -> ScanFragment.newInstance()
        Pager.MAKE -> MakeFragment.newInstance()
        Pager.SET -> SetFragment.newInstance()
      }
      fragmentMap[key] = fragment
      fragment
    }
  }

}