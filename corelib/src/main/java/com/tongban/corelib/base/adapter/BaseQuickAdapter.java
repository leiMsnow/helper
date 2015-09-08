package com.tongban.corelib.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 该类继承自BaseAdapter，完成BaseAdapter中部分通用抽象方法的编写，类似ArrayAdapter.
 * 该类声明了两个泛型，一个是我们的Bean（T），一个是BaseAdapterHelper(H)主要用于扩展BaseAdapterHelper时使用。
 * Created by zhangleilei on 15/6/4.
 */
public abstract class BaseQuickAdapter<T, H extends BaseAdapterHelper> extends BaseAdapter {

    protected Context mContext;
    //资源布局
    protected int mLayoutResId;
    //数据集合
    protected List<T> mData;
    protected IMultiItemTypeSupport<T> mMultiItemTypeSupport;

    public BaseQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.mContext = context;
        this.mLayoutResId = layoutResId;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
    }

    //新增可以扩展item的构造参数
    public BaseQuickAdapter(Context context, List<T> data, IMultiItemTypeSupport<T> multiItemTypeSupport) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    //---------------------------用与实现adapter方法-start-----------------------------------

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int i) {
        if (i >= mData.size())
            return null;
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        if (mMultiItemTypeSupport != null)
            return mMultiItemTypeSupport.getViewTypeCount();
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, mData.get(position));
        }
        return position >= mData.size() ? 0 : 1;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final H helper = getAdapterHelper(i, view, viewGroup);
        if (helper.isFirstCreate()) {
            onFirstCreateView(helper);
        }
        helper.setmPosition(i);
        T item = getItem(i);
        //对外公布的convert抽象方法
        convert(helper, item);
        return helper.getConvertView();
    }

    //---------------------------用与实现adapter方法-end-----------------------------------


    //-------------------------------用与操作data-start-----------------------------------


    public void add(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void add(T item, boolean isChanged) {
        mData.add(item);
        if (isChanged)
            notifyDataSetChanged();
    }

    public void add(int index, T item) {
        mData.add(index, item);
        notifyDataSetChanged();
    }

    public void addAll(List<T> items) {
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(T item) {
        mData.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void remove(int index, boolean isChange) {
        mData.remove(index);
        if (isChange)
            notifyDataSetChanged();
    }


    public void set(T oldItem, T newItem) {
        set(mData.indexOf(oldItem), newItem);
    }

    public void set(int index, T item) {
        mData.set(index, item);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> items) {
        mData.clear();
        addAll(items);
    }

    public boolean contains(T item) {
        return mData.contains(item);
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataAll() {
        return mData;
    }
    //-------------------------------用与操作data-end-----------------------------------

    @Override
    public boolean isEnabled(int position) {
        return position < mData.size();
    }

    /**
     * 这个抽象方法需要自行实现视图与数据的绑定
     *
     * @param helper 视图控件
     * @param item   数据
     */
    protected abstract void convert(H helper, T item);


    protected abstract H getAdapterHelper(int position, View view, ViewGroup viewGroup);

    /**
     * 是否第一次创建view，可以用此方法来创建嵌套adapter的布局
     * @param helper
     */
    protected abstract void onFirstCreateView(H helper);

}
