package com.tongban.im.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义蜂窝布局
 * version:1.0 陈恩裕  15/7/21
 */
public class HexagonLayout extends ViewGroup {

    /**
     * 子布局最大个数
     */
    private int MAX_CHILD_VIEWS = 4;
    /**
     * 子布局宽高与父布局宽高的比例系数:1/3
     */
    private float SCALE_ONE_THIRD = 1 / 3.0f;

    /**
     * 子布局宽高与父布局宽高的比例系数:1/4
     */
    private float SCALE_QUARTER = 1 / 4.0f;

    private float one_third_width, one_third_height, quarter_width, quarter_height;

    public HexagonLayout(Context context) {
        super(context);
    }

    public HexagonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexagonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count > MAX_CHILD_VIEWS)
            throw new IllegalArgumentException("Child views should not be more than 4.");

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        // 设置自定义ViewGroup的大小
        setMeasuredDimension(width, height);

        one_third_width = SCALE_ONE_THIRD * width;
        one_third_height = SCALE_ONE_THIRD * height;
        quarter_width = SCALE_QUARTER * width;
        quarter_height = SCALE_QUARTER * height;
        switch (count) {
            case 1:
                View child1 = getChildAt(0);
                LayoutParams lp1 = new LayoutParams(float2Int(one_third_width), float2Int(one_third_height));
                child1.setLayoutParams(lp1);
                break;
            case 2:
                for (int i = 0; i < count; i++) {
                    View child2 = getChildAt(i);
                    LayoutParams lp2 = new LayoutParams(float2Int(one_third_width), float2Int(one_third_height));
                    child2.setLayoutParams(lp2);
                }
                break;
            case 3:
                for (int i = 0; i < count; i++) {
                    View child3 = getChildAt(i);
                    LayoutParams lp3 = new LayoutParams(float2Int(one_third_width), float2Int(one_third_height));
                    child3.setLayoutParams(lp3);
                }
                break;
            case 4:
                for (int i = 0; i < count; i++) {
                    View child4 = getChildAt(i);
                    LayoutParams lp4 = new LayoutParams(float2Int(quarter_width), float2Int(quarter_height));
                    child4.setLayoutParams(lp4);
                }
                break;
            default:
                break;
        }
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);

    }

    private int measureWidth(int widthMeasureSpec) {
        int defaultWidth = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                defaultWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return defaultWidth;
    }

    private int measureHeight(int heightMeasureSpec) {
        int defaultHeight = 0;

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                defaultHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        return defaultHeight;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count > MAX_CHILD_VIEWS)
            throw new IllegalArgumentException("Child views should not be more than 4.");
        switch (count) {
            case 1:
                View child = getChildAt(0);
                if (child.getVisibility() != GONE) {
                    child.layout(float2Int(one_third_width), float2Int(one_third_height),
                            float2Int(2 * one_third_width), float2Int(2 * one_third_height));
                } else {
                    throw new IllegalArgumentException("Child view's visibility should not be GONE," +
                            "try to use INVISIBLE instead.");
                }
                break;
            case 2:
                View child0 = getChildAt(0);
                View child1 = getChildAt(1);
                if (child0.getVisibility() != GONE && child1.getVisibility() != GONE) {
                    // 上方的child
                    child0.layout(float2Int(one_third_width), float2Int(one_third_height / 3),
                            float2Int(2 * one_third_width), float2Int(one_third_height * 4 / 3));

                    // 下方的child
                    child1.layout(float2Int(one_third_width), float2Int(one_third_height * 5 / 3),
                            float2Int(2 * one_third_width), float2Int(one_third_height * 8 / 3));
                } else {
                    throw new IllegalArgumentException("Child view's visibility should not be GONE," +
                            "try to use INVISIBLE instead.");
                }
                break;
            case 3:
                View childA = getChildAt(0);
                View childB = getChildAt(1);
                View childC = getChildAt(2);
                if (childA.getVisibility() != GONE && childB.getVisibility() != GONE &&
                        childC.getVisibility() != GONE) {
                    // 上方的child
                    childA.layout(float2Int(one_third_width), float2Int(one_third_height / 2),
                            float2Int(2 * one_third_width), float2Int(one_third_height * 3 / 2));

                    // 左下方的child
                    childB.layout(float2Int(one_third_width / 2), float2Int(one_third_height * 3 / 2),
                            float2Int(one_third_width * 3 / 2), float2Int(one_third_height * 5 / 2));

                    // 右下方的child
                    childC.layout(float2Int(one_third_width * 3 / 2), float2Int(one_third_height * 3 / 2),
                            float2Int(one_third_width * 5 / 2), float2Int(one_third_height * 5 / 2));
                } else {
                    throw new IllegalArgumentException("Child view's visibility should not be GONE," +
                            "try to use INVISIBLE instead.");
                }
                break;
            case 4:
                View childM = getChildAt(0);
                View childT = getChildAt(1);
                View childLB = getChildAt(2);
                View childRB = getChildAt(3);
                if (childM.getVisibility() != GONE && childT.getVisibility() != GONE &&
                        childLB.getVisibility() != GONE && childRB.getVisibility() != GONE) {
                    // 中间的child
                    childM.layout(float2Int(quarter_width * 3 / 2), float2Int(quarter_height * 3 / 2),
                            float2Int(quarter_width * 5 / 2), float2Int(quarter_height * 5 / 2));

                    // 上方的child
                    childT.layout(float2Int(quarter_width * 3 / 2), float2Int(quarter_height / 4),
                            float2Int(quarter_width * 5 / 2), float2Int(quarter_height * 5 / 4));

                    // 左下方的child
                    childLB.layout(float2Int(quarter_width / 2), float2Int(quarter_height * 10 / 4),
                            float2Int(quarter_width * 6 / 4), float2Int(quarter_height * 14 / 4));

                    // 右下方的child
                    childRB.layout(float2Int(quarter_width * 10 / 4), float2Int(quarter_height * 10 / 4),
                            float2Int(quarter_width * 14 / 4), float2Int(quarter_height * 14 / 4));
                } else {
                    throw new IllegalArgumentException("Child view's visibility should not be GONE," +
                            "try to use INVISIBLE instead.");
                }
                break;
            default:
                break;
        }
    }

    private int float2Int(float num) {
        return (int) (num + 0.5);
    }
}
