package com.theswitchbot.common.interfaces;

/**
 * list回调
 * @author wuhongyang
 */
public interface OnListItemInteractionListener<T> {
    void onListItemClick(T item, int position);
    void onListItemLongClick(T item, int position);
    void onListItemDeleted(T item);
    void onListItemUpdate(T item, int position);
}
