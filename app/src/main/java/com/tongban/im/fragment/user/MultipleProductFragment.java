package com.tongban.im.fragment.user;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.activity.discover.MultiProductActivity;
import com.tongban.im.activity.topic.OfficialTopicDetailsActivity;
import com.tongban.im.adapter.MultipleProductAdapter;
import com.tongban.im.model.MultiProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题查询结果页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductFragment extends BaseApiFragment implements View.OnClickListener
,AdapterView.OnItemClickListener{


    private ListView lvMultipleProduct;

    private MultipleProductAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mutiple_product;
    }

    @Override
    protected void initView() {
        lvMultipleProduct = (ListView) mView.findViewById(R.id.lv_multiple_product);

        List<MultiProduct> data = new ArrayList<>();
        data.add(new MultiProduct());
        mAdapter = new MultipleProductAdapter(mContext,
                R.layout.item_multiple_product_list, data);
        lvMultipleProduct.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setOnClickListener(this);
        lvMultipleProduct.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_product){
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mContext.startActivity(new Intent(mContext, MultiProductActivity.class));
    }
}
