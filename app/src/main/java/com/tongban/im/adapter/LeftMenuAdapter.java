package com.tongban.im.adapter;

import android.content.Context;
import android.graphics.Color;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.BaseEvent;

import java.util.List;

/**
 * Created by zhangleilei on 15/7/5.
 */
public class LeftMenuAdapter extends QuickAdapter<BaseEvent.DrawerLayoutMenu> {

    public LeftMenuAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, BaseEvent.DrawerLayoutMenu item) {
        helper.setImageBitmap(R.id.sdv_icon, item.getIcon());
        helper.setText(R.id.tv_user_name, item.getText());
        helper.getConvertView().setBackgroundColor(Color.TRANSPARENT);
        if (item.isSelected()) {
            helper.getConvertView().setBackgroundResource(R.color.grey_300);
        }
    }

    public void setSelected(int position) {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).setSelected(false);
        }
        this.getItem(position).setSelected(true);
        notifyDataSetChanged();
    }

}
