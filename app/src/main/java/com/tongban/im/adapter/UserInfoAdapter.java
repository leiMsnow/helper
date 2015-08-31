package com.tongban.im.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongban.im.R;
import com.tongban.im.activity.user.PersonalInfoActivity;
import com.tongban.im.model.Child;

import java.util.List;

/**
 * 用户中心 - 两个孩子信息Adapter
 * Created by fushudi on 2015/8/31.
 */
public class UserInfoAdapter extends PagerAdapter {
    private Context mContext;
    private List<Child> mChildInfoList;
    private LayoutInflater mInflater;

    private View.OnClickListener onClickListener;

    private TextView tvUserName, tvDeclaration;
    private RelativeLayout rlUserInfo;
    private ImageView ivSex;

    private Child child;

    public UserInfoAdapter(Context context, List<Child> mChildInfoList) {
        this.mContext = context;
        this.mChildInfoList = mChildInfoList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getCount() {
        return mChildInfoList.size();
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
        View view = mInflater.inflate(R.layout.view_child_info, container, false);
        rlUserInfo = (RelativeLayout) view.findViewById(R.id.rl_user_info);
        tvDeclaration = (TextView) view.findViewById(R.id.tv_declaration);
        tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        ivSex = (ImageView) view.findViewById(R.id.iv_sex);

        rlUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, PersonalInfoActivity.class));
            }
        });

        child = mChildInfoList.get(position);
        if (child.getSex().equals("男")) {
            ivSex.setImageResource(R.mipmap.ic_boy);
        } else {
            ivSex.setImageResource(R.mipmap.ic_girl);
        }
        if (child != null) {
            tvUserName.setText(child.getNick_name() + " " +
                    child.getAge()
                    + " " + child.getConstellation());
        } else {
            tvUserName.setText("");
        }
        tvUserName.setText(child.getNick_name() + " " +
                child.getAge()
                + " " + child.getConstellation());

        tvDeclaration.setText(child.getSchool());

        container.addView(view, position);
        return view;
    }
}
