package com.theswitchbot.common.widget.dialog

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.theswitchbot.common.databinding.DialogBottomItemsBinding
import com.theswitchbot.common.databinding.ItemBottomItemsBinding
import com.theswitchbot.common.widget.adapter.BindingAdapter
import com.theswitchbot.common.widget.adapter.BindingViewHolder

/**
 * Author zrh
 * Date 2022/2/17 6:49 下午
 * Description 底部选项卡弹窗
 */
class BottomItemsDialog(activity: Activity) : BottomDialog<DialogBottomItemsBinding>(activity) {

    private var mAdapter = ItemAdapter()
    private var onAction: ((Item) -> Unit)? = null

    init {
        binding.btnCancel.setOnClickListener { dismiss() }
        binding.rvItems.layoutManager = LinearLayoutManager(activity)
        binding.rvItems.adapter = mAdapter
        mAdapter.setListener { _, _, data ->
            dismiss()
            onAction?.invoke(data)
        }
    }

    fun show(items: List<Item>, action: (Item) -> Unit) {
        mAdapter.setList(items)
        onAction = action
        show()
    }

    data class Item(val name: String, val data: Any?)

    class ItemAdapter : BindingAdapter<ItemBottomItemsBinding, Item>() {
        override fun onRender(holder: BindingViewHolder<ItemBottomItemsBinding>, item: Item) {
            holder.binding.tvItem.text = item.name
            holder.itemView.setOnClickListener {
                postAction(it, 0, item)
            }
        }
    }
}