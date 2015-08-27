package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.SingleProductAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 单品查询结果页
 *
 * @author fushudi
 */
public class SingleProductFragment extends BaseApiFragment implements AdapterView.OnItemClickListener{
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
        gvSingleProductList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mAdapter = new SingleProductAdapter(mContext, R.layout.item_single_product_list, null);
        gvSingleProductList.setAdapter(mAdapter);
        UserCenterApi.getInstance().fetchSingleProductList(this);
    }

    public void onEventMainThread(BaseEvent.FetchCollectedProductEvent obj) {

        mAdapter.replaceAll(obj.getSingleProductList());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TransferCenter.getInstance().
    }
}
