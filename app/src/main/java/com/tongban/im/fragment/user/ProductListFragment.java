package com.tongban.im.fragment.user;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tb.api.ProductApi;
import com.tb.api.UserCenterApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.utils.TransferCenter;
import com.tongban.corelib.model.ApiErrorResult;
import com.tongban.im.R;
import com.tongban.im.adapter.ProductBookAdapter;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import butterknife.OnItemClick;

/**
 * 单品列表页
 *
 * @author fushudi
 */
public class ProductListFragment extends AppBaseFragment
        implements AdapterView.OnItemClickListener {

    @Bind(R.id.gv_product)
    GridView mGridView;

    private ProductBookAdapter mAdapter;

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
    protected void initData() {
        mAdapter = new ProductBookAdapter(mContext, R.layout.item_product_list, null);
        mGridView.setAdapter(mAdapter);
        int type = getArguments().getInt("type", 0);
        if (type == 0) {
            // 用于搜索
        } else {
            // 用于获取收藏的单品列表
            UserCenterApi.getInstance().fetchCollectedProductList(0, 10, this);
        }
    }

    @OnItemClick(R.id.gv_product)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startProductBook(mAdapter.getItem(position).getProduct_id());
    }

    public void searchProduct(String keyword) {
        ProductApi.getInstance().searchProduct(keyword, 0, 15, this);
    }

    /**
     * 获取收藏的单品列表Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.FetchCollectedProductEvent event) {
        mAdapter.addAll(event.productBookList);
        mGridView.setVisibility(View.VISIBLE);
    }

    /**
     * 错误回调Event
     * @param obj
     */
    public void onEventMainThread(ApiErrorResult obj) {
        if (obj.getApiName().equals(ProductApi.SEARCH_PRODUCT)) {
            mAdapter.clear();
            mGridView.setVisibility(View.GONE);
        }
    }

    /**
     * 搜索单品成功的Event
     *
     * @param event
     */
    public void onEventMainThread(BaseEvent.SearchProductResultEvent event) {
        mAdapter.replaceAll(event.mProductBooks);
        mGridView.setVisibility(View.VISIBLE);

    }
}
