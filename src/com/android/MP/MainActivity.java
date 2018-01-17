package com.android.MP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.antiexplosionphone.R;
import com.android.utils.FormulaUtil;
import com.android.utils.Utils;

import static com.android.utils.FormulaUtil.$C$2;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;

public class MainActivity extends Activity {

    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    private TextView tv_5;
    private TextView tv_6;
    private TextView tv_7;
    private TextView tv_8;
    private TextView tv_9;
    private Button fanhui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByid();
        setonClick();
        init();

    }

    private void init() {
        Intent intent = getIntent();
        x1 = Double.parseDouble( intent.getStringExtra("x"));
        y1 = Double.parseDouble( intent.getStringExtra("y"));
        z1 = Double.parseDouble( intent.getStringExtra("z"));

        tv_1.setText("≤" + FormulaUtil.shang);
        if (FormulaUtil.shang >= getSX_Max()) {
            tv_2.setText("合格");
        } else {
            tv_2.setText("不合格");
        }
        tv_3.setText(getSX_Max()+"");

        tv_4.setText("≤" + FormulaUtil.zuo);
        if (FormulaUtil.zuo >= getZY_Max()) {
            tv_5.setText("合格");
        } else {
            tv_5.setText("不合格");
        }
        tv_6.setText(getZY_Max()+"");

        tv_7.setText("≤" + FormulaUtil.zong);
        if (FormulaUtil.zong >= getZK_Max()) {
            tv_8.setText("合格");
        } else {
            tv_8.setText("不合格");
        }
        tv_9.setText(getZK_Max()+"");
    }

    private void setonClick() {
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findViewByid() {
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);
        tv_5 = (TextView) findViewById(R.id.tv_5);
        tv_6 = (TextView) findViewById(R.id.tv_6);
        tv_7 = (TextView) findViewById(R.id.tv_7);
        tv_8 = (TextView) findViewById(R.id.tv_8);
        tv_9 = (TextView) findViewById(R.id.tv_9);
        fanhui = (Button) findViewById(R.id.button);
    }

    public double getSX_Max() {
        double max;
        max = Data.sx[0];
        for (int i = 0; i < Data.sx.length; i++) {
            if (Math.abs(Data.sx[i]) > max) max = Math.abs(Data.sx[i]);
        }
        return Utils.BD(max);
    }

    public double getZY_Max() {
        double max;
        max = Data.zy[0];
        for (int i = 0; i < Data.zy.length; i++) {
            if (Math.abs(Data.zy[i]) > max) max = Math.abs(Data.zy[i]);
        }
        return Utils.BD(max);
    }

    double x1, y1, z1;

    public double getZK_Max() {
        double max;
        Double z =  -FormulaUtil.kongshen * Math.cos(($C$2+90)*$F$1);
        Double x = FormulaUtil.kongshen * Math.sin(($C$2+90)*$F$1) * Math.cos($E$2*$F$1);
        Double y =  FormulaUtil.kongshen * Math.sin(($C$2+90)*$F$1) * Math.sin($E$2*$F$1);
        max = Utils.BD((x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z));
        return Utils.BD(Math.sqrt(max));
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
