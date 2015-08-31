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
public class TopicListAdapter extends QuickAdapter<Topic> {

    private View.OnClickListener onClickListener;

    private int[] images = new int[]{R.id.iv_small_img_1, R.id.iv_small_img_2, R.id.iv_small_img_3};

    public TopicListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        //用户信息
        if (item.getUser_info().getPortrait_url() != null) {
            helper.setImageBitmap(R.id.iv_user_portrait,
                    item.getUser_info().getPortrait_url().getMin(),
                    R.drawable.rc_default_portrait);
        } else {
            helper.setImageBitmap(R.id.iv_user_portrait, R.drawable.rc_default_portrait);
        }

        helper.setText(R.id.tv_user_name, item.getUser_info().getNick_name());
        if (item.getUser_info().getChild_info() != null &&
                item.getUser_info().getChild_info().size() > 0) {
            helper.setText(R.id.tv_child_age,
                    String.valueOf(item.getUser_info().getChild_info().get(0).getAge()) + "岁" +
                            item.getUser_info().getChild_info().get(0).getSex() + "宝宝");
        } else {
            helper.setText(R.id.tv_child_age, "");
        }
        helper.setText(R.id.tv_create_time, item.getC_time(mContext));
        //话题内容
        helper.setText(R.id.tv_topic_title, item.getTopic_title());
        helper.setText(R.id.tv_topic_content, item.getTopic_content());
        if (item.getContentType() == Topic.IMAGE) {
            setImagesVisibleAndUrl(helper, item);
            helper.setVisible(R.id.ll_small_img_parent, View.VISIBLE);
        } else {
            helper.setVisible(R.id.ll_small_img_parent, View.GONE);
        }
        //回复、收藏、地址
        helper.setText(R.id.tv_comment_count, String.valueOf(item.getComment_amount()));
        helper.setText(R.id.tv_collect_count, String.valueOf(item.getCollect_amount()));

        helper.setTag(R.id.iv_user_portrait, Integer.MAX_VALUE, item.getUser_info().getUser_id());
        helper.setOnClickListener(R.id.iv_user_portrait, onClickListener);
    }

    //设置图片的显示/隐藏和src
    private void setImagesVisibleAndUrl(final BaseAdapterHelper helper, final Topic item) {
        int count = item.getTopic_img_url().size() > 3 ? 3 : item.getTopic_img_url().size();
        for (int i = 0; i < images.length; i++) {
            helper.setVisible(images[i], View.INVISIBLE);
            if (i < count) {
                helper.setVisible(images[i], View.VISIBLE);
                helper.setImageBitmap(images[i],
                        item.getTopic_img_url().get(i).getMin(), R.drawable.rc_ic_def_rich_content);
                helper.setTag(images[i], Integer.MAX_VALUE,
                        item.getTopic_img_url());
                helper.setOnClickListener(images[i], onClickListener);
            }
        }
    }
}
