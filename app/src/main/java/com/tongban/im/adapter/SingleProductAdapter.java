package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.SingleProduct;

import java.util.List;

/**
 * Created by fushudi on 2015/8/13.
 */
public class SingleProductAdapter extends QuickAdapter<SingleProduct> {
    public SingleProductAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, SingleProduct item) {
        helper.setImageBitmap(R.id.iv_product,
                "http://d.hiphotos.baidu.com/image/pic/item/7acb0a46f21fbe096830b61569600c338744ad40.jpg");
    }

    @Override
    public int getCount() {
        return 10;
    }
}
