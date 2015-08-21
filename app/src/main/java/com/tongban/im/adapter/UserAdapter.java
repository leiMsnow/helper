package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.User;

import java.util.List;

/**
 * 粉丝和关注列表  adapter
 * Created by fushudi on 2015/8/15.
 */
public class UserAdapter extends QuickAdapter<User> {
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public UserAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, User item) {
//        helper.setImageBitmap(R.id.iv_user_icon, item.getPortrait_url().getMin());
        helper.setText(R.id.tv_user_name, item.getNick_name());
        helper.setTag(R.id.btn_follow, item.getUser_id());
        helper.setTag(R.id.iv_user_icon, item.getUser_id());
        helper.setOnClickListener(R.id.iv_user_icon, onClickListener);
        helper.setOnClickListener(R.id.btn_follow,onClickListener);

    }
}
