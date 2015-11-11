package com.tongban.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tb.api.model.TalentInfo;
import com.tb.api.model.group.Group;
import com.tb.api.model.group.GroupType;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;
import com.tongban.im.common.Consts;

import java.util.List;

/**
 * 服务大厅Adapter
 */
public class ServiceHallListAdapter extends QuickAdapter<TalentInfo> {

    private View.OnClickListener onClickListener;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ServiceHallListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, TalentInfo item) {
        helper.setText(R.id.tv_service_title, item.getProducer_name());
        helper.setImageBitmap(R.id.iv_service_bg, item.getBg_url());

        if (item.getUser() != null && item.getUser().getPortraitUrl() != null) {
            helper.setImageBitmap(R.id.iv_user_portrait, item.getUser().getPortraitUrl().getMin());
        } else {
            helper.setImageBitmap(R.id.iv_user_portrait, Consts.getUserDefaultPortrait());
        }

        helper.setText(R.id.tv_service_desc, item.getProducer_desc());

        String[] themeTags = item.getTags().split(",");
        if (themeTags.length > 0) {
            ((FlowLayout) helper.getView(R.id.fl_tag)).removeAllViews();
            for (String tag : themeTags) {
                TextView tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.include_service_tips_text
                                , ((FlowLayout) helper.getView(R.id.fl_tag)), false);
                tv.setText(tag);
                ((FlowLayout) helper.getView(R.id.fl_tag)).addView(tv);
            }
        } else {
            (helper.getView(R.id.fl_tag)).setVisibility(View.GONE);
        }

        helper.setRating(R.id.rb_score, item.getScore());
        helper.setText(R.id.tv_score, String.valueOf(item.getScore()));
        helper.setTag(R.id.rl_service_parent, item.getProducer_id());
        helper.setOnClickListener(R.id.rl_service_parent, onClickListener);
    }


    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
                helper.getView(R.id.iv_service_bg).getLayoutParams();
        lp.height = ScreenUtils.getScreenHeightNoStatus(mContext) / 3;
        helper.getView(R.id.iv_service_bg).setLayoutParams(lp);
    }
}
