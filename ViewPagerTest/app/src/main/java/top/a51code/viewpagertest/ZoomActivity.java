package top.a51code.viewpagertest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class ZoomActivity extends AppCompatActivity {
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        //hook up clicks on the thumbnail view;
        final View thumb1View = findViewById(R.id.thumb_button_1);
        final View thumb2View = findViewById(R.id.thumb_button_2);
        thumb1View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb1View,R.drawable.image1);
            }
        });
        thumb2View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(thumb2View,R.drawable.image2);
            }
        });
        //receive and cache the system's default "short" animation time
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }
    private void zoomImageFromThumb(final View thumbView,int imageResId){
        if(mCurrentAnimator != null){
            mCurrentAnimator.cancel();
        }
        final ImageView expandedImageView = (ImageView)findViewById(R.id.expand_image);
        expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset =  new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds,globalOffset);
        startBounds.offset(-globalOffset.x,-globalOffset.y);
        finalBounds.offset(-globalOffset.x,-globalOffset.y);

        float startScale;
        if((float)finalBounds.width() / finalBounds.height() > (float) startBounds.width()/ startBounds.height()){
            startScale = (float) startBounds.height() /  finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width())/2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }else{
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight  -  startBounds.height())/2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }
        //hide the thumbnail and show the zoomed-in view;
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set =  new AnimatorSet();
        set.
                play(ObjectAnimator.ofFloat(expandedImageView,View.X,startBounds.left,finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView,View.Y,startBounds.top,finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_X,startScale,1f))
                .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_Y,startScale,1f));
        set.setDuration(mShortAnimationDuration);
        //插入动画
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentAnimator != null){
                    mCurrentAnimator.cancel();
                }
                AnimatorSet set  = new AnimatorSet();
                set.
                    play(ObjectAnimator.ofFloat(expandedImageView,View.X,startBounds.left))
                    .with(ObjectAnimator.ofFloat(expandedImageView,View.Y,startBounds.top))
                    .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_X,startScaleFinal))
                    .with(ObjectAnimator.ofFloat(expandedImageView,View.SCALE_Y,startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
}
