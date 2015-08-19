package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.MultiProduct;

import java.util.List;

/**
 * 专题页
 * @author  zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductAdapter extends QuickAdapter<MultiProduct> {


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private View.OnClickListener onClickListener;

    public MultipleProductAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }
    @Override
    protected void convert(BaseAdapterHelper helper, MultiProduct item) {
        helper.setImageBitmap(R.id.iv_product,
                "http://www.qqzhi.com/uploadpic/2015-01-16/121337592.jpg");

    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        helper.getView(R.id.iv_product).setLayoutParams(localObject);
    }
}
