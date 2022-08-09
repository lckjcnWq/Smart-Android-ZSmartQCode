package com.theswitchbot.common.widget.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Author zrh
 * Date 2022/2/17 6:16 下午
 * Description
 */
class BindingViewHolder<VB:ViewBinding>(val binding:VB):RecyclerView.ViewHolder(binding.root) {}