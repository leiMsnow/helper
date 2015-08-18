package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

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
        switch (item.getGroup_type()) {
            case GroupType.ACTIVITY:
                helper.setText(R.id.tv_group_status, "活动圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_yellow));
                break;
            case GroupType.AGE:
                helper.setText(R.id.tv_group_status, "同龄圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_pink));
                break;
            case GroupType.CITY:
                helper.setText(R.id.tv_group_status, "同城圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_deep_purple));
                break;
            case GroupType.CLASSMATE:
                helper.setText(R.id.tv_group_status, "同学圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_light_blue));
                break;
            case GroupType.LIFE:
                helper.setText(R.id.tv_group_status, "生活圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_light_green));
                break;
            case GroupType.PRIVATE:
                helper.setText(R.id.tv_group_status, "私密圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_yellow));
                break;
            case GroupType.TALENT:
                helper.setText(R.id.tv_group_status, "达人圈");
                helper.setTextColor(R.id.tv_group_status,
                        mContext.getResources().getColor(R.color.theme_yellow));
                break;

        }
        helper.setImageBitmap(R.id.iv_group_icon, item.getGroup_avatar());
        helper.setText(R.id.tv_group_name, item.getGroup_name());
        helper.setText(R.id.tv_group_introduce, item.getDeclaration());
        helper.setVisible(R.id.btn_join, View.GONE);
    }
}
