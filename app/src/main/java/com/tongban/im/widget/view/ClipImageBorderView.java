package com.tongban.im.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {
    /**
     * ˮƽ������View�ı߾�
     */
    private int mHorizontalPadding;
    /**
     * ��ֱ������View�ı߾�
     */
    private int mVerticalPadding;
    /**
     * ���Ƶľ��εĿ��
     */
    private int mWidth;
    /**
     * �߿����ɫ��Ĭ��Ϊ��ɫ
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * �߿�Ŀ�� ��λdp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // �����������Ŀ��
        mWidth = getWidth() - 2 * mHorizontalPadding;
        // ���������Ļ��ֱ�߽� �ı߾�
        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Style.FILL);
        // �������1
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        // �����ұ�2
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
                getHeight(), mPaint);
        // �����ϱ�3
        canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
                mVerticalPadding, mPaint);
        // �����±�4
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
                getWidth() - mHorizontalPadding, getHeight(), mPaint);
        // ������߿�
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
                - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;

    }

}
