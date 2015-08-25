package com.tongban.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.ProductBook;

import java.util.List;

/**
 * 我的收藏 - 单品列表
 * Created by fushudi on 2015/8/13.
 */
public class SingleProductAdapter extends QuickAdapter<ProductBook> {
    public SingleProductAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    protected void convert(BaseAdapterHelper helper, ProductBook item) {
        helper.setImageBitmap(R.id.iv_product, item.getProduct_img_url().get(0).getMin(),
                R.drawable.rc_ic_def_rich_content);
        helper.setText(R.id.tv_product_name, item.getProduct_name());
        helper.setText(R.id.tv_product_content, item.getProduct_tags());
    }


    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams lp = helper.getView(R.id.iv_product).getLayoutParams();
//        lp.width = mScreenWidth / 2;
        lp.height = mScreenWidth / 7 * 3;
        helper.getView(R.id.iv_product).setLayoutParams(lp);
    }
}
