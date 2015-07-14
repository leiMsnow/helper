package com.tongban.corelib.base.adapter;

/**
 * 多item支持接口
 *
 * @param <T>
 */
public interface IMultiItemTypeSupport<T> {
    int getLayoutId(int position, T t);

    int getViewTypeCount();

    int getItemViewType(int position, T t);
}