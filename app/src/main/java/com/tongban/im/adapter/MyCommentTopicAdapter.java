package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.TopicComment;

import java.util.List;

/**
 * Created by fushudi on 2015/8/17.
 */
public class MyCommentTopicAdapter extends QuickAdapter<TopicComment> {
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MyCommentTopicAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TopicComment item) {
        helper.setImageBitmap(R.id.iv_user_portrait, item.getUser_info().getPortrait_url().getMin(),
                R.drawable.rc_default_portrait);
        helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
        helper.setText(R.id.tv_comment_time, item.getC_time(mContext));
        //TODO 缺少评论内容参数
        helper.setText(R.id.tv_comment_content, item.getComment_content());
        helper.setText(R.id.tv_topic_content, item.getTopic_info().getTopic_content());

        helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
        helper.setOnClickListener(R.id.tv_comment, onClickListener);
    }
}
