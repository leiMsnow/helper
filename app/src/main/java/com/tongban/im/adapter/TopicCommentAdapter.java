package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.TopicComment;

import java.util.List;

/**
 * 话题评论Adapter
 * Created by fushudi on 2015/8/1.
 */
public class TopicCommentAdapter extends QuickAdapter<TopicComment> {
    public TopicCommentAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TopicComment item) {
//        helper.setImageBitmap(R.id.iv_user_portrait, item.getUser_info().getPortrait_url().getMid());
//        helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
        helper.setText(R.id.tv_comment_content, item.getComment_content());
        helper.setText(R.id.tv_comment_time, item.getC_time(mContext));

//        helper.setTag(R.id.iv_user_portrait,Integer.MAX_VALUE,item.getUser_info().getUser_id());
        helper.setOnClickListener(R.id.iv_user_portrait,onClickListener);
    }
}
