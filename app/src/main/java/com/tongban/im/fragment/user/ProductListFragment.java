package com.tongban.im.fragment.user;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.LogUtil;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.adapter.ProductBookAdapter;
import com.tongban.im.api.ProductApi;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 单品列表页
 *
 * @author fushudi
 */
public class ProductListFragment extends BaseApiFragment implements AdapterView.OnItemClickListener {
    private ProductBookAdapter mAdapter;
    private GridView mGridView;

    /**
     * 构造方法
     *
     * @param type 类型 0:搜索单品列表  1:收藏单品列表
     * @return ProductListFragment
     */
    public static ProductListFragment newInstance(int type) {
        ProductListFragment f = new ProductListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        f.setArguments(args);
        return f;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_product_list;
    }

    @Override
    protected void initView() {
        mGridView = (GridView) mView.findViewById(R.id.gv_product);
    }

    @Override
    protected void initListener() {
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mAdapter = new ProductBookAdapter(mContext, R.layout.item_product_list, null);
        mGridView.setAdapter(mAdapter);
        int type = getArguments().getInt("type", 0);
        if (type == 0) {
            // 用于搜索
        } else {
            // 用于获取收藏的单品列表
            UserCenterApi.getInstance().fetchCollectedProductList(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startProductBook(mAdapter.getItem(position).getProduct_id());
    }

    /**
     * 获取收藏的单品列表Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchCollectedProductEvent event) {
        mAdapter.replaceAll(event.getProductBookList());
    }

    /**
     * 通知Fragment执行搜索
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchThemeAndProductEvent event) {
        ProductApi.getInstance().searchProduct(event.keyword, 0, 15, this);
    }

    /**
     * 搜索单品成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchProductResultEvent event) {
        mAdapter.replaceAll(event.mProductBooks);
    }
}
