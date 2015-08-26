package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;
import com.tongban.im.model.Group;
import com.tongban.im.model.GroupType;

import java.util.List;

/**
 * 个人中心--我的圈子Adapter
 * Created by fushudi on 2015/8/18.
 */
public class GroupListAdapter extends QuickAdapter<Group> {

    private View.OnClickListener onClickListener;

    private boolean displayModel = true;

    /**
     * 展示模式
     *
     * @param displayModel true 查看模式-我的圈子页； false 操作模式-推荐圈子/查询圈子
     */
    public void setDisplayModel(boolean displayModel) {
        this.displayModel = displayModel;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public GroupListAdapter(Context context, int layoutResId, List data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Group item) {
        switch (item.getGroup_type()) {
            case GroupType.ACTIVITY:
                setGroupTags(helper, "活动圈", R.color.theme_yellow);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_yellow);
                helper.setText(R.id.tv_group_introduce, "");
                break;
            case GroupType.AGE:
                setGroupTags(helper, "同龄圈", R.color.theme_pink);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_pink);
                helper.setText(R.id.tv_group_introduce, "-" + item.getAge() + "岁," +
                        item.getConstellation());
                break;
            case GroupType.CITY:
                setGroupTags(helper, "同城圈", R.color.theme_deep_purple);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_purple);
                helper.setText(R.id.tv_group_introduce, "-" + item.getLastAddress() + "<" +
                        item.getDistance());
                break;
            case GroupType.CLASSMATE:
                setGroupTags(helper, "同学圈", R.color.theme_light_blue);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_blue);
                helper.setText(R.id.tv_group_introduce, "-" + item.getLastAddress());
                break;
            case GroupType.LIFE:
                setGroupTags(helper, "生活圈", R.color.theme_light_green);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_green);
                helper.setText(R.id.tv_group_introduce, "");
                break;
            case GroupType.TALENT:
                setGroupTags(helper, "达人圈", R.color.theme_yellow);
                helper.setBackgroundRes(R.id.tv_group_status, R.drawable.shape_corners_bg_yellow);
                break;
        }

        helper.setImageBitmap(R.id.iv_group_portrait, item.getGroup_avatar().getMin());
        helper.setText(R.id.tv_group_name, item.getGroup_name());

        //判断是否可以加入
        if (item.isAllow_add()) {
            helper.getView(R.id.btn_join).setEnabled(false);
            helper.setText(R.id.btn_join, mContext.getResources().getString(R.string.joined));
            helper.setOnClickListener(R.id.btn_join, null);
        } else {
            helper.getView(R.id.btn_join).setEnabled(true);
            helper.setText(R.id.btn_join, mContext.getResources().getString(R.string.join));
            helper.setTag(R.id.btn_join, item);
            helper.setOnClickListener(R.id.btn_join, onClickListener);
        }

        helper.setTag(R.id.rl_group_item, item);
        helper.setOnClickListener(R.id.rl_group_item, onClickListener);
    }

    private void setGroupTags(BaseAdapterHelper helper, String tags, int tagsColor) {
        helper.setText(R.id.tv_group_status, tags);
        helper.setTextColor(R.id.tv_group_status,
                mContext.getResources().getColor(tagsColor));
    }

    @Override
    protected void onFirstCreateView(BaseAdapterHelper helper) {
        if (displayModel) {
            helper.setVisible(R.id.btn_join, View.GONE);
        } else {
            helper.setVisible(R.id.btn_join, View.VISIBLE);
        }
    }
}
