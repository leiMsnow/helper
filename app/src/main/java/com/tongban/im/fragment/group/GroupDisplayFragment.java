package com.tongban.im.fragment.group;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tongban.corelib.base.fragment.BaseApiFragment;
import com.tongban.im.R;
import com.tongban.im.common.Consts;
import com.tongban.im.common.GroupListenerImpl;
import com.tongban.im.common.TransferCenter;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import io.rong.imkit.RongIM;

/**
 * 圈子展示界面
 */
public class GroupDisplayFragment extends BaseApiFragment {


    private TextView tvGroupStatus;
    private TextView tvGroupName;
    private TextView tvGroupIntroduce;
    private Button btnJoin;
    private ImageView ivGroupPortrait;
    private View vGroupItem;

    private Group groupInfo;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_group_display;
    }

    @Override
    protected void initView() {
        vGroupItem = mView.findViewById(R.id.rl_group_item);
        tvGroupStatus = (TextView) mView.findViewById(R.id.tv_group_status);
        tvGroupName = (TextView) mView.findViewById(R.id.tv_group_name);
        tvGroupIntroduce = (TextView) mView.findViewById(R.id.tv_group_introduce);
        btnJoin = (Button) mView.findViewById(R.id.btn_join);
        ivGroupPortrait = (ImageView) mView.findViewById(R.id.iv_group_portrait);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle.getSerializable(Consts.KEY_GROUP_INFO) != null) {
            groupInfo = (Group) bundle.getSerializable(Consts.KEY_GROUP_INFO);
            vGroupItem.setTag(groupInfo);
            btnJoin.setTag(groupInfo);
            setGroupType();
            if (groupInfo.getGroup_avatar() != null) {
                Glide.with(mContext).load(groupInfo.getGroup_avatar().getMax()).
                        placeholder(R.drawable.rc_default_group_portrait).into(ivGroupPortrait);
            } else {
                ivGroupPortrait.setImageResource(R.drawable.rc_default_group_portrait);
            }
            tvGroupName.setText(groupInfo.getGroup_name());
        }
    }

    @Override
    protected void initListener() {
        vGroupItem.setOnClickListener(new GroupListenerImpl(mContext));
        btnJoin.setOnClickListener(new GroupListenerImpl(mContext));
    }

    private void setGroupType() {
        switch (groupInfo.getGroup_type()) {
            case GroupType.ACTIVITY:
                setGroupTags("活动圈", R.color.theme_yellow);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_yellow);
                tvGroupIntroduce.setText("");
                break;
            case GroupType.AGE:
                setGroupTags("同龄圈", R.color.theme_pink);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_pink);
                tvGroupIntroduce.setText("-" + groupInfo.getAge() + "岁," +
                        groupInfo.getConstellation());
                break;
            case GroupType.CITY:
                setGroupTags("同城圈", R.color.theme_deep_purple);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_purple);
                tvGroupIntroduce.setText("-" + groupInfo.getLastAddress() + "<" +
                        groupInfo.getDistance());
                break;
            case GroupType.CLASSMATE:
                setGroupTags("同学圈", R.color.theme_light_blue);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_blue);
                tvGroupIntroduce.setText("-" + groupInfo.getLastAddress());
                break;
            case GroupType.LIFE:
                setGroupTags("生活圈", R.color.theme_light_green);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_green);
                tvGroupIntroduce.setText("");
                break;
            case GroupType.TALENT:
                setGroupTags("达人圈", R.color.theme_yellow);
                tvGroupStatus.setBackgroundResource(R.drawable.shape_corners_bg_yellow);
                break;
        }
    }

    private void setGroupTags(String tags, int tagsColor) {
        tvGroupStatus.setText(tags);
        tvGroupStatus.setTextColor(
                mContext.getResources().getColor(tagsColor));
    }

}
