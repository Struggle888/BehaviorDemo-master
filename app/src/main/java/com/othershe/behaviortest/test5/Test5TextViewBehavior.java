package com.othershe.behaviortest.test5;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Test5TextViewBehavior extends CoordinatorLayout.Behavior<TextView> {


    public Test5TextViewBehavior() {
    }

    public Test5TextViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
//        return dependency instanceof RecyclerView;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
//
//        Log.i("info","dependency.getTranslationY() = "+dependency.getTranslationY());
//        child.setX(dependency.getX()+200);
//        child.setY(dependency.getY()+200);
//        child.setText(dependency.getX()+","+dependency.getY());
//
//        return true;
//    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        if (target instanceof RecyclerView) {

            child.setText("dy="+dy);

        }
    }

}
