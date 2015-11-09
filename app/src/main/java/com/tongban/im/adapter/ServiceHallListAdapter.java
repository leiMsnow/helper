package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tb.api.model.group.Group;
import com.tb.api.model.group.GroupType;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.utils.ScreenUtils;
import com.tongban.im.R;

import java.util.List;

/**
 * 服务大厅Adapter
 */
public class ServiceHallListAdapter extends QuickAdapter<Group> {

    private View.OnClickListener onClickListener;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ServiceHallListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Group item) {
        helper.setText(R.id.tv_service_title, item.getGroup_name());
        helper.setImageBitmap(R.id.iv_service_bg, "http://pic36.nipic.com/20131126/2457387_133328197000_2.jpg");
        helper.setImageBitmap(R.id.iv_user_portrait, "http://cdn.duitang.com/uploads/item/201409/12/20140912004436_2aK2M.jpeg");
        helper.setText(R.id.tv_service_user, "美甲师");
        helper.setText(R.id.tv_service_desc, "这里是服务描述，一般不会超过两行的。我说的是真的，不信你就一直写吧" +
                ",反正我只显示两行。");

        helper.setOnClickListener(R.id.rl_service_parent, onClickListener);
    }


    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
                helper.getView(R.id.iv_service_bg).getLayoutParams();
        lp.height = ScreenUtils.getScreenHeightNoStatus(mContext) / 3;
        helper.getView(R.id.iv_service_bg).setLayoutParams(lp);
        if (helper.getPosition() == 0) {
            FrameLayout.LayoutParams plp = (FrameLayout.LayoutParams)
                    helper.getView(R.id.rl_service_parent).getLayoutParams();
            plp.topMargin = DensityUtils.dp2px(mContext, 8);
            helper.getView(R.id.rl_service_parent).setLayoutParams(plp);
        }


    }
}
