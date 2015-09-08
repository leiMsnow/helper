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
        if (item.getUser_info() != null) {
            if (item.getUser_info().getPortrait_url() != null) {
                helper.setTag(R.id.iv_user_portrait,
                        Integer.MAX_VALUE, item.getUser_info().getUser_id());
                helper.setImageBitmap(R.id.iv_user_portrait, item.getUser_info().getPortrait_url().getMin(),
                        R.drawable.rc_default_portrait);
                helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
            } else {
                helper.setImageBitmap(R.id.iv_user_portrait, R.drawable.rc_default_portrait);
            }
            helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
            helper.setText(R.id.tv_comment_time, item.getC_time(mContext));
            helper.setText(R.id.tv_comment_content, item.getComment_content());
            helper.setText(R.id.tv_topic_content, item.getTopic_info().getTopic_title());
            helper.setTag(R.id.tv_comment, item);
            helper.setOnClickListener(R.id.tv_comment, onClickListener);

        }
    }
}
