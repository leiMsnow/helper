package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.GroupType;

import java.util.List;

/**
 * 选择圈子类型adapter
 * Created by zhangleilei on 15/7/22.
 */
public class ChoiceGroupTypeAdapter extends QuickAdapter<GroupType> {

    public ChoiceGroupTypeAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, GroupType item) {

        helper.setImageBitmap(R.id.iv_group_icon,item.getIcon());
        helper.setText(R.id.tv_group_type,item.getGroupType());
        helper.setText(R.id.tv_group_desc,item.getGroupDesc());

    }
}