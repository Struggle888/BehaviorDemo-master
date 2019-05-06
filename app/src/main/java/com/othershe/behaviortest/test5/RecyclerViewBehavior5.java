package com.othershe.behaviortest.test5;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.othershe.behaviortest.R;

public class RecyclerViewBehavior5 extends CoordinatorLayout.Behavior<RecyclerView> {

    public RecyclerViewBehavior5() {
    }

    public RecyclerViewBehavior5(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return true;
    }
    //dependency.getId() == R.id.first

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {

        //计算列表y坐标，最小为0
        float y = dependency.getHeight() + dependency.getTranslationY();
//        Log.i("info","y="+y+" dependency.getHeight()="+dependency.getHeight()+"  dependency.getTranslationY()="+dependency.getTranslationY());

        if (y < 0) {
            y = 0;
        }
        child.setY(y);
        return true;
    }
}
