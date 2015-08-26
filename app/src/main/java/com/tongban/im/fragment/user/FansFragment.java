package com.tongban.im.fragment.user;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.adapter.UserAdapter;
import com.tongban.im.api.UserCenterApi;
import com.tongban.im.common.Consts;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.User;

import java.util.List;

/**
 * 粉丝界面
 *
 * @author fushudi
 */
public class FansFragment extends BaseApiFragment implements AdapterView.OnItemClickListener {
    private ListView lvFansList;
    private UserAdapter mAdapter;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_fans;
    }

    @Override
    protected void initView() {
        lvFansList = (ListView) mView.findViewById(R.id.lv_fans_list);
    }

    @Override
    protected void initData() {
        mAdapter = new UserAdapter(mContext, R.layout.item_my_info_list, null);
        lvFansList.setAdapter(mAdapter);
        if (getArguments() != null) {
            String userID = getArguments().getString(Consts.USER_ID);
            UserCenterApi.getInstance().fetchFansUserList(0, 10, userID, this);
        }
    }

    @Override
    protected void initListener() {
        lvFansList.setOnItemClickListener(this);
    }

    /**
     * 粉丝列表Event
     *
     * @param obj
     */
    public void onEventMainThread(BaseEvent.MyFansListEvent obj) {
        mAdapter.replaceAll(obj.getMyFansList());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransferCenter.getInstance().startUserCenter(mAdapter.getItem(position).getUser_id());
    }
}
