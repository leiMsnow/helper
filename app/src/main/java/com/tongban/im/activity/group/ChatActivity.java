package com.tongban.im.activity.group;

import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tb.api.GroupApi;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.user.User;
import com.tb.api.utils.TransferCenter;
import com.tongban.im.R;
import com.tongban.im.activity.SecondDetailsActivity;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.MemberGridAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import io.rong.imkit.model.Event;

/**
 * 聊天界面
 *
 * @author zhangleilei
 * @createTime 2015/7/16
 */
public class ChatActivity extends AppBaseActivity {

    private String mTitle;
    private String mTargetId;


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
            if (!TextUtils.isEmpty(mTitle)) {
                setTitle(mTitle);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_user_info) {
            SecondDetailsActivity.startDetailsActivity(this
                    , SecondDetailsActivity.TALENT_DETAILS
                    , mTargetId);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(BaseEvent.QuitGroupEvent obj) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

}
