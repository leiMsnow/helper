package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;
import com.tongban.im.model.Theme;

import java.util.List;

/**
 * 专题页
 *
 * @author zhangleilei
 * @createTime 2015/8/13
 */
public class MultipleProductAdapter extends QuickAdapter<Theme> {


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private View.OnClickListener onClickListener;

    public MultipleProductAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Theme item) {
        helper.setImageBitmap(R.id.iv_product, item.getTheme_img_url().get(0).getMin());
        helper.setText(R.id.tv_product_desc, item.getTheme_content());
        helper.setText(R.id.tv_product_name, item.getTheme_title());
        helper.setText(R.id.tv_collect_count, item.getCollect_amount());

    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        int mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth,
                (int) (3.0F * (mScreenWidth / 4.0F)));
        helper.getView(R.id.iv_product).setLayoutParams(localObject);
    }
}
