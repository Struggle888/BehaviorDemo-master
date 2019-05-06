package com.othershe.behaviortest.demno;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.behaviortest.R;
import com.othershe.behaviortest.source.HeaderScrollingViewBehavior;

import java.util.List;

public class MainContentBehavior extends HeaderScrollingViewBehavior {

    private Context mContext ;

    public MainContentBehavior() {
    }

    public MainContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        float contentScrollY = dependency.getTranslationY() / getHeaderOffer()
                *(dependency.getHeight() - getFinalHeight());
        float y = dependency.getHeight() - contentScrollY ;
        child.setY(y);

        return true;
    }

    @Override
    protected View findFirstDependency(List<View> views) {

        for (int i = 0 , z = views.size(); i < z ; i++) {
            View view = views.get(i);
            if (isDependOn(view)){
                return  view;
            }
        }

        return null;
    }

    @Override
    protected int getScrollRange(View v) {

        if (isDependOn(v)){
            return Math.max(0,v.getMeasuredHeight() - getFinalHeight());
        }else{
            return super.getScrollRange(v);
        }

    }

    private int getHeaderOffer(){
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private int getFinalHeight(){

        return mContext.getResources().getDimensionPixelOffset(R.dimen.tab_height)+
                mContext.getResources().getDimensionPixelOffset(R.dimen.title_height);

    }


    private boolean isDependOn(View dependency){
        return dependency != null && dependency.getId() == R.id.header;
    }

}
