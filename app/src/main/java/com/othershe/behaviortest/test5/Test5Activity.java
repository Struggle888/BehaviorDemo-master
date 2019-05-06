package com.othershe.behaviortest.test5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.othershe.behaviortest.R;
import com.othershe.behaviortest.mainpage.TypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class Test5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test5);

//        findViewById(R.id.btn).setOnTouchListener(new View.OnTouchListener() {
//            @Override public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//
//                    case MotionEvent.ACTION_MOVE:
//                        v.setX(event.getRawX()-v.getWidth()/2);
//                        v.setY(event.getRawY()-v.getHeight()/2);
//                        break;
//                }
//                return false;
//            }
//        });

        RecyclerView commentList = findViewById(R.id.recyc);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i + 1 + "");
        }
        TypeAdapter adapter = new TypeAdapter(this, list, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentList.setLayoutManager(layoutManager);
        commentList.setAdapter(adapter);

    }
}
