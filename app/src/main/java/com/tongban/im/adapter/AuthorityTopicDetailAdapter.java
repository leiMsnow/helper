package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Product;

import java.util.List;

/**
 *官方话题评论Adapter
 * Created by fushudi on 2015/8/10.
 */
public class AuthorityTopicDetailAdapter extends QuickAdapter<Product> {
    public AuthorityTopicDetailAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Product item) {
        helper.setImageBitmap(R.id.iv_product_icon, "http://b.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4a244792ca38b87d6277ff942.jpg");
        helper.setText(R.id.tv_product_advantage_content, item.getProductAdvantage());
        helper.setText(R.id.tv_product_disadvantage_content, item.getProductDisAdvantage());
        helper.setText(R.id.tv_product_introduce_content, item.getProductIntroduction());
        helper.setText(R.id.tv_product_parameters_content, item.getProductParameter());
        helper.setText(R.id.tv_product_name, item.getProductName());
        helper.setText(R.id.tv_collect_num, item.getProductCollectNum());
    }
}