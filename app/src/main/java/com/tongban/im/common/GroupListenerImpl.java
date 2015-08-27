package com.tongban.im.common;

import android.content.Context;
import android.view.View;

import com.tongban.im.R;
import com.tongban.im.api.GroupApi;
import com.tongban.im.model.Group;

import io.rong.imkit.RongIM;

/**
 * 圈子监听实现类
 * Created by zhangleilei on 8/26/15.
 */
public class GroupListenerImpl implements View.OnClickListener {

    private Context mContext;

    public GroupListenerImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //加入圈子
            case R.id.btn_join:
                Group group = (Group) v.getTag();
                GroupApi.getInstance().joinGroup(group.getGroup_id(), group.getGroup_name(),
                        group.getUser_info().getUser_id(), null);
                break;
            //打开圈子聊天页/详情页
            case R.id.rl_group_item:
                group = (Group) v.getTag();
                if (group.isAllow_add()) {
                    RongIM.getInstance().startGroupChat(mContext, group.getGroup_id(),
                            group.getGroup_name());
                }else{
                    TransferCenter.getInstance().startGroupInfo(group.getGroup_id());
                }
                break;
        }
    }
}
