package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * Created by fushudi on 2015/8/17.
 */
public class MyReplyTopicAdapter extends QuickAdapter<Topic> {
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MyReplyTopicAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }
    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        helper.setImageBitmap(R.id.iv_topic_icon, item.getTopicReply().getPortrait_url());
        helper.setText(R.id.tv_nickname, item.getTopicReply().getReplyNickName());
        helper.setText(R.id.tv_reply_time, item.getTopicReply().getReplyTime());
        helper.setText(R.id.tv_reply_content, item.getTopicReply().getReplyContent());
        helper.setText(R.id.tv_topic_content, item.getTopicContent());
    }
}
