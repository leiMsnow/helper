package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Topic;

import java.util.List;

/**
 * 话题适配器
 * Created by fushudi on 2015/7/16.
 */
public class TopicAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    public TopicAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        //用户信息
        helper.setImageBitmap(R.id.iv_user_portrait, item.getUser_info().getPortrait_url().getMin(),
                R.drawable.rc_default_portrait);
        helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
        helper.setText(R.id.tv_birthday,
                String.valueOf(item.getUser_info().getChild_info().get(0).getAge()) + "岁" +
                        item.getUser_info().getChild_info().get(0).getSex() + "宝宝");
        helper.setText(R.id.tv_create_time, item.getC_time(mContext));
        //话题内容
        helper.setText(R.id.tv_topic_title, item.getTopic_title());
        helper.setText(R.id.tv_topic_content, item.getTopic_content());

        setImagesVisibleAndUrl(helper, item);
        //回复、收藏、地址
        helper.setText(R.id.tv_reply_count, String.valueOf(item.getCollect_amount()));
        helper.setText(R.id.tv_collect_count, String.valueOf(item.getComment_amount()));
    }

    //设置图片的显示/隐藏和src
    private void setImagesVisibleAndUrl(final BaseAdapterHelper helper, final Topic item) {
        for (int i = 0; i < item.getTopic_img_url().size(); i++) {
            if (item.getContentType() == Topic.TEXT) {
                helper.setVisible(images[i], View.GONE);
            } else {
                helper.setVisible(images[i], View.VISIBLE);
                helper.setImageBitmap(images[i],
                        item.getTopic_img_url().get(i).getMin(),R.drawable.rc_ic_def_rich_content);
                helper.setTag(images[i], Integer.MAX_VALUE,
                        item.getTopic_img_url());
                helper.setOnClickListener(images[i], onClickListener);
            }
        }
    }
}
