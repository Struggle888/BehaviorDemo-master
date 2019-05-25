package com.othershe.behaviortest.test10;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.othershe.behaviortest.R;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class MainActivity10 extends AppCompatActivity {

    ArrayList<Integer> xuanIntList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main10);

//        erfen(24000045672l,3251212);

        xuanIntList.add(1);
        xuanIntList.add(2);
        xuanIntList.add(3);
        xuanIntList.add(4);
//        xuanIntList.add(908);
//        xuanIntList.add(234);
//        xuanIntList.add(34);
//        xuanIntList.add(332);
//        xuanIntList.add(736);
//        xuanIntList.add(24);
//        xuanIntList.add(92);
//        xuanIntList.add(176);
//        xuanIntList.add(212);
//        xuanZhePaiXu(xuanIntList);
//

//        int a  = fact(3);
//        Log.i("info","a="+a);

        tudiDigui(1680,640);
        addDigui(xuanIntList,0,0);
    }


    //二分查询（O(log n)）
    private void erfen(long allNum ,int findNum){

        long low = 0 ;
        long hight = allNum ;
        long mid = 0 ;
        while (low<=hight){
            mid = (low+hight)/2 ;
            if (mid > findNum){
                hight = mid;
            }else if(mid == findNum){
                Log.i("info","mid="+mid);
                return ;
            }else if (mid < findNum){
                low = mid;
            }
            Log.i("info","low="+low);

        }

    }

    //选择排序
    private void xuanZhePaiXu(ArrayList<Integer> lists){
        //[4,l43,2,34,2,1,44,1]
        ArrayList<Integer> list = new ArrayList<>() ;
        list.addAll(lists);
        ArrayList<Integer> newList = new ArrayList<>();
        int min = list.get(0) ;
        int minNum = 0 ;

        int allCishu = list.size();
        int cishu = 0 ;

        while (cishu <= allCishu){
            for (int i = 0; i < list.size(); i++) {
                if (min > list.get(i)){
                    min = list.get(i);
                    minNum = i ;
                }
            }
            cishu ++ ;
            newList.add(min);
            if (list.size()>minNum){
                list.remove(minNum);
                if (list.size()>0){
                    min = list.get(0) ;
                    minNum = 0 ;
                }
            }
        }

        if (newList.size()>2){
            newList.remove(newList.size()-1);
        }
        Log.i("info",""+newList);
    }


    private int  fact(int x){

        /**
         *
         *
         *
         */

        if (x == 1){
            //asd
            return 1 ;
        }else {
            return x * fact(x-1);
        }

    }

    private int tudiDigui(int width ,int height){
        if (width == height){
            Log.i("info","width = height ="+width);
            return width ;
        }else{
            if (width < height){
                return tudiDigui((height - width ),width);
            }else{
                return tudiDigui((width - height ),height);
            }
        }
    }


    private int addDigui(ArrayList<Integer> list,int a,int c){

        if (a == list.size()){
            Log.i("info","all="+c);
            return c ;
        }else {
            int b = list.get(a)+c;
            return addDigui(list,(a+1),b);
        }

    }




}
