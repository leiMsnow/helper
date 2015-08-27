package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 关注界面
 *
 * @author fushudi
 */
public class FocusFragment extends BaseApiFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {
    private ListView lvFocusList;
    private UserAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_focus;
    }

    @Override
    protected void initView() {
        lvFocusList = (ListView) mView.findViewById(R.id.lv_focus_list);
    }

    @Override
    protected void initData() {
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        mAdapter.setIsFocused(true);
        lvFocusList.setAdapter(mAdapter);
        if (getArguments() != null) {
            String userID = getArguments().getString(Consts.USER_ID);
            UserCenterApi.getInstance().fetchFocusUserList(0, 10, userID, this);
        }
    }

    @Override
    protected void initListener() {
        lvFocusList.setOnItemClickListener(this);
        mAdapter.setOnClickListener(this);
    }

    /**
     * 我的关注列表Event
     */
    public void onEventMainThread(BaseEvent.MyFollowListEvent obj) {
        mAdapter.replaceAll(obj.myFollowList);
    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            for (int j = 0; j < obj.userIds.length; j++) {
                if (mAdapter.getItem(i).getUser_id().equals(obj.userIds[j])) {
                    mAdapter.remove(i);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消关注
            case R.id.btn_follow:
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(false, new String[]{focusId}, this);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startUserCenter(mAdapter.getItem(position).getUser_id());
    }
}
