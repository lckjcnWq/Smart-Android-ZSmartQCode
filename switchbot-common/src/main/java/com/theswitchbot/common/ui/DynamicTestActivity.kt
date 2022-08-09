package com.theswitchbot.common.ui

import com.theswitchbot.common.databinding.ActivityLayoutEmptyBinding
import com.theswitchbot.common.util.ARouterUtil

/**
 * 此Activity无实际用途 仅做代码示例，可随时删除
 */
class DynamicTestActivity:DynamicLayoutActivity() {


    override fun setup() {

//        在唤起此Activity时 传递布局class的名称，例如 ARouterUtil.navigation(this,"RoutePath",LAYOUT_NAME,ActivityLayoutEmptyBinding::class.qualifiedName!!)
//        需注意ActivityLayoutEmptyBinding 需与dynamicLayout方法的泛型一致

        val binding=dynamicLayout<ActivityLayoutEmptyBinding>(intent.getStringExtra(LAYOUT_NAME)!!)
        //正常操作UI控件即可
    }
}