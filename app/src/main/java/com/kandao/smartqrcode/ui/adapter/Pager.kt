package com.kandao.smartqrcode.ui.adapter

enum class Pager {
    SCAN, MAKE, SET;

    companion object {

        fun getPagerFromPosition(position: Int): Pager {
            return when (position) {
                0 -> SCAN
                1 -> MAKE
                2 -> SET
                else -> SCAN
            }
        }
    }
}