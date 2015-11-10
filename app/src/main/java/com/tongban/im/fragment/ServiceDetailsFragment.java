package com.tongban.im.fragment;

import android.app.Activity;
import android.os.Bundle;

import com.tongban.im.R;
import com.tongban.im.activity.SecondDetailsActivity;
import com.tongban.im.fragment.base.AppBaseFragment;

import io.rong.imkit.RongIM;

/**
 * 服务详情
 */
public class ServiceDetailsFragment extends AppBaseFragment implements
        SecondDetailsActivity.BottomOnClickListener {


    private String serviceId;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SecondDetailsActivity) {
            ((SecondDetailsActivity) activity).setBottomOnClickListener(this);
        }
    }

    public static ServiceDetailsFragment getInstance(String serviceId) {

        ServiceDetailsFragment serviceDetailsFragment = new ServiceDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("serviceId", serviceId);
        serviceDetailsFragment.setArguments(bundle);
        return serviceDetailsFragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service_details;
    }

    @Override
    protected void initData() {
        serviceId = getArguments().getString("serviceId", "");
    }

    @Override
    public void bottomOnClick(String tagId) {
        RongIM.getInstance().startGroupChat(mContext, serviceId,
                "达人");
    }
}
