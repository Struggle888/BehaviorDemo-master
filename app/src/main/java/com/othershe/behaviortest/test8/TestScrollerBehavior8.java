package com.othershe.behaviortest.test8;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.othershe.behaviortest.R;

import java.lang.ref.WeakReference;

import javax.crypto.Mac;

public class TestScrollerBehavior8 extends CoordinatorLayout.Behavior<RecyclerView> {

    private boolean isExpanded = false;
    private boolean isScrolling = false;

    private WeakReference<View> dependentView;
    private Scroller scroller;
    private Handler handler;

    public TestScrollerBehavior8() {
    }

    public TestScrollerBehavior8(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        if (dependency != null && dependency.getId() == R.id.scrolling_header) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            child.layout(0, 0, parent.getWidth(), (int) (parent.getHeight() - getDependentViewCollapsedHeight()));
            return true;
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {

        Resources resoutces = getDependentView().getResources();

        final float progress = 1.f - Math.abs(dependency.getTranslationY()) /
                (dependency.getHeight() - resoutces.getDimension(R.dimen.collapsed_header_height));

        child.setTranslationY(dependency.getHeight() + dependency.getTranslationY());


        float scale = 1 + 0.4f * (1.f - progress);

        dependency.setScaleX(scale);
        dependency.setScaleY(scale);
        dependency.setAlpha(progress);

        return true;
    }

    private float getDependentViewCollapsedHeight() {
        return getDependentView().getResources().getDimension(R.dimen.collapsed_header_height);
    }

    private View getDependentView() {
        return dependentView.get();
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        scroller.abortAnimation();
        isScrolling = false;

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        if (dy < 0) {
            return;
        }
        //通过监控自身recyclerview的变化 dy 去改变顶部 image 及 自身recyclerview 的位置
        // 获取到顶部 imageview
        View dependentView = getDependentView();

        // 第一次时 dependentView.getTranslationY() 为 0  dy 为偏移量
        float newTranslateY = dependentView.getTranslationY() - dy;

        // 可以移动的最大距离
        float minHeaderTranslate = -(dependentView.getHeight() - getDependentViewCollapsedHeight());

        if (newTranslateY > minHeaderTranslate) {
            dependentView.setTranslationY(newTranslateY);
            consumed[1] = dy;
        }

    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        if (dyUnconsumed >0){
            return;
        }
        View dependentView = getDependentView();
        float newTranslateY = dependentView.getTranslationY() - dyUnconsumed;
        final float maxHeaderTranslate = 0;

        if (newTranslateY < maxHeaderTranslate) {
            dependentView.setTranslationY(newTranslateY);
        }

    }


    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout,
                                    @NonNull RecyclerView child, @NonNull View target,
                                    float velocityX, float velocityY) {

        return onUserStopDragging(velocityY);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull RecyclerView child, @NonNull View target, int type) {
        if (!isScrolling){
            onUserStopDragging(800);
        }
    }

    private boolean onUserStopDragging(float velocityY) {

        View dependentView = getDependentView();
        float translateY = dependentView.getTranslationY();

        float minHeaderTranslate = -(dependentView.getHeight() - getDependentViewCollapsedHeight());

        if (translateY == 0 || translateY == minHeaderTranslate){
            return false ;
        }

        boolean targetState ;
        if (Math.abs(velocityY) <= 800){
            if (Math.abs(translateY) < Math.abs(translateY - minHeaderTranslate)){
                targetState = false ;//展开
            }else{
                targetState = true ; // 折叠
            }
        }else{
            if (velocityY>0){
                targetState = true ; // 折叠
            }else{
                targetState = false ;//展开
            }
        }

        float targetTranslateY = targetState ? minHeaderTranslate : 0 ;

        scroller.startScroll(0,(int)translateY,0,(int)(targetTranslateY - translateY),(int)(1000000/Math.abs(velocityY)));
        handler.post(flingRunnable);
        isScrolling = true ;
        return true ;

    }


    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {

            if (scroller.computeScrollOffset()){
                getDependentView().setTranslationY(scroller.getCurrY());
                handler.post(this);
            }else{
                isExpanded = getDependentView().getTranslationY() !=0 ;
                isScrolling = false ;
            }

        }
    };

}





















