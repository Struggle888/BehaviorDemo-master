package com.othershe.behaviortest.test9;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.othershe.behaviortest.R;

public class MainBehaviorRecyc extends CoordinatorLayout.Behavior<RecyclerView> {

    Context mContext ;
    public MainBehaviorRecyc() {
    }

    public MainBehaviorRecyc(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context ;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        return dependency instanceof RelativeLayout;
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
        float y = dependency.getHeight()+(dependency.getTranslationY()*dependency.getHeight())/ getHeaderOffset() ;
        if (y<=0){
            y=0;
        }
        child.setY(y);
        return true;
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.main9title_height1);
    }
}
