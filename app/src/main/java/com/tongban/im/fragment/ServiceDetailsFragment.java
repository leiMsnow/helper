package com.tongban.im.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tb.api.TalentApi;
import com.tb.api.model.TalentInfo;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.activity.SecondDetailsActivity;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * 服务详情
 */
public class ServiceDetailsFragment extends AppBaseFragment implements
        SecondDetailsActivity.BottomOnClickListener {


    @Bind(R.id.tv_service_title)
    TextView tvServiceTitle;
    @Bind(R.id.fl_tag)
    FlowLayout flTag;
    @Bind(R.id.rb_score)
    RatingBar rbScore;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.tv_service_time)
    TextView tvServiceTime;
    @Bind(R.id.tv_service_desc)
    TextView tvServiceDesc;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;


    String serviceId;

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
        TalentApi.getInstance().getTalentInfo(serviceId, this);
    }

    @Override
    public void bottomOnClick(String tagId) {
        RongIM.getInstance().startPrivateChat(mContext, tagId,
                tvUserName.getText().toString());
    }

    public void onEventMainThread(TalentInfo obj) {

        if (obj.getUser() != null) {
            tvUserName.setText(obj.getUser().getNick_name());
        }
        String[] themeTags = obj.getTags().split(",");
        if (themeTags.length > 0) {
            flTag.removeAllViews();
            for (String tag : themeTags) {
                TextView tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.include_service_tips_text
                                , flTag, false);
                tv.setText(tag);
                flTag.addView(tv);
            }
        } else {
            flTag.setVisibility(View.GONE);
        }
        tvServiceTitle.setText(obj.getProducer_name());
        rbScore.setRating(obj.getScore());
        tvScore.setText(String.valueOf(obj.getScore()));
        tvServiceDesc.setText(obj.getProducer_desc().getDesc());
    }

}
