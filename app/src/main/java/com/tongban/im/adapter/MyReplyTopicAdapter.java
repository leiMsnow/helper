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
        helper.setImageBitmap(R.id.iv_user_portrait, item.getUser_info().getPortrait_url().getMin());
        helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
        helper.setText(R.id.tv_reply_time, item.getM_time(mContext));
        //TODO 缺少评论内容参数
//        helper.setText(R.id.tv_reply_content, item.getTopicComment().getComment_content());
        helper.setText(R.id.tv_topic_content, item.getTopic_content());

        helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
        helper.setOnClickListener(R.id.tv_reply, onClickListener);
    }
}
