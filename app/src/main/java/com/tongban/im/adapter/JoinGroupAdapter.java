package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Group;

import java.util.List;

/**
 * 加入圈子-adapter
 * Created by zhangleilei on 15/7/22.
 */
public class JoinGroupAdapter extends QuickAdapter<Group> {


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private View.OnClickListener onClickListener;

    public JoinGroupAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Group item) {
        helper.setText(R.id.tv_group_name, item.getGroup_name());
        helper.setImageBitmap(R.id.iv_group_icon,
                "http://www.qqzhi.com/uploadpic/2015-01-16/121337592.jpg");
        //是否已经加入过
        if (item.is_joined()) {
            helper.setVisible(R.id.btn_join, View.GONE);
        } else {
            helper.setVisible(R.id.btn_join, View.VISIBLE);

            helper.setTag(R.id.btn_join, item);
            helper.setOnClickListener(R.id.btn_join, onClickListener);
        }
    }
}
