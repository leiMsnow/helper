package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;

import java.util.List;

/**
 * 创建话题添加图片Adapter
 * Created by fushudi on 2015/7/16.
 */
public class CreateTopicImgAdapter extends QuickAdapter<String> {

    public CreateTopicImgAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(final BaseAdapterHelper helper, final String item) {
        if (TextUtils.isEmpty(item)) {
            helper.setImageBitmap(R.id.iv_topic_img, R.mipmap.ic_menu_add);
        } else {
            helper.setImageBitmap(R.id.iv_topic_img, item);
        }
        helper.setOnClickListener(R.id.iv_topic_img, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(item) && mData.size() <= 2) {
                    add(0, "http://img2.3lian.com/2014/f7/5/d/22.jpg");

                } else if (TextUtils.isEmpty(item) && mData.indexOf(item) == 2) {
                    //替换
                    set(mData.indexOf(item), "http://www.touxiang.cn/uploads/20120723/23-033215_282.jpg");
                }
            }
        });
    }
}
