package com.othershe.behaviortest.test6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.TextView;

import com.othershe.behaviortest.mainpage.behavior.MainHeaderBehavior;
import com.othershe.behaviortest.source.ViewOffsetBehavior;

import java.lang.ref.WeakReference;

public class TestBehavior6 extends ViewOffsetBehavior<FrameLayout> {

    //界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    //列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    //列表上一个全部可见的item位置
    private int lastPosition = -1;


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, FrameLayout child, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                upReach = false;
                downReach = false;
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(child);
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    public TestBehavior6() {
        init();
    }


    @Override
    protected void layoutChild(CoordinatorLayout parent, FrameLayout child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<>(parent);
        mChild = new WeakReference<>(child);
    }


    public TestBehavior6(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(mContext);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FrameLayout child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FrameLayout child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if (target instanceof RecyclerView) {

            RecyclerView list = (RecyclerView) target;

            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstVisibleItemPosition();

//            Log.i("info","pos="+pos+"=lastPosition="+lastPosition);
            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }

            if (canScroller(child, dy) && pos == 0) {

                float finalY = child.getTranslationY() - dy;

                if (finalY < -child.getHeight()) {
                    finalY = -child.getHeight();
                    upReach = true;
                } else if (finalY > 0) {
                    finalY = 0;
                }

                child.setTranslationY(finalY);
                consumed[1] = dy;
            }
//            Log.i("info","lastPosition="+lastPosition+"=pos="+pos);
            lastPosition = pos;
        }

    }

    private boolean canScroller(View child, int scrollerY) {

        if (scrollerY > 0 && child.getTranslationY() == -child.getHeight() && !upReach) {
            return false;
        }

        if (downReach) {
            return false;
        }

        return true;
    }



    private FlingRunnable mFlingRunnable;
    private OverScroller mOverScroller;
    private Context mContext;
    private WeakReference<CoordinatorLayout> mParent;//CoordinatorLayout
    private WeakReference<FrameLayout> mChild;//CoordinatorLayout的子View，即header
    private void handleActionUp(View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        //手指抬起时，header上滑距离超过总距离三分之一，则整体自动上滑到关闭状态
        Log.i("info","handleActionUp child.getTranslationY()="+child.getTranslationY());
        Log.i("info","handleActionUp child.getHeight()="+child.getHeight()/ 3.0f);
        if (child.getTranslationY() < -child.getHeight() / 2.5f) {
            scrollToClose(300);
        } else {
            scrollToOpen(300);
        }
    }


    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    mLayout.setTranslationY(mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mLayout);
                }
            }
        }
    }
    private void scrollToClose(int duration) {
        int curTranslationY = (int) mChild.get().getTranslationY();
        int dy = -mChild.get().getHeight() - curTranslationY;
        mOverScroller.startScroll(0, curTranslationY, 0, dy, duration);

        Log.i("info","scrollToClose =  curTranslationY="+curTranslationY+"  dy="+dy);
        start();
    }

    private void scrollToOpen(int duration) {
        float curTranslationY = mChild.get().getTranslationY();
        mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
        Log.i("info","scrollToOpen =  curTranslationY="+curTranslationY);
        start();
    }

    private void start() {
        if (mOverScroller.computeScrollOffset()) {
            mFlingRunnable = new FlingRunnable(mParent.get(), mChild.get());
            ViewCompat.postOnAnimation(mChild.get(), mFlingRunnable);
        } else {
            onFlingFinished(mChild.get());
        }
    }

    private void onFlingFinished(View layout) {

    }






}
