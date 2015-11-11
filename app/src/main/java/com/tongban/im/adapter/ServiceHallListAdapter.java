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
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.corelib.widget.view.FlowLayout;
import com.tongban.im.R;

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
        helper.setImageBitmap(R.id.iv_service_bg, "http://imglf2.nosdn.127.net/img/cXdOelNkY3ZjdzY0SFBydXRlMFJWRjZMVGJkSjNRS3A2aGVheUtBVmxibER4a01hcy9pRjZnPT0.jpg?imageView&thumbnail=1680x0&quality=96&stripmeta=0&type=jpg");
        helper.setImageBitmap(R.id.iv_user_portrait, "http://imglf0.nosdn.127.net/img/SzZqcDg4Rk01VGhoOXJGTHJkUXI0NENNTHhUMnF0MzRWUmlSallqUUxNTkJUcVRHclNkNGNnPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg");
        helper.setText(R.id.tv_service_desc, "这里是服务描述，一般不会超过两行的。我说的是真的，不信你就一直写吧" +
                ",反正我只显示两行。");

        String[] themeTags = item.getTags().split(",");
        if (themeTags.length > 0) {
            ((FlowLayout)helper.getView(R.id.fl_tag)).removeAllViews();
            for (String tag : themeTags) {
                TextView tv = (TextView) LayoutInflater.from(mContext)
                        .inflate(R.layout.include_service_tips_text
                                , ((FlowLayout)helper.getView(R.id.fl_tag)), false);
                tv.setText(tag);
                ((FlowLayout)helper.getView(R.id.fl_tag)).addView(tv);
            }
        } else {
            (helper.getView(R.id.fl_tag)).setVisibility(View.GONE);
        }

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
