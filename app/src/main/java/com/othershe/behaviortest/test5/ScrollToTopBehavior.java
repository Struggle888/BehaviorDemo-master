package com.othershe.behaviortest.test5;

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
import android.widget.TextView;

import java.text.DecimalFormat;

public class ScrollToTopBehavior extends CoordinatorLayout.Behavior<View> {
    int offsetTotal = 0;
    boolean scrolling = false;

    public ScrollToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull
            View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        if (target instanceof RecyclerView) {


            RecyclerView list = (RecyclerView) target;
            int pos = ((LinearLayoutManager) list.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

//
//            int old = offsetTotal;
//            int top = offsetTotal - dyConsumed;
//
//            top = Math.max(top, -child.getHeight());
//            top = Math.min(top, 0);
//            offsetTotal = top;

//            int dex = offsetTotal - old;

//            Log.i("info", "dex = " + dex);
//            Log.i("info", "offsetTotal = " + offsetTotal);
//                child.offsetTopAndBottom(dex);

            if (pos < 3){
                int offsetTotal = (int)child.getTranslationY() - dy;
                Log.i("info", "offsetTotal = " + offsetTotal);
                if (offsetTotal < -child.getHeight()) {
                    offsetTotal = -child.getHeight();
                } else if (offsetTotal > 0) {
                    offsetTotal = 0;
                }
                child.setTranslationY(offsetTotal);
//                consumed[1]= offsetTotal;
            }



//            float alpha = (float) (child.getHeight() - Math.abs(offsetTotal)) / child.getHeight();
//
//            Log.i("info", "Math.abs(offsetTotal)= " + Math.abs(offsetTotal) + "  child.getHeight()=" + (float) child.getHeight() + "  alpha=" + alpha);
//            child.setAlpha(alpha);

//            int old = offsetTotal;
//            int top = offsetTotal - dyConsumed;
//            top = Math.max(top, -child.getHeight());
//            top = Math.min(top, child.getHeight());
//            offsetTotal = top;
//
//
//
////            if (old == offsetTotal){
////                scrolling = false;
////                return;
////            }
//            int delta = offsetTotal-old;
//            child.offsetTopAndBottom(delta);
//            scrolling = true;


        }
    }

//    @Override
//    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
//                               @NonNull View target, int dxConsumed, int dyConsumed,
//                               int dx, int dy, int type) {
////        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
//
//    }

}