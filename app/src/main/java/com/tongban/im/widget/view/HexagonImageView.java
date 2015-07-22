package com.tongban.im.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 自定义蜂窝状(正六边形)的ImageView
 * version:1.0 chenenyu  15/7/18
 */
public class HexagonImageView extends ImageView {

    private Path hexagonPath;
    private Path hexagonBorderPath;
    private float radius1, radius2;
    private Bitmap mBitmap;
    private int w, h;
    private Paint paint;
    private BitmapShader shader;
    private Paint paintBorder;

    private int borderWidth = 0;

    public HexagonImageView(Context context) {
        super(context);
        setup();
    }

    public HexagonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public HexagonImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        paint = new Paint();
        paint.setAntiAlias(true);

        paintBorder = new Paint();
        //setBorderColor(Color.WHITE);
        paintBorder.setColor(Color.WHITE);
        paintBorder.setAntiAlias(true);
        // 禁用硬件加速,在api>=14的版本不支持加速
        this.setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
        // 设置阴影
//        paintBorder.setShadowLayer(1.0f, 0, 0, Color.BLACK);

        hexagonPath = new Path();
        hexagonBorderPath = new Path();

    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        calculatePath();
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);
        this.invalidate();
    }

    private void calculatePath() {

        double angle30 = 30 * Math.PI / 180; // 30°角

        float a2 = (float) (radius2 * Math.sin(angle30));
        float b2 = (float) (radius2 * Math.cos(angle30)); // 中心距离y轴的距离
        float c2 = (w - 2 * b2) / 2; // 不是斜边

        // 内圈(不带Border)
        hexagonPath.moveTo(w / 2, radius1 + radius2);
        hexagonPath.lineTo(w - c2, h - a2 - borderWidth);
        hexagonPath.lineTo(w - c2, a2 + borderWidth);
        hexagonPath.lineTo(w / 2, borderWidth);
        hexagonPath.lineTo(c2, a2 + borderWidth);
        hexagonPath.lineTo(c2, h - a2 - borderWidth);
        hexagonPath.close();

        float a1 = (float) (radius1 * Math.sin(angle30));
        float b1 = (float) (radius1 * Math.cos(angle30)); // 中心距离y轴的距离
        float c1 = (w - 2 * b1) / 2; // 不是斜边

        // 外圈(带Border)
        if (borderWidth > 0) {
            hexagonBorderPath.moveTo(w / 2, h);
            hexagonBorderPath.lineTo(w - c1, h - a1);
            hexagonBorderPath.lineTo(w - c1, a1);
            hexagonBorderPath.lineTo(w / 2, 0);
            hexagonBorderPath.lineTo(c1, a1);
            hexagonBorderPath.lineTo(c1, h - a1);
            hexagonBorderPath.close();
        }

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        w = width;
        h = height;
        radius1 = height / 2;
        radius2 = radius1 - borderWidth;

        calculatePath();

        setMeasuredDimension(width, height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getDrawable() == null)
            return;

        mBitmap = drawable2Bitmap(getDrawable());
        if (mBitmap != null) {
            canvas.drawColor(Color.WHITE);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(mBitmap, canvas.getWidth(),
                    canvas.getHeight(), true);
            shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            if (borderWidth > 0)
                // border
                canvas.drawPath(hexagonBorderPath, paintBorder);
            canvas.drawPath(hexagonPath, paint);
        }

    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        result = getPaddingLeft() + getPaddingRight() + getMeasuredWidth();
        // EXACTLY：表示设置了精确的值，一般当childView设置其宽、高为精确值、match_parent时;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize; //建议：result直接使用确定值
        } else if (specMode == MeasureSpec.AT_MOST) {
            // AT_MOST：表示子布局被限制在一个最大值内，一般当childView设置其宽、高为wrap_content时
            result = Math.min(result, specSize); //建议：result不能大于specSize
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        result = getPaddingTop() + getPaddingBottom() + getMeasuredWidth();
        // EXACTLY：表示设置了精确的值，一般当childView设置其宽、高为精确值、match_parent时;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize; //建议：result直接使用确定值
        } else if (specMode == MeasureSpec.AT_MOST) {
            // AT_MOST：表示子布局被限制在一个最大值内，一般当childView设置其宽、高为wrap_content时
            result = Math.min(result, specSize); //建议：result不能大于specSize
        }
        return result;
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
            Log.d("" + drawable.getIntrinsicWidth(), "" + drawable.getIntrinsicHeight());
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bitmap;
    }

}