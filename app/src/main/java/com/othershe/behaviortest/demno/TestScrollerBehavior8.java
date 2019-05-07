package com.othershe.behaviortest.demno;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.othershe.behaviortest.R;

import java.lang.ref.WeakReference;

import javax.crypto.Mac;

public class TestScrollerBehavior8 extends CoordinatorLayout.Behavior<RecyclerView> {

    private boolean isScrooler = false ;
    private boolean isExpanded = false ;

    private WeakReference<View> dependent;
    private Handler mHandler ;
    private Scroller mScroller ;

    public TestScrollerBehavior8() {
    }

    public TestScrollerBehavior8(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mHandler = new Handler();
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT){
            child.layout(0,0,(int)parent.getWidth(),(int)(parent.getHeight()-getDependViewHeight()));
            return  true ;
        }

        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        if (dependency != null && dependency.getId() == R.id.scrolling_header){
            dependent = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {

        Resources resources = getDependView().getResources() ;

        float progress = 1.0f - (Math.abs(dependency.getTranslationY())/
                (Math.abs(getDependView().getHeight()-getDependView().getResources().getDimension(R.dimen.collapsed_header_height))));

        child.setTranslationY(dependency.getHeight()+dependency.getTranslationY());

        float scale = 1 + 0.4f*(1.0f - progress);

        dependency.setScaleY(scale);
        dependency.setScaleX(scale);
        dependency.setAlpha(progress);

        return true;
    }

    private float getDependViewHeight(){
        return  getDependView().getResources().getDimension(R.dimen.collapsed_header_height);
    }

    private View getDependView(){
        return dependent.get();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        mScroller.abortAnimation();
        isScrooler = false ;

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);

    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if (dy<0){
            return ;
        }


        float finalY = getDependView().getTranslationY() - dy ;

        float minHeight = Math.abs(getDependView().getHeight()-getDependView().getResources().getDimension(R.dimen.collapsed_header_height));
        if (Math.abs(finalY)<minHeight){
            getDependView().setTranslationY(finalY);
            consumed[1] = dy ;
        }



    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (dyUnconsumed>0){
            return ;
        }

        float finalY = getDependView().getTranslationY() - dyUnconsumed ;

        float minHeight = -getDependView().getHeight()-getDependView().getResources().getDimension(R.dimen.collapsed_header_height);
        if (finalY<minHeight){
            getDependView().setTranslationY(finalY);
        }

    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return onUserStopDragging(velocityY);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int type) {
        if (!isScrooler){
            onUserStopDragging(800);
        }
    }

    private boolean onUserStopDragging(float velocityY) {

        View dependentView = getDependView();
        float translateY = dependentView.getTranslationY();
        float minHeaderTranslate = -(dependentView.getHeight() - getDependViewHeight());

        if (translateY == 0 || translateY == minHeaderTranslate) {
            return false;
        }

        boolean targetState;
        if (Math.abs(velocityY)<800){

            if (getDependView().getTranslationY()>-(getDependView().getHeight()/2)){
                targetState = false;
            }else{
                targetState = true;
            }
        }else{
            if (velocityY>0){
                targetState = true;
            }else{
                targetState = false;
            }
        }
        float targetTranslateY = targetState ? minHeaderTranslate : 0;
        mScroller.startScroll(0, (int) translateY, 0,
                (int) (targetTranslateY - translateY),
                (int) (1000000 / Math.abs(velocityY)));
        mHandler.post(flingRunnable);
        return true ;
    }



    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {

            if (mScroller.computeScrollOffset()){
                getDependView().setTranslationY(mScroller.getCurrY());
                mHandler.post(this);
            }else{
                isScrooler = false ;
            }

        }
    };

}
