package com.tongban.corelib.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.tongban.corelib.R;

public class ChangeColorView extends View {

    private int mDefaultColor = 0xff333333;//0xff55555;
    private int mChangeColor = 0xff45c01a;

    private Bitmap mIconBitmap;
    private String mText = "";
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

    private Canvas mcCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    private float mAlpha = 0.0f;

    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }
    public void setUnSelectColor(int mDefaultColor) {
        this.mDefaultColor = mDefaultColor;
        setIconAlpha(1.0f);
    }

    public void setSelectedColor(int mChangeColor) {
        this.mChangeColor = mChangeColor;
        setIconAlpha(1.0f);
    }

    public void setIconBitmap(int mIconBitmap) {
        this.mIconBitmap = BitmapFactory.decodeResource(getResources(),mIconBitmap);
        setIconAlpha(1.0f);
    }

    public void setmText(String mText) {
        this.mText = mText;
        setIconAlpha(1.0f);
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        setIconAlpha(1.0f);
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    public ChangeColorView(Context context) {
        this(context, null);
    }

    public ChangeColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获取自定义属性的值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ChangeColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ChangeColorView);

        int n = ta.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.ChangeColorView_changeIcon) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) ta
                        .getDrawable(attr);
                mIconBitmap = bitmapDrawable.getBitmap();
            } else if (attr == R.styleable.ChangeColorView_changeColor) {
                mChangeColor = ta.getColor(attr, 0xff45c01a);

            } else if (attr == R.styleable.ChangeColorView_changeText) {
                mText = ta.getString(attr);

            } else if (attr == R.styleable.ChangeColorView_changeTextSize) {
                mTextSize = (int) ta.getDimension(attr, TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                getResources().getDisplayMetrics()));
            } else if(attr == R.styleable.ChangeColorView_defaultColor){
                mDefaultColor = ta.getColor(attr,0xff333333);
            }
        }
        ta.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff55555);

        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingBottom()
                - getPaddingTop() - mTextBound.height());
        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth)
                / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int alpha = (int) Math.ceil(255 * mAlpha);
        if (mIconBitmap != null) {
            canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
            // 内存中绘制bitmap
            drawChangeBitmap(alpha);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        } // 绘制文本
        drawText(canvas, alpha);
        // 绘制变色文本
        drawChangeText(canvas, alpha);
    }

    private void drawChangeText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mChangeColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        if (mIconBitmap == null) {
            y = getMeasuredHeight() / 2 + mTextBound.height() / 2;
        }
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mDefaultColor);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        if (mIconBitmap == null) {
            y = getMeasuredHeight() / 2 + mTextBound.height() / 2;
        }
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /**
     * 内存中绘制可变色的icon
     */
    private void drawChangeBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        mcCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mChangeColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mcCanvas.drawRect(mIconRect, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mcCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }

    private static final String INSTANCE_STATUS = "INSTANCE_STATUS";
    private static final String CURRENT_ALPHA = "INSTANCE_ALPHA";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(CURRENT_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(CURRENT_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
