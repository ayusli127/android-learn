package top.a51code.viewpagertest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2017/5/9.
 */

public class TouchHighlightImageButton extends ImageButton {

    private Drawable mForegroundDrawer;
    private Rect mCacheBounds = new Rect();
    public TouchHighlightImageButton(Context context) {
        super(context);
        init();
    }

    public TouchHighlightImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchHighlightImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setBackgroundColor(0);
        setPadding(0,0,0,0);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.selectableItemBackground}
        );
        mForegroundDrawer = a.getDrawable(0);
        mForegroundDrawer.setCallback(this);
        a.recycle();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        //update state
        if(mForegroundDrawer.isStateful()){
            mForegroundDrawer.setState(getDrawableState());
        }
        //trigger drawer
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Then draw the highlight on top of it.If the button is neither focused nor pressed.
        //the drawable will be transparent,so just the image will be drawn;
        mForegroundDrawer.setBounds(mCacheBounds);
        mForegroundDrawer.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //cache the view bounds;
        mCacheBounds.set(0,0,w,h);
    }
}
