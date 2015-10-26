package com.tongban.im.activity.group;

import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.tongban.im.R;
import com.tongban.im.activity.base.AppBaseActivity;
import com.tongban.im.adapter.MemberGridAdapter;
import com.tongban.im.api.GroupApi;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.user.User;

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

    @Bind(R.id.gv_members)
    GridView gvMembers;
    @Bind(R.id.members_parent)
    LinearLayout membersParent;

    private String mTitle;
    private String mTargetId;
    private boolean isPrivateChat = true;
    private MemberGridAdapter mMemberGridAdapter;


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

            mMemberGridAdapter = new MemberGridAdapter(mContext, R.layout.item_member_grid, null);
            gvMembers.setAdapter(mMemberGridAdapter);
            GroupApi.getInstance().getGroupMembersList(mTargetId, 0, 15, this);
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

    public void onEventMainThread(BaseEvent.GroupMemberEvent obj) {
        membersParent.setVisibility(View.VISIBLE);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            users.add(obj.users.get(0));
        }
        mMemberGridAdapter.addAll(users);

//        mMemberGridAdapter.addAll(obj.users);
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
