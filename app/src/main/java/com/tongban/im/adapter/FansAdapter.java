package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.User;

import java.util.List;

/**
 * Created by fushudi on 2015/8/15.
 */
public class FansAdapter extends QuickAdapter<User> {

    public FansAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, User item) {
        helper.setImageBitmap(R.id.iv_user_icon, item.getPortrait_url().getMin());
        helper.setText(R.id.tv_nickname, item.getNick_name());
    }
}
