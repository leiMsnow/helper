package com.tongban.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongban.corelib.utils.SPUtils;
import com.tongban.im.R;
import com.tongban.im.activity.user.PersonalInfoActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.model.Child;

import java.util.List;

/**
 * 用户中心 - 两个孩子信息Adapter
 * Created by fushudi on 2015/8/31.
 */
public class UserInfoAdapter extends PagerAdapter {
    private Context mContext;
    private List<Child> mChildInfoList;

    private TextView tvChildInfo;

    private String userId;

    public UserInfoAdapter(Context context, List<Child> mChildInfoList, String userId) {
        this.mContext = context;
        this.mChildInfoList = mChildInfoList;
        this.userId = userId;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_child_info, container, false);
        tvChildInfo = (TextView) view.findViewById(R.id.tv_child_info);
        //跳转到个人资料界面
        if (SPUtils.get(mContext, Consts.USER_ID, "").equals(userId)) {
            tvChildInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, PersonalInfoActivity.class));
                }
            });
        } else {
            tvChildInfo.setVisibility(View.GONE);
        }
        Child child = mChildInfoList.get(position);
        if (child != null) {
            tvChildInfo.setText(child.getAge() + "岁 | " + child.getConstellation());
        } else {
            tvChildInfo.setText("");
        }
        container.addView(view, position);
        return view;
    }
}
