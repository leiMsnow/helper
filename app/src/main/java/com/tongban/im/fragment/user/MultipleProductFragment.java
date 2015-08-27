package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.MultipleProductAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 专题查询结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductFragment extends BaseApiFragment implements View.OnClickListener
        , AdapterView.OnItemClickListener {
    private ListView lvMultipleProduct;

    private MultipleProductAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mutiple_product;
    }

    @Override
    protected void initView() {
        lvMultipleProduct = (ListView) mView.findViewById(R.id.lv_multiple_product);

    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);
        lvMultipleProduct.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        mAdapter = new MultipleProductAdapter(mContext,
                R.layout.item_multiple_product_list, null);
        lvMultipleProduct.setAdapter(mAdapter);
        UserCenterApi.getInstance().fetchCollectMultipleTopicList(0, 10, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_product) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startThemeDetails(mAdapter.getItem(position).getTheme_id());
    }

    public void onEventMainThread(BaseEvent.FetchCollectedThemeEvent obj) {

        mAdapter.replaceAll(obj.mThemeList);
    }
}
