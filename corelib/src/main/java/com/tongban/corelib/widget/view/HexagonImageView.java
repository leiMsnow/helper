package com.tongban.corelib.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义蜂窝状(正六边形)的ImageView
 * version:1.1 chenenyu  15/8/4
 */
public class HexagonImageView extends ImageView {

    private Path path;
    private float radius;
    private int w, h;
    private Paint paint;

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
        path = new Path();
        // 硬件加速的canvas不支持canvas.clipPath和canvas.clipRegion,所以禁用硬件加速,且api<11的版本不支持加速
        this.setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    public void onDraw(Canvas canvas) {
        w = canvas.getWidth();
        h = canvas.getHeight();
        radius = h / 2;
        double angle30 = 30 * Math.PI / 180; // 30°角
        float a = (float) (radius * Math.sin(angle30));
        float b = (float) (radius * Math.cos(angle30));
        float xMargin = (w - 2 * b) / 2;

        path.moveTo(w / 2, h);
        path.lineTo(w - xMargin, h - a);
        path.lineTo(w - xMargin, a);
        path.lineTo(w / 2, 0);
        path.lineTo(xMargin, a);
        path.lineTo(xMargin, h - a);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        setImageDrawable(null);
        super.onDetachedFromWindow();
    }
}