package com.tongban.im.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tongban.corelib.utils.LogUtil;

/**
 * AdapterView工具类
 * Created by Cheney on 15/8/24.
 */
public class AdapterViewUtils {
    /**
     * 设置ListView的高度
     *
     * @param lv ListView
     */
    public static void setLvHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View itemView = adapter.getView(i, null, lv);
            itemView.measure(0, 0);
            totalHeight += itemView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = lv.getLayoutParams();
        layoutParams.height = totalHeight
                + (lv.getDividerHeight() * (adapter.getCount() - 1));
        lv.setLayoutParams(layoutParams);
    }

    /**
     * 设置GridView的高度
     *
     * @param gv      GridView
     * @param columns
     */
    public static void setGvHeight(GridView gv, int columns) {
        ListAdapter adapter = gv.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight;
        int items = adapter.getCount();

        View listItem = adapter.getView(0, null, gv);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        int rows = (int) Math.ceil(items / columns);
        totalHeight *= rows;

        ViewGroup.LayoutParams params = gv.getLayoutParams();
        params.height = totalHeight;
        gv.setLayoutParams(params);
    }
}
