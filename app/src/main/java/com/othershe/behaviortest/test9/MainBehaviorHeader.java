package com.othershe.behaviortest.test9;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.othershe.behaviortest.R;

public class MainBehaviorHeader extends CoordinatorLayout.Behavior<RelativeLayout> {

    //界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    //列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    //列表上一个全部可见的item位置
    private int lastPosition = -1;
    private Context mContext;

    private float finalTransy = 0;
    public MainBehaviorHeader() {
    }

    public MainBehaviorHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, RelativeLayout child, MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downReach = false ;
                upReach = false ;
                break;
            case MotionEvent.ACTION_UP:
                Log.i("info","finalTransy"+finalTransy);
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    boolean is = true;

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RelativeLayout child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        /**
         *
         * 1 onInterceptTouchEvent 拦截手势
         * 2 获取目标recyclerview 的实例
         * 3 实时获取第一个可以看见的item的下标看是不是第一个
         * 4 判断 如果pos 是第一个 说明可以开始往下滑动了 并且判端是从 下标2 开始往下标1 移动
         * 5
         *
         *
         *
         *
         *
         */

//        if (target instanceof RecyclerView){
//            RecyclerView list = (RecyclerView) target;
//            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

//            Log.i("info","--------------------pos="+pos+"    lastPosition="+lastPosition);
//            if (pos == 0 && pos < lastPosition){
//                Log.i("info","--------------------");
//                downReach = true;
//            }
//
//            canScroll(child, dy);


//            if (pos == 0) {
//                float finalY = child.getTranslationY() - dy;
//                is = true;
//                if (finalY < getHeaderOffset()) {
//                    finalY = getHeaderOffset();
//                    is = false;
//                } else if (finalY > 0) {
//                    finalY = 0;
//                    is = false;
//                }
//                if (is) {
//                    child.setTranslationY(finalY);
//                    consumed[1] = dy;
//                }
//            }
//            if (canScroll(child, dy)) {
                float finalY = child.getTranslationY() - dy;
            Log.i("info","--------------------"+finalY);
                is = true;
//                Log.i("info","child.getTranslationY()= "+ child.getTranslationY()+"    finalY="+finalY);
                if (finalY < getHeaderOffset()) {
                    finalY = getHeaderOffset();
//                    upReach = true;
                    is = false;
                } else if (finalY > 0) {
                    finalY = 0;
                    is = false;
                }
                if(is){
                    Log.i("info","--------------------"+finalY);
                    child.setTranslationY(finalY);
                    consumed[1] = dy;
                }


//            }
//            lastPosition = pos;
//            finalTransy = child.getTranslationY();
//            Log.i("info","--------------------"+child.getTranslationY());

//        }
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.main9title_height);
    }

    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() == getHeaderOffset() && !upReach) {
            return false;
        }

//        if (downReach) {
//            Log.i("info","++++++++++++++++++++++++++++++++++");
//            return false;
//        }

        return true;
    }

}
