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
     * 子布局宽高与父布局宽高的比例系数
     */
    private float SCALE_DEFAULT = 1 / 3.0f;

    /**
     * 子布局宽高与父布局宽高的比例系数
     */
    private float SCALE_FOUR = 2 / 7.0f;

    private float default_width, default_height, four_width, four_height;


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

        default_width = SCALE_DEFAULT * width;
        default_height = SCALE_DEFAULT * height;
        four_width = SCALE_FOUR * width;
        four_height = SCALE_FOUR * height;
        switch (count) {
            case 1:
                View child1 = getChildAt(0);
                LayoutParams lp1 = new LayoutParams(float2Int(default_width), float2Int(default_height));
                child1.setLayoutParams(lp1);
                break;
            case 2:
                for (int i = 0; i < count; i++) {
                    View child2 = getChildAt(i);
                    LayoutParams lp2 = new LayoutParams(float2Int(default_width), float2Int(default_height));
                    child2.setLayoutParams(lp2);
                }
                break;
            case 3:
                for (int i = 0; i < count; i++) {
                    View child3 = getChildAt(i);
                    LayoutParams lp3 = new LayoutParams(float2Int(default_width), float2Int(default_height));
                    child3.setLayoutParams(lp3);
                }
                break;
            case 4:
                for (int i = 0; i < count; i++) {
                    View child4 = getChildAt(i);
                    LayoutParams lp4 = new LayoutParams(float2Int(four_width), float2Int(four_height));
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
                    child.layout(float2Int(default_width), float2Int(default_height),
                            float2Int(2 * default_width), float2Int(2 * default_height));
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
                    child0.layout(float2Int(default_width), float2Int(default_height / 3),
                            float2Int(2 * default_width), float2Int(default_height * 4 / 3));

                    // 下方的child
                    child1.layout(float2Int(default_width), float2Int(default_height * 5 / 3),
                            float2Int(2 * default_width), float2Int(default_height * 8 / 3));
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
                    childA.layout(float2Int(default_width), float2Int(default_height / 2),
                            float2Int(2 * default_width), float2Int(default_height * 3 / 2));

                    // 左下方的child
                    childB.layout(float2Int(default_width * 2 / 5), float2Int(default_height * 3 / 2),
                            float2Int(default_width * 7 / 5), float2Int(default_height * 5 / 2));

                    // 右下方的child
                    childC.layout(float2Int(default_width * 8 / 5), float2Int(default_height * 3 / 2),
                            float2Int(default_width * 13 / 5), float2Int(default_height * 5 / 2));
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
                    childM.layout(float2Int(four_width * 5 / 4), float2Int(four_height * 17 / 12),
                            float2Int(four_width * 9 / 4), float2Int(four_height * 29 / 12));

                    // 上方的child
                    childT.layout(float2Int(four_width * 5 / 4), float2Int(four_height * 19 / 60),
                            float2Int(four_width * 9 / 4), float2Int(four_height * 79 / 60));

                    // 左下方的child
                    childLB.layout(float2Int(four_width / 5), float2Int(four_height * 13 / 6),
                            float2Int(four_width * 6 / 5), float2Int(four_height * 19 / 6));

                    // 右下方的child
                    childRB.layout(float2Int(four_width * 23 / 10), float2Int(four_height * 13 / 6),
                            float2Int(four_width * 33 / 10), float2Int(four_height * 19 / 6));
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
