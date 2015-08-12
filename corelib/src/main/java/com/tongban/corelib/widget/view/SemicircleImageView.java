package com.tongban.corelib.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 半圆imaageview
 *
 * @author zhangshao
 */
public class SemicircleImageView extends ImageView {

    int mWidth, mHeight;
    Path mPath;
    Paint mPaint;
    RectF mRectF;

    public SemicircleImageView(Context context) {
        super(context);
        init();
    }

    public SemicircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemicircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
//        this.measure(0, 0);  

        mWidth = getWidth();
        mHeight = getHeight();
        Bitmap outBit = null;
        if (drawable.getClass() == NinePatchDrawable.class) {
            outBit = drawOneSideOval((NinePatchDrawable) drawable);
        } else {
            Bitmap b = ((BitmapDrawable) drawable).getBitmap();
            Bitmap bitmap = b.copy(Config.RGB_565, true);
            outBit = drawOneSideOval(bitmap);
        }

        if (outBit == null) {
            return;
        }

        canvas.save();
        mPath.reset();
        mRectF.set(-1.0f * mWidth, -mHeight, 2.0f * mWidth, mHeight);
        mPath.addOval(mRectF, Path.Direction.CCW);
        canvas.clipPath(mPath, Op.REPLACE);
        canvas.drawBitmap(outBit, 0, 0, mPaint);
        canvas.restore();
    }

    private Bitmap drawOneSideOval(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();


        Bitmap output = Bitmap.createBitmap(mWidth,
                mHeight, Config.RGB_565);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(255, 255, 255, 255);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Matrix matrix = new Matrix();
        float scaleY = mHeight * 1.0f / bh;
        float scaleX = mWidth * 1.0f / bw;
        //float scale=scaleX>scaleY?scaleY:scaleX;
        matrix.setScale(scaleX, scaleX);
        int w = (int) (scaleX * bw);
        Bitmap drawBitmap = Bitmap.createBitmap(bitmap, 0, 0, bw, bh, matrix, true);
        if (w > mWidth) {
            canvas.drawBitmap(drawBitmap, 0, 0, paint);
        } else {
            canvas.drawBitmap(drawBitmap, mWidth / 2 - w / 2, 0, paint);
        }

        bitmap.recycle();
        return output;
    }

    private Bitmap drawOneSideOval(NinePatchDrawable nineDrawable) {
        if (nineDrawable == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
        if (output != null) {
            Canvas canvas = new Canvas(output);
            nineDrawable.setBounds(0, 0, mWidth, mHeight);
            nineDrawable.draw(canvas);
        }
        return output;
    }
}
