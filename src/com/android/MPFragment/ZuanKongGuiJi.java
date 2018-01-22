package com.android.MPFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.antiexplosionphone.R;

import java.util.ArrayList;
import java.util.List;

import static com.android.antiexplosionphone.R.id.tab3;


/**
 * Created by jiangchao on 2016/11/14.
 */

public class ZuanKongGuiJi extends FragmentActivity implements View.OnClickListener {


    private Button tab1;
    private Button tab2;
    private Button fanhui;
    private Button amplify;
    private TextView text_note;
    private NoScrollViewPager viewPager;
    private List<Fragment> list = new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_v);

        findView();

        init();
    }

    private void findView() {
        viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        tab1 = (Button) findViewById(R.id.tab1);
        tab2 = (Button) findViewById(R.id.tab2);
        fanhui = (Button) findViewById(tab3);
        text_note = (TextView) findViewById(R.id.text_note);
        amplify = (Button) findViewById(R.id.amplify);

    }


    private void init() {

        tab2.setOnClickListener(this);
        tab1.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        amplify.setOnClickListener(this);
        list.add(new Vertical());
        list.add(new Horizontal());

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnClickListener(this);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // 页面选中完毕
                System.out.println("onPageSelected:" + arg0);
                // 移动完
                if (arg0 == 0) {
                    View view = getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else if (arg0 == 1) {
                    View view = getWindow().peekDecorView();
                    if (view != null) {
                        InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                } else if (arg0 == 2) {

                    // f3.starnotifyDataSetChanged();
                }
            }

            /*
             * arg0=页面的索引 arg1= 方向值 0.9 0.2 arg2=移动的像素值 * @see
             * android.support.v4
             * .view.ViewPager.OnPageChangeListener#onPageScrolled(int, float,
             * int)
             */
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // 页面滑动

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // 页面活动状态改变

                System.out.println("onPageScrollStateChanged:" + arg0);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tab1) {
            viewPager.setCurrentItem(0);
            tab1.setVisibility(View.GONE);
            tab2.setVisibility(View.VISIBLE);
            text_note.setText("上下偏差图");
        } else if (v.getId() == R.id.tab2) {
            viewPager.setCurrentItem(1);
            tab1.setVisibility(View.VISIBLE);
            tab2.setVisibility(View.GONE);
            text_note.setText("左右偏差图");
        } else if (v.getId() == R.id.tab3) {
            finish();
        }else if (v.getId() == R.id.amplify){
            Intent intent = new Intent();
            if (viewPager.getCurrentItem()==0){
                intent.setClass(this,Amplify_vertical.class);
            }else {
                intent.setClass(this,Amplify_horizontal.class);
            }
            startActivity(intent);
        }
    }

    public class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        // @Override
        // public CharSequence getPageTitle(int position) {
        // return titleList.get(position);
        // }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


}
