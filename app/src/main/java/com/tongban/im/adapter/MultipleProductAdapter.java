package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.Group;
import com.tongban.im.model.MultipleProduct;

import java.util.List;

/**
 * 专题页
 * @author  zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductAdapter extends QuickAdapter<MultipleProduct> {


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private View.OnClickListener onClickListener;

    public MultipleProductAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, MultipleProduct item) {
        helper.setImageBitmap(R.id.iv_product,
                "http://www.qqzhi.com/uploadpic/2015-01-16/121337592.jpg");
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        helper.getView(R.id.iv_product).setLayoutParams(localObject);
    }


}