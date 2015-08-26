package com.tongban.corelib.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 下方为弧形的ImageView
 * Created by Cheney on 15/8/18.
 */
public class BezierImageView extends ImageView {
    private Path path;
    private int w, h;

    public BezierImageView(Context context) {
        super(context);
        init();
    }

    public BezierImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        w = canvas.getWidth();
        h = canvas.getHeight();
        path.moveTo(0, 0.9f * h);
        path.quadTo(w / 2.0f, 1.1f * h, w, 0.9f * h);
        path.lineTo(w, 0);
        path.lineTo(0, 0);
        path.close();
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
