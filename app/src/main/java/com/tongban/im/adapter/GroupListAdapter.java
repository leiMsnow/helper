package com.tongban.im.adapter;

import android.content.Context;
import android.view.View;

import com.tb.api.model.group.Group;
import com.tb.api.model.group.GroupType;
import com.tongban.corelib.base.adapter.BaseAdapterHelper;
import com.tongban.corelib.base.adapter.QuickAdapter;
import com.tongban.im.R;

import java.util.List;

/**
 * 圈子Adapter
 * 1.推荐圈子
 * 2.搜索圈子
 * 3.我的圈子
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
            case GroupType.CITY:
                setGroupTags(helper
                        , item
                        , "同城圈"
                        , R.color.theme_deep_purple
                        , R.drawable.shape_corners_stroke_deep_purple
                        , R.drawable.shape_group_icon_pressed_deep_purple
                        , R.mipmap.ic_group_city);
                break;
            case GroupType.AGE:
                setGroupTags(helper
                        , item
                        , "同龄圈"
                        , R.color.theme_pink
                        , R.drawable.shape_corners_stroke_pink
                        , R.drawable.shape_group_icon_pressed_pink
                        , R.mipmap.ic_group_age);
                break;
            case GroupType.CLASSMATE:
                setGroupTags(helper
                        , item
                        , "同学圈"
                        , R.color.theme_light_blue
                        , R.drawable.shape_corners_stroke_blue
                        , R.drawable.shape_group_icon_pressed_light_blue
                        , R.mipmap.ic_group_classmate);
                break;
            case GroupType.LIFE:
                setGroupTags(helper
                        , item
                        , "生活圈"
                        , R.color.theme_light_green
                        , R.drawable.shape_corners_stroke_green
                        , R.drawable.shape_group_icon_pressed_light_green
                        , R.mipmap.ic_group_life);
                break;
            case GroupType.ACTIVITY:
                setGroupTags(helper
                        , item
                        , "活动圈"
                        , R.color.theme_yellow
                        , R.drawable.shape_corners_stroke_yellow
                        , R.drawable.shape_group_icon_pressed_yellow
                        , R.mipmap.ic_group_activity);
                break;
        }

        helper.setText(R.id.tv_group_name, item.getGroup_name());
        //判断是否已经加入
        if (!item.isAllow_add()) {
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

    /**
     * 设置属性
     *
     * @param helper
     * @param tags      标签文本
     * @param tagsColor 标签颜色
     * @param tagsBG    标签背景色
     * @param bgColor   图片背景色
     */
    private void setGroupTags(BaseAdapterHelper helper
            , Group item
            , String tags
            , int tagsColor
            , int tagsBG
            , int bgColor
            , int defaultPortrait) {
        helper.setText(R.id.tv_group_status, tags);
        helper.setTextColor(R.id.tv_group_status,
                mContext.getResources().getColor(tagsColor));
        helper.setBackgroundRes(R.id.tv_group_status, tagsBG);
        if (item.getGroupAvatar() != null) {
            helper.setBackgroundRes(R.id.fl_group_portrait, 0);
            helper.setImageBitmap(R.id.iv_group_portrait, item.getGroupAvatar().getMin());
        } else {
            helper.setBackgroundRes(R.id.fl_group_portrait, bgColor);
            helper.setImageBitmap(R.id.iv_group_portrait, defaultPortrait);
        }
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
