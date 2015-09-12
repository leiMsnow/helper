package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.model.User;

import java.util.List;

/**
 * 粉丝和关注列表  adapter
 * Created by fushudi on 2015/8/15.
 */
public class UserAdapter extends QuickAdapter<User> {
    private View.OnClickListener onClickListener;
    private boolean isFocused = false;

    public void setIsFocused(boolean isFocused) {
        this.isFocused = isFocused;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public UserAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, User item) {
        if (item.getPortrait_url() != null) {
            helper.setImageBitmap(R.id.iv_user_icon, item.getPortrait_url().getMin());
        } else {
            helper.setImageResource(R.id.iv_user_icon, Consts.getUserDefaultPortrait());
        }
        helper.setTag(R.id.iv_user_icon,item.getUser_id());
        helper.setText(R.id.tv_user_name, item.getNick_name());
        helper.setTag(R.id.btn_follow, item.getUser_id());
        if (isFocused) {
            helper.setText(R.id.btn_follow, "已关注");
            helper.getView(R.id.btn_follow).setEnabled(false);
        } else {
            if (item.is_focused()) {
                helper.setText(R.id.btn_follow, "已关注");
                helper.getView(R.id.btn_follow).setEnabled(false);
            } else {
                helper.setText(R.id.btn_follow, "关注");
                helper.getView(R.id.btn_follow).setEnabled(true);
            }
        }
        helper.setTag(R.id.iv_user_icon, Integer.MAX_VALUE, item.getUser_id());
        helper.setOnClickListener(R.id.iv_user_icon, onClickListener);
        helper.setOnClickListener(R.id.btn_follow, onClickListener);

    }
}
