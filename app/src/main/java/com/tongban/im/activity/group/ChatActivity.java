package com.tongban.im.activity.group;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tongban.im.R;
import com.tongban.im.activity.base.BaseToolBarActivity;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

import io.rong.imkit.model.Event;

/**
 * 聊天界面
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class ChatActivity extends BaseToolBarActivity  {

    private String mTargetId;
    private String mTitle;
    private boolean isPrivateChat = true;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            Uri uri = getIntent().getData();
            mTargetId = uri.getQueryParameter("targetId");
            mTitle = uri.getQueryParameter("title");
            isPrivateChat = uri.toString().contains("private");
            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        MenuItem item = menu.findItem(R.id.menu_group_info);
        if (item != null) {
            if (!isPrivateChat)
                item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_group_info) {
            TransferCenter.getInstance().startGroupInfo(mTargetId, false);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(BaseEvent.QuitGroupEvent obj) {
        finish();
    }


    public void onEventMainThread(Event.LastTopicNameEvent topicNameEvent) {
    }
}
