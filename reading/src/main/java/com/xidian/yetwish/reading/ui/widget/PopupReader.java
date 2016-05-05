package com.xidian.yetwish.reading.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.xidian.yetwish.reading.R;
import com.xidian.yetwish.reading.framework.utils.ScreenUtils;

/**
 * todo  progress 联动
 * Created by Yetwish on 2016/5/5 0005.
 */
public class PopupReader extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private View mTopContainer;

    private View mBottomContainer;

    private ImageView ivBack;

    private TextView tvPreChapter;

    private TextView tvNextChapter;

    private NumberProgressBar pbProgress;

    private ImageView ivMenu;

    private ImageView ivTextSize;

    private ImageView ivBookMark;

    private ImageView ivNote;

    private int topBarHeight;
    private int bottomBarHeight;
    //todo one more

    public PopupReader(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
        View root = LayoutInflater.from(mContext).inflate(R.layout.popup_reader, null);
        setContentView(root);

        this.setHeight(ScreenUtils.getScreenHeight(mContext));
        this.setWidth(ScreenUtils.getScreenWidth(mContext));

        this.setOutsideTouchable(true);
        this.update();

        root.findViewById(R.id.midContainer).setOnClickListener(this);
        mTopContainer = root.findViewById(R.id.rlReaderTopContainer);
        mBottomContainer = root.findViewById(R.id.llReaderBottomContainer);

        topBarHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.reader_popup_top_height);
        bottomBarHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.reader_popup_bottom_height);
    }


    public void show(View parentView) {
        if (this.isShowing())
            return;
        showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
        enterAnimation();

    }

    private void enterAnimation() {
        ObjectAnimator topEnterAni = new ObjectAnimator().ofFloat(mTopContainer, "y", -topBarHeight, 0);
        topEnterAni.setDuration(200);

        ObjectAnimator bottomEnterAni = new ObjectAnimator().ofFloat(mBottomContainer,
                "y", ScreenUtils.getScreenHeight(mContext), ScreenUtils.getScreenHeight(mContext) - bottomBarHeight);
        bottomEnterAni.setDuration(200);

        topEnterAni.start();
        bottomEnterAni.start();
    }

    private void exitAnimation() {
        ObjectAnimator topExitAni = new ObjectAnimator().ofFloat(mTopContainer, "y", 0, -topBarHeight);
        topExitAni.setDuration(150);

        ObjectAnimator bottomExitAni = new ObjectAnimator().ofFloat(mBottomContainer,
                "y", ScreenUtils.getScreenHeight(mContext) - bottomBarHeight, ScreenUtils.getScreenHeight(mContext));
        bottomExitAni.setDuration(150);

        topExitAni.addListener(mAnimationListener);
        bottomExitAni.addListener(mAnimationListener);

        topExitAni.start();
        bottomExitAni.start();
    }

    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            dismiss();
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };



    public void hide() {
        //animation
        exitAnimation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.midContainer:
                hide();
                break;
        }
    }
}
