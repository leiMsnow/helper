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
public class TopicAdapter extends QuickAdapter<Topic> {

    public TopicAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Topic item) {
        helper.setText(R.id.tv_topic_name, item.getTopicName());
        helper.setText(R.id.tv_topic_num, item.getTopicReplyNum());
        helper.setText(R.id.tv_topic_content, item.getTopicContent());
        helper.setImageBitmap(R.id.iv_user_icon, "http://b.hiphotos.baidu.com/image/pic/item/023b5bb5c9ea15ce7f13d886b4003af33b87b2bb.jpg");
    }
}
