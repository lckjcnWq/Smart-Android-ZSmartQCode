package com.theswitchbot.common.interfaces

interface OnItemClickListener<T> {

    fun onItemClick(item: T, position: Int)

}