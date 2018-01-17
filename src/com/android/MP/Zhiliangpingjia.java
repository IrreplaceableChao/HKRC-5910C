package com.android.MP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.antiexplosionphone.R;
import com.android.utils.ConstantDef;
import com.android.utils.SharedPreferencesHelper;


/**
 * Created by jiangchao on 2016/11/11.
 */

public class Zhiliangpingjia extends Activity {
    private TextView editText0;
    private TextView editText1;
    private TextView editText2;
    private EditText editText3;
    private EditText editText4;
    private EditText editText5;
    private EditText editText6;
    private EditText editText7;
    private EditText editText8;
    private Button button;
    private LinearLayout ll_text_1;
    private SharedPreferencesHelper SPconfig;
    public static String mSJFilePath;  // 采集有效点数据文件
    public static String []spStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhiliangpingjia);
        findViewById();
        setOnClick();
        init();
    }

    private void setOnClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editBuilderS(editText6,"请填上下偏差！","请重新输入上下偏差应在0-100m。"))return;
                if (!editBuilderS(editText7,"请填左右偏差！","请重新输入左右偏差应在0-100m。"))return;
                if (!editBuilderS(editText8,"请填终孔偏差！","请重新输入终孔偏差应在0-100m。"))return;

                if (editText3.getText() == null || editText3.getText().toString().trim().length() == 0) {
                    editBuilder("请填设计倾角！");
                    return;
                } else {
                    int jiangetemp = Integer.parseInt(editText3.getText().toString().trim());
                    if (jiangetemp > 90 || jiangetemp < -90) {
                        editBuilder("设计倾角应在-90-90°范围内！");
                        return;
                    }
                }
                if (editText4.getText() == null || editText4.getText().toString().trim().length() == 0) {
                    editBuilder("请填设计方位角！");
                    return;
                } else {
                    int jiangetemp = Integer.parseInt(editText4.getText().toString().trim());
                    if (jiangetemp >= 360 || jiangetemp < 0) {
                        editBuilder("设计方位角应在0-360°范围内！");
                        return;
                    }
                }
                if (editText5.getText() == null || editText5.getText().toString().trim().length() == 0) {
                    editBuilder("请填设计孔深！");
                    return;
                } else {
                    int jiangetemp = Integer.parseInt(editText5.getText().toString().trim());
                    if (jiangetemp > 2000 || jiangetemp < 1) {
                        editBuilder("设计孔深应在1-2000m范围内！");
                        return;
                    }
                }
                saveData();
                Intent intent = new Intent();
                intent.setClass(Zhiliangpingjia.this, Data.class);
                startActivity(intent);
            }
        });
    }

    private void findViewById() {
        button = (Button) findViewById(R.id.button);
        editText0 = (TextView) findViewById(R.id.text_0);
        editText1 = (TextView) findViewById(R.id.text_1);
        editText2 = (TextView) findViewById(R.id.text_2);
        editText3 = (EditText) findViewById(R.id.text_3);
        editText4 = (EditText) findViewById(R.id.text_4);
        editText5 = (EditText) findViewById(R.id.text_5);
        editText6 = (EditText) findViewById(R.id.text_6);
        editText7 = (EditText) findViewById(R.id.text_7);
        editText8 = (EditText) findViewById(R.id.text_8);
        ll_text_1 = (LinearLayout) findViewById(R.id.ll_text_1);
    }

    private void init() {
        SPconfig = new SharedPreferencesHelper(this, "SET_START");

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        spStr = path.split("-");

        if (spStr[1].equals("P")) {
            ll_text_1.setVisibility(View.GONE);
            editText0.setText(spStr[2]);
            editText2.setText(spStr[3]);
            mSJFilePath = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + path +"/"+ spStr[1]+"-"+spStr[2]+"-"+spStr[5]+ ConstantDef.FILE_SUFFIX_SJ_POINT;
        } else {
            ll_text_1.setVisibility(View.VISIBLE);
            editText0.setText(spStr[2]);
            editText1.setText(spStr[3]);
            editText2.setText(spStr[4]);
            mSJFilePath = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + path +"/"+ spStr[1]+"-"+spStr[2]+"-"+spStr[3]+"-"+spStr[6]+ ConstantDef.FILE_SUFFIX_SJ_POINT;
        }
        getSaveData();
    }

    private void getSaveData(){
        if (SPconfig.getString("qinjiao") != null) editText3.setText(SPconfig.getString("qinjiao"));
        if (SPconfig.getString("fangwei") != null) editText4.setText(SPconfig.getString("fangwei"));
        if (SPconfig.getString("kongshen") != null) editText5.setText(SPconfig.getString("kongshen"));
        if (SPconfig.getString("shang") != null) editText6.setText(SPconfig.getString("shang"));
        if (SPconfig.getString("zuo") != null) editText7.setText(SPconfig.getString("zuo"));
        if (SPconfig.getString("zong") != null) editText8.setText(SPconfig.getString("zong"));
    }
    private void saveData() {

        SPconfig.putValue("gongzuomian", editText0.getText().toString());
        SPconfig.putValue("zuanchang", editText1.getText().toString());
        SPconfig.putValue("konghao", editText2.getText().toString());
        SPconfig.putValue("qinjiao", editText3.getText().toString().trim());
        SPconfig.putValue("fangwei", editText4.getText().toString().trim());
        SPconfig.putValue("kongshen", editText5.getText().toString().trim());
        SPconfig.putValue("shang", editText6.getText().toString().trim());
        SPconfig.putValue("zuo", editText7.getText().toString().trim());
        SPconfig.putValue("zong", editText8.getText().toString().trim());
    }
    private boolean editBuilderS(EditText edit,String str,String str1){

        if (edit.getText() == null || edit.getText().toString().length() == 0) {
            editBuilder(str);
            return false;
        } else if (0 >= Integer.parseInt(edit.getText().toString().trim()) || Integer.parseInt(edit.getText().toString().trim())> 100) {
            editBuilder(str1);
            return false;
        }
        return  true;
    }

    private void editBuilder(String str) {
        new AlertDialog.Builder(Zhiliangpingjia.this).setTitle("提示").setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false).show();
    }
}
