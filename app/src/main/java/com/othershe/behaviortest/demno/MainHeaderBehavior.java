package com.othershe.behaviortest.demno;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import com.othershe.behaviortest.R;
import com.othershe.behaviortest.helper.NestedLinearLayout;
import com.othershe.behaviortest.source.ViewOffsetBehavior;

import java.lang.ref.WeakReference;

public class MainHeaderBehavior extends ViewOffsetBehavior<View> {


    private static final int STATE_OPENED = 0;
    private static final int STATE_CLOSED = 1;
    private static final int DURATION_SHORT = 300;
    private static final int DURATION_LONG = 600;

    private int mCurState = STATE_OPENED;
    private OnHeaderStateListener mHeaderStateListener;

    private OverScroller mOverScroller;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;

    //界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;

    private boolean downReach;

    private int lastPosition = -1;

    private FlingRunnable mFlingRunnable;
    private Context mContext;

    private boolean tabSuspension = false;

    public MainHeaderBehavior() {
        init();
    }

    public MainHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(mContext);
    }


    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);

        mParent = new WeakReference<>(parent);
        mChild = new WeakReference<>(child);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int nestedScrollAxes) {
        if (tabSuspension) {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && !isClosed();
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {

        lastPosition = -1;
        return !isClosed();
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downReach = false;
                upReach = false;
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(child);
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }



    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                  @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        float scrollY = dy / 4.0f;

        if (target instanceof NestedLinearLayout) {

            float finalY = child.getTranslationY() - scrollY;
            if (finalY < getHeaderOffset()) {
                finalY = getHeaderOffset();
            } else if (finalY > 0) {
                finalY = 0;
            }

            child.setTranslationY(finalY);
            consumed[1] = dy;
        }else if(target instanceof RecyclerView){
            RecyclerView list = (RecyclerView) target;

            int pos = ((LinearLayoutManager)list.getLayoutManager()).findFirstVisibleItemPosition();
            if (pos == 0 && pos <lastPosition){
                downReach = true ;
            }
            if (pos == 0 && canScroll(child,scrollY)){

                float finalY = child.getTranslationY() - scrollY;

                if (finalY < getHeaderOffset()){
                    finalY = getHeaderOffset() ;
                    upReach = true ;
                }else if (finalY > 0){
                    finalY = 0 ;
                }
                child.setTranslationY(finalY);

                consumed[1] = dy;
            }

            lastPosition = pos ;
        }

    }
    /**
     * 是否可以整体滑动
     *
     * @return
     */
    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() > getHeaderOffset()) {
            return true;
        }

        if (child.getTranslationY() == getHeaderOffset() && upReach) {
            return true;
        }

        if (scrollY < 0 && !downReach) {
            return true;
        }

        return false;
    }


    private void handleActionUp(View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        //手指抬起时，header上滑距离超过总距离三分之一，则整体自动上滑到关闭状态
        if (child.getTranslationY() < getHeaderOffset() / 3.0f) {
            scrollToClose(DURATION_SHORT);
        } else {
            scrollToOpen(DURATION_SHORT);
        }
    }

    private void scrollToOpen(int duration) {
        float curTranslationY = mChild.get().getTranslationY();
        mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
        start();
    }

    private void scrollToClose(int duration) {
        int curTranslationY = (int) mChild.get().getTranslationY();
        int dy = getHeaderOffset() - curTranslationY;
        mOverScroller.startScroll(0, curTranslationY, 0, dy, duration);
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
        changeState(isClosed(layout) ? STATE_CLOSED : STATE_OPENED);
    }

    private boolean isClosed(View child) {
        return child.getTranslationY() == getHeaderOffset();
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }

    private void changeState(int newState) {

        if (mCurState != newState) {
            mCurState = newState;

            if (mHeaderStateListener == null) {
                return;
            }
            if (mCurState == STATE_OPENED) {
                mHeaderStateListener.onHeaderOpened();
            } else {
                mHeaderStateListener.onHeaderClosed();
            }
        }

    }


    private class FlingRunnable implements Runnable {

        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout mParent, View mLayout) {
            this.mParent = mParent;
            this.mLayout = mLayout;
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

    public void setTabSuspension(boolean tabSuspension) {
        this.tabSuspension = tabSuspension;
    }

    public void setHeaderStateListener(OnHeaderStateListener headerStateListener) {
        mHeaderStateListener = headerStateListener;
    }

    public interface OnHeaderStateListener {

        void onHeaderClosed();

        void onHeaderOpened();

    }


}
