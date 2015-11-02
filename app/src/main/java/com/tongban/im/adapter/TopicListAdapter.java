package com.tongban.im.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tb.api.model.topic.Topic;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicListAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;


    public TopicListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {

        if (item.getUser_info() != null) {
            //用户信息
            if (item.getUser_info().getPortraitUrl() != null) {
                helper.setImageBitmap(R.id.iv_user_portrait,
                        item.getUser_info().getPortraitUrl().getMin(),
                        Consts.getUserDefaultPortrait());
                helper.setTag(R.id.iv_user_portrait,
                        Integer.MAX_VALUE, item.getUser_info().getUser_id());
            } else {
                helper.setImageBitmap(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
            }

            helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
            //头像点击
            helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getUser_info().getUser_id());
            helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
        }
        helper.setText(R.id.tv_create_time, item.getC_time(mContext));
        helper.setText(R.id.tv_topic_title, item.getTopic_title());

        Drawable iconType = mContext.getResources().getDrawable(com.voice.tongban.R.color.transparent);

        if (item.getTopicContent() != null) {
            if (item.getTopicContent().getTopic_img_url() != null
                    && !TextUtils.isEmpty(item.getTopicContent().getTopic_content_voice())) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_voice_pic);

            } else if (item.getTopicContent().getTopic_img_url() != null) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_pic);

            } else if (!TextUtils.isEmpty(item.getTopicContent().getTopic_content_voice())) {
                iconType = mContext.getResources().getDrawable(com.voice.tongban.R.mipmap.ic_voice);
            }
        }
        iconType.setBounds(0, 0, iconType.getMinimumWidth(), iconType.getMinimumHeight());
        ((TextView) helper.getView(com.voice.tongban.R.id.tv_topic_title))
                .setCompoundDrawables(null, null, iconType, null);

        //回复、收藏、地址
        helper.setText(R.id.tv_comment_count, item.getComment_amount() + "人回答");
        helper.setText(R.id.tv_collect_count, item.getCollect_amount() + "人同问");
        setUserPortrait(helper, item);
    }

    private void setUserPortrait(BaseAdapterHelper helper, Topic item) {

        String[] userPortrait = new String[]{
                "http://img0.imgtn.bdimg.com/it/u=3069927012,2522642890&fm=23&gp=0.jpg",
                "http://img4.imgtn.bdimg.com/it/u=2065565183,3137255907&fm=23&gp=0.jpg",
                "http://img2.imgtn.bdimg.com/it/u=810719682,2467839254&fm=23&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=396080164,3772108535&fm=23&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=3559158573,1397744140&fm=23&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2623718032,2785881774&fm=23&gp=0.jpg",
                "http://img2.imgtn.bdimg.com/it/u=3393933311,3706599955&fm=23&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=4007298296,186568072&fm=23&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=3228140756,1828024847&fm=23&gp=0.jpg"
        };
        ((FlowLayout) helper.getView(R.id.fl_portrait_container)).setIsSingleLine(true);
        ((FlowLayout) helper.getView(R.id.fl_portrait_container)).removeAllViews();
        for (int i = 0; i < userPortrait.length; i++) {
            ImageView tv = (ImageView) LayoutInflater.from(mContext)
                    .inflate(R.layout.include_user_portrait
                            , ((FlowLayout) helper.getView(R.id.fl_portrait_container)), false);
            Glide.with(mContext)
                    .load(userPortrait[i])
                    .error(Consts.getUserDefaultPortrait())
                    .into(tv);
            ((FlowLayout) helper.getView(R.id.fl_portrait_container)).addView(tv);
        }
    }

}
