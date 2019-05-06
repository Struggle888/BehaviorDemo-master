package com.othershe.behaviortest.test7;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.othershe.behaviortest.R;

public class TestBehavior7 extends CoordinatorLayout.Behavior<RelativeLayout> {

    private float deltaY = 0;
    public TestBehavior7() {
    }
    public TestBehavior7(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {

        if (deltaY == 0) {
            deltaY = dependency.getY() - child.getHeight();
        }

        float dy = dependency.getY() - child.getHeight();

        dy = dy < 0 ? 0 : dy;

        float y = -(dy/deltaY)*child.getHeight();

        child.setTranslationY(y);

        return true;
    }
}
