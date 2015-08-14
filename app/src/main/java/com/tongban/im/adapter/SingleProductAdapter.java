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
                "http://b.hiphotos.baidu.com/image/w%3D310/sign=7e618bd474c6a7efb926ae27cdfbafe9/fc1f4134970a304ebc6f8ef1d3c8a786c9175c6f.jpg");
    }

    @Override
    public int getCount() {
        return 10;
    }
}
