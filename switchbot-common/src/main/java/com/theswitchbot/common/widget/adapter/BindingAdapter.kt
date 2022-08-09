package com.theswitchbot.common.widget.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.theswitchbot.common.util.ViewBindingUtils

/**
 * Author zrh
 * Date 2022/2/17 6:19 下午
 * Description 只支持单种数据类型的列表
 */
abstract class BindingAdapter<VB : ViewBinding, T> : RecyclerView.Adapter<BindingViewHolder<VB>>() {

    private lateinit var inflater: LayoutInflater
    private val mList = ArrayList<T>()
    private var onAction: ((view: View, type: Int, data: T) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<T>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<T>) {
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun setListener(action: (view: View, type: Int, data: T) -> Unit) {
        onAction = action
    }

    protected fun postAction(view: View, type: Int, data: T) {
        onAction?.invoke(view, type, data)
    }

    override fun getItemCount():Int {
        return mList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<VB> {

        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        val binding = ViewBindingUtils.binding<VB>(this, inflater, parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder<VB>, position: Int) {
        val item = mList[position]
        onRender(holder, item)
    }

    abstract fun onRender(holder: BindingViewHolder<VB>, item: T)
}