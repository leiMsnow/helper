package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Group;

import java.util.List;

/**
 * 个人中心--我的圈子Adapter
 * Created by fushudi on 2015/8/18.
 */
public class MyGroupAdapter extends QuickAdapter<Group> {
    public MyGroupAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Group item) {
        helper.setImageBitmap(R.id.iv_group_icon, item.getGroup_avatar());
        helper.setText(R.id.tv_group_name, item.getGroup_name());
        helper.setText(R.id.tv_group_status, item.getTags());
        helper.setText(R.id.tv_group_introduce, item.getDeclaration());
        helper.setVisible(R.id.btn_join, View.GONE);
    }
}
