package com.theswitchbot.common.ui

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.databinding.ActivityLayoutEmptyBinding
import java.lang.reflect.Method

abstract class DynamicLayoutActivity:BaseActivity<ActivityLayoutEmptyBinding>() {
    companion object{
        const val LAYOUT_NAME="layoutName"
    }


    fun <T:ViewBinding>dynamicLayout(clazzName:String):T{
        val clazz=Class.forName(clazzName)
        val method: Method = clazz.getMethod("inflate", LayoutInflater::class.java)
        val layout= method.invoke(null,layoutInflater) as T
        setContentView(layout.root)
        return layout
    }



    override fun binding(): ActivityLayoutEmptyBinding = ActivityLayoutEmptyBinding.inflate(layoutInflater)


}