package com.tongban.corelib.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.tongban.corelib.utils.LogUtil;

import java.util.List;

import static com.tongban.corelib.base.adapter.BaseAdapterHelper.get;

/**
 * 继承自 BaseHelperAdapter
 * 用于提供一个快速使用的Adapter。一般情况下直接用此类作为Adapter即可，
 * 但是如果你扩展了BaseAdapterHelper，可能就需要自己去继承BaseAdapterHelper实现自己的Adapter。
 * 所以该类，对于getAdapterHelper直接返回了BaseAdapterHelper。
 * Created by zhangleilei on 15/6/8.
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, BaseAdapterHelper>  {

    public QuickAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public QuickAdapter(Context context, List data, IMultiItemTypeSupport multiItemTypeSupport) {
        super(context, data, multiItemTypeSupport);
    }

    @Override
    protected BaseAdapterHelper getAdapterHelper(int position, View view, ViewGroup viewGroup) {
        if (mMultiItemTypeSupport != null) {
            return get(mContext, view, viewGroup,
                    mMultiItemTypeSupport.getLayoutId(position, mData.get(position)));
        }
        return get(mContext, view, viewGroup, mLayoutResId);
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
    }
}
