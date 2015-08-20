package com.tongban.im.fragment.user;


import android.widget.GridView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.SingleProductAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.model.ProductBook;

import java.util.List;

/**
 * 单品查询结果页
 *
 * @author fushudi
 */
public class SingleProductFragment extends BaseApiFragment {
    private SingleProductAdapter mAdapter;
    private GridView gvSingleProductList;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_single_product;
    }

    @Override
    protected void initView() {
        gvSingleProductList = (GridView) mView.findViewById(R.id.gv_single_product);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        UserCenterApi.getInstance().fetchSingleProductList(this);
    }

    public void onEventMainThread( List<ProductBook> singleProductList) {

        mAdapter = new SingleProductAdapter(mContext, R.layout.item_single_product_list, singleProductList);
        gvSingleProductList.setAdapter(mAdapter);
    }
}
