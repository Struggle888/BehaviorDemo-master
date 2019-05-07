package com.othershe.behaviortest.test1;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.othershe.behaviortest.mainpage.TypeAdapter;
import com.othershe.behaviortest.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity1 extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        RecyclerView recyclerView = findViewById(R.id.my_list);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add(i + 1 + "");
        }
        TypeAdapter adapter = new TypeAdapter(this, list, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

//        TextView title = findViewById(R.id.title);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) title.getLayoutParams();
//        params.setBehavior(new SampleTitleBehavior());
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //垂直方向偏移量
        int offset = Math.abs(verticalOffset);
        Log.i("info", "verticalOffset=" + verticalOffset);
        //最大偏移距离
        int scrollRange = appBarLayout.getTotalScrollRange();
        Log.i("info", "scrollRange=" + appBarLayout.getTotalScrollRange());
//        if (offset <= scrollRange / 2) {//当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
//            toolbarOpen.setVisibility(View.VISIBLE);
//            toolbarClose.setVisibility(View.GONE);
//            //根据偏移百分比 计算透明值
//            float scale2 = (float) offset / (scrollRange / 2);
//            int alpha2 = (int) (255 * scale2);
//            bgToolbarOpen.setBackgroundColor(Color.argb(alpha2, 48, 63, 159));
//        } else {//当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
//            toolbarClose.setVisibility(View.VISIBLE);
//            toolbarOpen.setVisibility(View.GONE);
//            float scale3 = (float) (scrollRange - offset) / (scrollRange / 2);
//            int alpha3 = (int) (255 * scale3);
//            bgToolbarClose.setBackgroundColor(Color.argb(alpha3, 48, 63, 159));
//        }
//        //根据偏移百分比计算扫一扫布局的透明度值
//        float scale = (float) offset / scrollRange;
//        int alpha = (int) (255 * scale);
//        bgContent.setBackgroundColor(Color.argb(alpha, 48, 63, 159));
    }
}
