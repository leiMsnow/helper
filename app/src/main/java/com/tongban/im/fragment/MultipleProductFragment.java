package com.tongban.im.fragment;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.MultipleProductAdapter;

/**
 * 专题查询结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductFragment extends BaseApiFragment implements AdapterView.OnItemClickListener {


    private ListView lvMultipleProduct;

    private MultipleProductAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mutiple_product;
    }

    @Override
    protected void initView() {
        lvMultipleProduct = (ListView) mView.findViewById(R.id.lv_multiple_product);

        mAdapter = new MultipleProductAdapter(mContext,
                R.layout.item_multiple_product_list, null);
        lvMultipleProduct.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        lvMultipleProduct.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
