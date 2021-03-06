package com.tongban.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.tb.api.model.ImageUrl;
import com.tb.api.model.topic.Comment;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.Constants;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

import java.util.List;


/**
 * 话题评论Adapter
 * Created by fushudi on 2015/8/1.
 */
public class TopicCommentAdapter extends QuickAdapter<Comment> {

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    public TopicCommentAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    private View.OnClickListener onClickListener;

    private View.OnClickListener onImgClickListener;

    public void setOnImgClickListener(View.OnClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Comment item) {
        if (item.getUser_info() != null) {
            if (item.getUser_info().getPortraitUrl() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait,
                        item.getUser_info().getPortraitUrl().getMid());
            } else {
                helper.setImageResource(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }
            if (item.getCommentContent() != null
                    && item.getCommentContent().getComment_img_url() != null) {
                setImagesVisibleAndUrl(helper, item.getCommentContent().getComment_img_url());
                helper.setVisible(R.id.ll_small_img_parent, View.VISIBLE);
            } else {
                helper.setVisible(R.id.ll_small_img_parent, View.GONE);
            }

            helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
            String repliedName = TextUtils.isEmpty(item.getReplied_comment_id()) ? "" :
                    "回复 " + item.getReplied_nick_name();
            helper.setText(R.id.tv_comment_name, repliedName);

            //点击头像
            helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getUser_info().getUser_id());
            helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);

            //是自己就不显示回复
            if (SPUtils.get(mContext, Constants.USER_ID, "").toString().equals(
                    item.getUser_info().getUser_id())) {
                helper.setVisible(R.id.rl_comment_parent, View.GONE);
            } else {
                helper.setVisible(R.id.rl_comment_parent, View.VISIBLE);
                //回复
                helper.setTag(R.id.rl_comment_parent, item);
                helper.setOnClickListener(R.id.rl_comment_parent, onClickListener);
            }

        } else {
            helper.setText(R.id.tv_user_name, "");
            helper.setVisible(R.id.tv_comment, View.GONE);
        }
        if (item.getCommentContent() != null) {
            helper.setText(R.id.tv_comment_content, item.getCommentContent().getComment_content_text());
        }
        helper.setText(R.id.tv_comment_time, item.getC_time(mContext));
    }

    //设置图片的显示/隐藏和src
    private void setImagesVisibleAndUrl(final BaseAdapterHelper helper, final List<ImageUrl> item) {
        int count = item.size() > 3 ? 3 : item.size();
        for (int i = 0; i < images.length; i++) {
            helper.setVisible(images[i], View.INVISIBLE);
            if (i < count) {
                helper.setVisible(images[i], View.VISIBLE);
                helper.setImageBitmap(images[i],
                        item.get(i).getMin(), R.mipmap.ic_default_image);
                helper.setTag(images[i], Integer.MAX_VALUE, item);
                helper.setOnClickListener(images[i], onImgClickListener);
            }
        }
    }
}
