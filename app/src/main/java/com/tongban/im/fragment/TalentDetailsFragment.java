package com.tongban.im.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tb.api.TalentApi;
import com.tb.api.model.TalentInfo;
import com.tongban.im.R;
import com.tongban.im.activity.SecondDetailsActivity;
import com.tongban.im.adapter.TalentCanDoAdapter;
import com.tongban.im.fragment.base.AppBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 达人详情
 */
public class TalentDetailsFragment extends AppBaseFragment implements
        SecondDetailsActivity.BottomOnClickListener {


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SecondDetailsActivity) {
            ((SecondDetailsActivity) activity).setBottomOnClickListener(this);
        }
    }

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

        TalentApi.getInstance().getTalentUserDetails(talentId,this);

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");

        talentCanDoAdapter = new TalentCanDoAdapter(mContext, R.layout.item_talent_can_list, list);
        for (int i = 0; i < talentCanDoAdapter.getCount(); i++) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_talent_can_list, null);
            lvCanList.addView(view);
        }
    }

    @Override
    public void bottomOnClick(String tagId) {

    }
}
