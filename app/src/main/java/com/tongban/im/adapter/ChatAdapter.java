package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * Created by fushudi on 2015/7/16.
 */
public class ChatAdapter extends QuickAdapter<Topic> {

    public ChatAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        helper.setText(R.id.tv_chat_name,item.getTopicName());
        helper.setText(R.id.tv_chat_num,item.getTopicPersonNum());
        helper.setText(R.id.tv_chat_context,item.getTopicContext());
    }
}
