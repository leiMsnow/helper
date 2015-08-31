package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.widget.view.LoadMoreListView;
import com.tongban.corelib.widget.view.listener.OnLoadMoreListener;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;

/**
 * 粉丝界面
 *
 * @author fushudi
 */
public class FansFragment extends BaseApiFragment implements AdapterView.OnItemClickListener,
        View.OnClickListener, OnLoadMoreListener {
    private LoadMoreListView lvFansList;
    private UserAdapter mAdapter;
    private int mCursor = 0;
    private int mPageSize = 10;
    private String userID;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_fans;
    }

    @Override
    protected void initView() {
        lvFansList = (LoadMoreListView) mView.findViewById(R.id.lv_fans_list);
    }

    @Override
    protected void initData() {
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        lvFansList.setAdapter(mAdapter);
        lvFansList.setPageSize(mPageSize);
        if (getArguments() != null) {
            userID = getArguments().getString(Consts.USER_ID);
            UserCenterApi.getInstance().fetchFansUserList(mCursor, mPageSize, userID, this);
        }
    }

    @Override
    protected void initListener() {
        lvFansList.setOnItemClickListener(this);
        mAdapter.setOnClickListener(this);
        lvFansList.setOnLoadMoreListener(this);
    }

    /**
     * 粉丝列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.MyFansListEvent obj) {
        mCursor++;
        mAdapter.replaceAll(obj.myFansList);
        lvFansList.setResultSize(obj.myFansList.size());
    }

    /**
     * 关注Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.FocusEvent obj) {
        UserCenterApi.getInstance().fetchFansUserList(mCursor, mPageSize, userID, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startUserCenter(mAdapter.getItem(position).getUser_id());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_follow:
                String focusId = v.getTag().toString();
                UserCenterApi.getInstance().focusUser(true, focusId, this);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        UserCenterApi.getInstance().fetchFansUserList(mCursor, mPageSize, userID, this);
    }
}
