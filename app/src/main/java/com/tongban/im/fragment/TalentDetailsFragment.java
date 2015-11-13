package com.tongban.im.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tb.api.TalentApi;
import com.tb.api.model.Skills;
import com.tb.api.model.TalentInfo;
import com.tongban.im.R;
import com.tongban.im.adapter.TalentCanDoAdapter;
import com.tongban.im.fragment.base.AppBaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 达人详情
 */
public class TalentDetailsFragment extends AppBaseFragment {


    String talentId;
    @Bind(R.id.tv_talent_all_count)
    TextView tvTalentAllCount;
    @Bind(R.id.rb_score)
    RatingBar rbScore;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.ll_can_list)
    LinearLayout lvCanList;
    @Bind(R.id.tv_talent_desc)
    TextView tvTalentDesc;

    TalentCanDoAdapter talentCanDoAdapter;

    public static TalentDetailsFragment getInstance(String talentId) {

        TalentDetailsFragment serviceDetailsFragment = new TalentDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("talentId", talentId);
        serviceDetailsFragment.setArguments(bundle);
        return serviceDetailsFragment;

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_talent_details;
    }

    @Override
    protected void initData() {
        talentId = getArguments().getString("talentId", "");

        TalentApi.getInstance().getTalentUserDetails(talentId, this);

        talentCanDoAdapter = new TalentCanDoAdapter(mContext, R.layout.item_talent_can_list, null);

    }

    public void onEventMainThread(TalentInfo talentInfo) {

        rbScore.setRating(talentInfo.getScore());
        tvScore.setText(String.valueOf(talentInfo.getScore()));
        tvTalentDesc.setText(talentInfo.getProducer_desc().getDesc());

        for (int i = 0; i < talentInfo.getProducer_desc().getSkills().size(); i++) {
            Skills skills = talentInfo.getProducer_desc().getSkills().get(i);
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_talent_can_list, null);

            TextView tvServiceTitle = (TextView) view.findViewById(R.id.tv_service_title);
            TextView tvServiceDesc = (TextView) view.findViewById(R.id.tv_service_desc);
            TextView tvServiceTime = (TextView) view.findViewById(R.id.tv_service_time);

            tvServiceTitle.setText(skills.getTitle());
            tvServiceDesc.setText(skills.getContent());
            tvServiceTime.setText(skills.getDate());

            lvCanList.addView(view);
        }
    }

}
