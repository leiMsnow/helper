package com.tongban.im.adapter;

import android.content.Context;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.TopicReply;

import java.util.List;

/**
 * 话题评论Adapter
 * Created by fushudi on 2015/8/1.
 */
public class TopicReplyAdapter extends QuickAdapter<TopicReply> {
    public TopicReplyAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TopicReply item) {
//        helper.setImageBitmap(R.id.iv_user_icon, "http://b.hiphotos.baidu.com/image/pic/item/dbb44aed2e738bd4a244792ca38b87d6277ff942.jpg");
//        helper.setText(R.id.tv_user_name, item.getReplyNickName());
//        helper.setText(R.id.tv_reply_time, item.getReplyTime());
//        helper.setText(R.id.tv_reply_content, item.getReplyContent());
    }
}
