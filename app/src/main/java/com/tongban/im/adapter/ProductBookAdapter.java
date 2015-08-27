package com.tongban.im.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.Product;
import com.tongban.im.model.ProductBook;

import java.util.List;

/**
 * 我的收藏 - 单品列表Adapter
 * Created by fushudi on 2015/8/13.
 */
public class ProductBookAdapter extends QuickAdapter<ProductBook> {
    public ProductBookAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    protected void convert(BaseAdapterHelper helper, ProductBook item) {
        helper.setImageBitmap(R.id.iv_product, item.getProduct_img_url().get(0).getMid());
        helper.setText(R.id.tv_product_name, item.getProduct_name());
        helper.setText(R.id.tv_product_content, item.getBook_content_desc());
    }


    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        ViewGroup.LayoutParams lp = helper.getView(R.id.iv_product).getLayoutParams();
        lp.height = mScreenWidth / 2;
        lp.height = mScreenWidth / 7 * 3;
        helper.getView(R.id.iv_product).setLayoutParams(lp);
    }
}
