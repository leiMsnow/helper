package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.fragment.base.BaseToolBarFragment;
import com.tongban.im.model.BaseEvent;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 关注界面
 *
 * @author fushudi
 */
public class FocusFragment extends BaseToolBarFragment implements
        OnLoadMoreListener {

    @Bind(R.id.lv_focus_list)
    LoadMoreListView lvFocusList;
    private UserAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;
    private String userID;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_focus;
    }

    @Override
    protected void initData() {
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        mAdapter.setIsFocused(true);
        lvFocusList.setAdapter(mAdapter);
        lvFocusList.setPageSize(mPageSize);
        if (getArguments() != null) {
            userID = getArguments().getString(Consts.USER_ID);
            UserCenterApi.getInstance().fetchFocusUserList(mCursor, mPageSize, userID, this);
            lvFocusList.setOnLoadMoreListener(this);
        }
    }

    /**
     * 获取我的关注列表Event
     */
    public void onEventMainThread(BaseEvent.MyFollowListEvent obj) {
        mCursor++;
        mAdapter.addAll(obj.myFollowList);
        lvFocusList.setResultSize(obj.myFollowList.size());
    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getItem(i).getUser_id().equals(obj.userIds)) {
                mAdapter.remove(i);
            }
        }
    }

    @OnClick({R.id.btn_follow, R.id.iv_user_icon})
    public void onClick(View v) {
        switch (v.getId()) {
            //取消关注
            case R.id.btn_follow:
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(false, focusId, this);
                break;
            case R.id.iv_user_icon:
                String userId = v.getTag(Integer.MAX_VALUE).toString();
                TransferCenter.getInstance().startUserCenter(userId);
                getActivity().finish();
                break;
        }
    }

    @OnItemClick(R.id.lv_focus_list)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startUserCenter(mAdapter.getItem(position).getUser_id());
        getActivity().finish();
    }

    @Override
    public void onLoadMore() {
        if (getArguments() != null) {
            UserCenterApi.getInstance().fetchFocusUserList(mCursor, mPageSize, userID, this);
        }
    }

}
