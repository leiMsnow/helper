package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.widget.view.LoadMoreListView;
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
        AdapterView.OnItemClickListener, LoadMoreListView.OnLoadMoreListener {
    private LoadMoreListView lvFocusList;
    private UserAdapter mAdapter;

    private int mCursor = 0;
    private int mPageSize = 10;
    private String userID;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_focus;
    }

    @Override
    protected void initView() {
        lvFocusList = (LoadMoreListView) mView.findViewById(R.id.lv_focus_list);
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
        }
    }

    @Override
    protected void initListener() {
        lvFocusList.setOnItemClickListener(this);
        mAdapter.setOnClickListener(this);
        lvFocusList.setOnLoadMoreListener(this);
    }

    /**
     * 获取我的关注列表Event
     */
    public void onEventMainThread(BaseEvent.MyFollowListEvent obj) {
        mPageSize++;
        mAdapter.replaceAll(obj.myFollowList);
        lvFocusList.setResultSize(obj.myFollowList.size());
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

    @Override
    public void onLoadMore() {
        if (getArguments() != null) {
            UserCenterApi.getInstance().fetchFocusUserList(mCursor, mPageSize, userID, this);
        }
    }
}
