package com.tongban.im.utils;

import android.content.Context;
import android.graphics.Point;

import com.tongban.corelib.utils.DensityUtils;
import com.tongban.corelib.widget.header.RentalsSunHeaderView;
import com.tongban.im.R;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * ptr的header工具类
 * Created by zhangleilei on 9/27/15.
 */
public class PTRHeaderUtils {

    /**
     * 阳光小镇
     *
     * @param mContext
     * @param ptrFrameLayout
     * @return
     */
    public static RentalsSunHeaderView getSunTownView(Context mContext, PtrFrameLayout ptrFrameLayout) {

        RentalsSunHeaderView header = new RentalsSunHeaderView(mContext);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DensityUtils.dp2px(mContext, 16),
                0, DensityUtils.dp2px(mContext, 16));
        header.setUp(ptrFrameLayout);

        return header;
    }

    /**
     * 坐标绘制的“加载中”
     *
     * @param mContext
     * @param ptrFrameLayout
     * @return
     */
    public static StoreHouseHeader getPointListView(Context mContext, PtrFrameLayout ptrFrameLayout) {

        StoreHouseHeader header = new StoreHouseHeader(mContext);
        header.setTextColor(R.color.main_black);
        header.setPadding(DensityUtils.dp2px(mContext, 16), DensityUtils.dp2px(mContext, 16),
                DensityUtils.dp2px(mContext, 16), 0);
        header.initWithPointList(getPointList());
        return header;
    }


    private static ArrayList<float[]> getPointList() {
        // this point is taken from https://github.com/cloay/CRefreshLayout
        List<Point> startPoints = new ArrayList<Point>();
        startPoints.add(new Point(240, 80));
        startPoints.add(new Point(270, 80));
        startPoints.add(new Point(265, 103));
        startPoints.add(new Point(255, 65));
        startPoints.add(new Point(275, 80));
        startPoints.add(new Point(275, 80));
        startPoints.add(new Point(302, 80));
        startPoints.add(new Point(275, 107));

        startPoints.add(new Point(320, 70));
        startPoints.add(new Point(313, 80));
        startPoints.add(new Point(330, 63));
        startPoints.add(new Point(315, 87));
        startPoints.add(new Point(330, 80));
        startPoints.add(new Point(315, 100));
        startPoints.add(new Point(330, 90));
        startPoints.add(new Point(315, 110));
        startPoints.add(new Point(345, 65));
        startPoints.add(new Point(357, 67));
        startPoints.add(new Point(363, 103));

        startPoints.add(new Point(375, 80));
        startPoints.add(new Point(375, 80));
        startPoints.add(new Point(425, 80));
        startPoints.add(new Point(380, 95));
        startPoints.add(new Point(400, 63));

        List<Point> endPoints = new ArrayList<Point>();
        endPoints.add(new Point(270, 80));
        endPoints.add(new Point(270, 110));
        endPoints.add(new Point(270, 110));
        endPoints.add(new Point(250, 110));
        endPoints.add(new Point(275, 107));
        endPoints.add(new Point(302, 80));
        endPoints.add(new Point(302, 107));
        endPoints.add(new Point(302, 107));

        endPoints.add(new Point(340, 70));
        endPoints.add(new Point(360, 80));
        endPoints.add(new Point(330, 80));
        endPoints.add(new Point(340, 87));
        endPoints.add(new Point(315, 100));
        endPoints.add(new Point(345, 98));
        endPoints.add(new Point(330, 120));
        endPoints.add(new Point(345, 108));
        endPoints.add(new Point(360, 120));
        endPoints.add(new Point(363, 75));
        endPoints.add(new Point(345, 117));

        endPoints.add(new Point(380, 95));
        endPoints.add(new Point(425, 80));
        endPoints.add(new Point(420, 95));
        endPoints.add(new Point(420, 95));
        endPoints.add(new Point(400, 120));
        ArrayList<float[]> list = new ArrayList<float[]>();

        int offsetX = Integer.MAX_VALUE;
        int offsetY = Integer.MAX_VALUE;

        for (int i = 0; i < startPoints.size(); i++) {
            offsetX = Math.min(startPoints.get(i).x, offsetX);
            offsetY = Math.min(startPoints.get(i).y, offsetY);
        }
        for (int i = 0; i < endPoints.size(); i++) {
            float[] point = new float[4];
            point[0] = startPoints.get(i).x - offsetX;
            point[1] = startPoints.get(i).y - offsetY;
            point[2] = endPoints.get(i).x - offsetX;
            point[3] = endPoints.get(i).y - offsetY;
            list.add(point);
        }
        return list;
    }


}
