package com.tongban.im.activity.base;

import android.net.Uri;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.tongban.im.R;
import com.tongban.im.api.TopicApi;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.Topic;

/**
 * 通用的topicDetails父类
 * 裁剪
 * 拍照
 * 相册
 * Created by fushudi on 2015/8/13.
 */
public abstract class TopicDetailsBaseActivity extends CommonImageResultActivity {

    protected MenuItem menuItem;
    protected Topic mTopicInfo;
    protected String mTopicId;

    protected int mCursor = 0;
    protected int mPage = 10;
    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTopicId = uri.getQueryParameter(Consts.KEY_TOPIC_ID);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_topic_detail, menu);
        menuItem = menu.findItem(R.id.menu_collect);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_collect) {
            menuItem.setEnabled(false);
            TopicApi.getInstance().collectTopic(!mTopicInfo.isCollect_status(), mTopicId, this);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(BaseEvent.TopicInfoEvent topicInfoEvent) {
        mTopicInfo = topicInfoEvent.topic;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTopicInfo.isCollect_status()) {
                    menuItem.setIcon(R.mipmap.ic_menu_collected);
                } else {
                    menuItem.setIcon(R.mipmap.ic_menu_collect);
                }
            }
        }, 500);
    }

    /**
     * 收藏话题事件回调
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.TopicCollect obj) {
        mTopicInfo.setCollect_status(obj.status);
        menuItem.setEnabled(true);
        if (obj.status) {
            menuItem.setIcon(R.mipmap.ic_menu_collected);
        } else {
            menuItem.setIcon(R.mipmap.ic_menu_collect);
        }
    }

}
