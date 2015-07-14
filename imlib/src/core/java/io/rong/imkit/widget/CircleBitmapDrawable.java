//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imkit.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.InputStream;

public class CircleBitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 6;
    private CircleBitmapDrawable.BitmapState mBitmapState;
    private Bitmap mBitmap;
    private int mTargetDensity;
    private final Rect mDstRect;
    private boolean mApplyGravity;
    private boolean mRebuildShader;
    private boolean mMutated;
    private int mBitmapWidth;
    private int mBitmapHeight;

    /** @deprecated */
    @Deprecated
    public CircleBitmapDrawable() {
        this.mDstRect = new Rect();
        this.mBitmapState = new CircleBitmapDrawable.BitmapState((Bitmap)null);
    }

    public CircleBitmapDrawable(Resources res) {
        this.mDstRect = new Rect();
        this.mBitmapState = new CircleBitmapDrawable.BitmapState((Bitmap)null);
        this.mBitmapState.mTargetDensity = this.mTargetDensity;
    }

    /** @deprecated */
    @Deprecated
    public CircleBitmapDrawable(Bitmap bitmap) {
        this((CircleBitmapDrawable.BitmapState)(new CircleBitmapDrawable.BitmapState(bitmap)), (Resources)null);
    }

    public CircleBitmapDrawable(Resources res, Bitmap bitmap) {
        this(new CircleBitmapDrawable.BitmapState(bitmap), res);
        this.mBitmapState.mTargetDensity = this.mTargetDensity;
    }

    /** @deprecated */
    @Deprecated
    public CircleBitmapDrawable(String filepath) {
        this((CircleBitmapDrawable.BitmapState)(new CircleBitmapDrawable.BitmapState(BitmapFactory.decodeFile(filepath))), (Resources)null);
        if(this.mBitmap == null) {
            Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + filepath);
        }

    }

    public CircleBitmapDrawable(Resources res, String filepath) {
        this((CircleBitmapDrawable.BitmapState)(new CircleBitmapDrawable.BitmapState(BitmapFactory.decodeFile(filepath))), (Resources)null);
        this.mBitmapState.mTargetDensity = this.mTargetDensity;
        if(this.mBitmap == null) {
            Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + filepath);
        }

    }

    /** @deprecated */
    @Deprecated
    public CircleBitmapDrawable(InputStream is) {
        this((CircleBitmapDrawable.BitmapState)(new CircleBitmapDrawable.BitmapState(BitmapFactory.decodeStream(is))), (Resources)null);
        if(this.mBitmap == null) {
            Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + is);
        }

    }

    public CircleBitmapDrawable(Resources res, InputStream is) {
        this((CircleBitmapDrawable.BitmapState)(new CircleBitmapDrawable.BitmapState(BitmapFactory.decodeStream(is))), (Resources)null);
        this.mBitmapState.mTargetDensity = this.mTargetDensity;
        if(this.mBitmap == null) {
            Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + is);
        }

    }

    public final Paint getPaint() {
        return this.mBitmapState.mPaint;
    }

    public final Bitmap getBitmap() {
        return this.mBitmap;
    }

    private void computeBitmapSize() {
        this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
        this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
    }

    private void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if(bitmap != null) {
            this.computeBitmapSize();
        } else {
            this.mBitmapWidth = this.mBitmapHeight = -1;
        }

    }

    public void setTargetDensity(Canvas canvas) {
        this.setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        this.mTargetDensity = metrics.densityDpi;
        if(this.mBitmap != null) {
            this.computeBitmapSize();
        }

    }

    public void setTargetDensity(int density) {
        this.mTargetDensity = density == 0?160:density;
        if(this.mBitmap != null) {
            this.computeBitmapSize();
        }

    }

    public int getGravity() {
        return this.mBitmapState.mGravity;
    }

    public void setGravity(int gravity) {
        this.mBitmapState.mGravity = gravity;
        this.mApplyGravity = true;
    }

    public void setAntiAlias(boolean aa) {
        this.mBitmapState.mPaint.setAntiAlias(aa);
    }

    public void setFilterBitmap(boolean filter) {
        this.mBitmapState.mPaint.setFilterBitmap(filter);
    }

    public void setDither(boolean dither) {
        this.mBitmapState.mPaint.setDither(dither);
    }

    public TileMode getTileModeX() {
        return this.mBitmapState.mTileModeX;
    }

    public TileMode getTileModeY() {
        return this.mBitmapState.mTileModeY;
    }

    public void setTileModeX(TileMode mode) {
        this.setTileModeXY(mode, this.mBitmapState.mTileModeY);
    }

    public final void setTileModeY(TileMode mode) {
        this.setTileModeXY(this.mBitmapState.mTileModeX, mode);
    }

    public void setTileModeXY(TileMode xmode, TileMode ymode) {
        CircleBitmapDrawable.BitmapState state = this.mBitmapState;
        if(state.mPaint.getShader() == null || state.mTileModeX != xmode || state.mTileModeY != ymode) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
        }

    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mBitmapState.mChangingConfigurations;
    }

    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mApplyGravity = true;
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.mBitmap;
        if(bitmap != null) {
            int radius = this.mBitmapWidth > this.mBitmapHeight?this.mBitmapHeight / 2:this.mBitmapWidth / 2;
            canvas.save();
            CircleBitmapDrawable.BitmapState state = this.mBitmapState;
            TileMode tmx = state.mTileModeX;
            TileMode tmy = state.mTileModeY;
            BitmapShader s = new BitmapShader(bitmap, tmx == null?TileMode.CLAMP:tmx, tmy == null?TileMode.CLAMP:tmy);
            state.mPaint.setShader(s);
            state.mPaint.setAntiAlias(true);
            this.copyBounds(this.mDstRect);
            Shader shader = state.mPaint.getShader();
            if(this.mApplyGravity) {
                this.mDstRect.set(this.getBounds());
                this.mApplyGravity = false;
            }

            canvas.drawCircle((float)(this.mBitmapWidth / 2), (float)(this.mBitmapHeight / 2), (float)radius, state.mPaint);
        }

    }

    public void setColorFilter(ColorFilter cf) {
        this.mBitmapState.mPaint.setColorFilter(cf);
    }

    public Drawable mutate() {
        if(!this.mMutated && super.mutate() == this) {
            this.mBitmapState = new CircleBitmapDrawable.BitmapState(this.mBitmapState);
            this.mMutated = true;
        }

        return this;
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getOpacity() {
        if(this.mBitmapState.mGravity != 119) {
            return -3;
        } else {
            Bitmap bm = this.mBitmap;
            return bm != null && !bm.hasAlpha() && this.mBitmapState.mPaint.getAlpha() >= 255?-1:-3;
        }
    }

    public final ConstantState getConstantState() {
        this.mBitmapState.mChangingConfigurations = super.getChangingConfigurations();
        return this.mBitmapState;
    }

    private CircleBitmapDrawable(CircleBitmapDrawable.BitmapState state, Resources res) {
        this.mDstRect = new Rect();
        this.mBitmapState = state;
        if(res != null) {
            this.mTargetDensity = res.getDisplayMetrics().densityDpi;
        } else if(state != null) {
            this.mTargetDensity = state.mTargetDensity;
        } else {
            this.mTargetDensity = 160;
        }

        this.setBitmap(state.mBitmap);
    }

    public void setAlpha(int alpha) {
        this.mBitmapState.mPaint.setAlpha(alpha);
    }

    static final class BitmapState extends ConstantState {
        Bitmap mBitmap;
        int mChangingConfigurations;
        int mGravity;
        Paint mPaint;
        TileMode mTileModeX;
        TileMode mTileModeY;
        int mTargetDensity;

        BitmapState(Bitmap bitmap) {
            this.mGravity = 119;
            this.mPaint = new Paint(6);
            this.mTargetDensity = 160;
            this.mBitmap = bitmap;
            this.mPaint.setAntiAlias(true);
        }

        BitmapState(CircleBitmapDrawable.BitmapState bitmapState) {
            this(bitmapState.mBitmap);
            this.mChangingConfigurations = bitmapState.mChangingConfigurations;
            this.mGravity = bitmapState.mGravity;
            this.mTileModeX = bitmapState.mTileModeX;
            this.mTileModeY = bitmapState.mTileModeY;
            this.mTargetDensity = bitmapState.mTargetDensity;
            this.mPaint = new Paint(bitmapState.mPaint);
            this.mPaint.setAntiAlias(true);
        }

        public Drawable newDrawable() {
            return new CircleBitmapDrawable(this, (Resources)null);
        }

        public Drawable newDrawable(Resources res) {
            return new CircleBitmapDrawable(this, res);
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
    }
}
