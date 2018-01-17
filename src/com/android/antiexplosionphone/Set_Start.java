package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bean.SetValueBean;
import com.android.bluetooth.BluetoothTools;
import com.android.utils.ConstantDef;
import com.android.utils.FileUtils;
import com.android.utils.Preferences;
import com.android.utils.SharedPreferencesHelper;
import com.android.utils.TextWatcherMaxInt;
import com.android.utils.TransProtocol;
import com.android.utils.Utils;
import com.android.utils.WenJianM;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;

import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.x;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.Arrays;

import static com.android.utils.ConstantDef.FILE_PATH;

/**
 * 设置和启动界面
 *
 * @author TY
 */
public class Set_Start extends Activity implements OnClickListener {

    private static final String TAG = Self_Detection.class.getSimpleName();

    private EditText mEditDeepNum;
    private EditText mEditDelayTime;
    private EditText mEditIntervalTime;
    private TextWatcherMaxInt mDelayTextWatcher;
    private EditText ET_jiaozhun;
    private EditText mEditFace;
    private EditText mEditDrilling;
    private TextView mTextDrilling;
    private long zms;
    private boolean isben = true;
    private boolean isfang = true;
    private char mnDelayTime = 0; // 单位为分钟
    private char mnIntervalTime = 0; // 单位为秒
    private RadioButton BMceng;// 本煤层button
    private RadioButton YCai;// 预采button
    private LinearLayout BMClayout;
    private LinearLayout YClayout;
    private int mReSendTime = ConstantDef.MAX_RESEND_TIME;
    private int mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    private Button setstartbutton;
    private TextView xiaoshi;
    private TextView tian;
    private TextView fen;
    int yxcl1;
    int yxcl2;
    int yxcl3;
    private int jian;
    private String bory = "P";
    private String bory1 = "F";

    private EditText editText3, editText4, editText5, editText6, editText7,editText8;

    private int WenJianm = WenJianM.getWenJianm();
    private Handler mHandler = new Handler();
    File[] file = new File(Environment.getExternalStorageDirectory() + "/HKCX-SJ").listFiles(); // 接收文件中全部的文件
    private BroadcastReceiver bluetoothTransReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothTools.ACTION_READ_DATA.equals(action)) {

                // 写延时间隔时间命令应答
                if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_DELAY_INTERVAL) {

                    // 写延时间隔时间命令应答，分析应答数据
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_Send_Delay_Interval.getSendCmd(mnDelayTime, mnIntervalTime))) {
                        Utils.toast(Set_Start.this, "写延时间隔时间命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 发送写孔号前10byte指令
                    BluetoothTools.sendData(Set_Start.this, TransProtocol.CMD_Send_HoleID.getSendCmd(mEditDeepNum.getText().toString(), bory, mEditFace.getText().toString(), mEditDrilling.getText().toString(), true), TransProtocol.CMD_TYPE_SEND_HOLE_ID0);
                    resetCtrlParam();
                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_HOLE_ID0) {

                    // 写孔号前10byte应答解析
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_Send_HoleID.getSendCmd(mEditDeepNum.getText().toString(), bory, mEditFace.getText().toString(), mEditDrilling.getText().toString(), true))) {
                        Utils.toast(Set_Start.this, "写孔号前10字节命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 发送写孔号后10byte指令
                    BluetoothTools.sendData(Set_Start.this, TransProtocol.CMD_Send_HoleID.getSendCmd(mEditDeepNum.getText().toString(), bory, mEditFace.getText().toString(), mEditDrilling.getText().toString(), false), TransProtocol.CMD_TYPE_SEND_HOLE_ID1);
                    resetCtrlParam();

                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_HOLE_ID1) {

                    // 写孔号后10byte应答解析
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_Send_HoleID.getSendCmd(mEditDeepNum.getText().toString(), bory, mEditFace.getText().toString(), mEditDrilling.getText().toString(), false))) {
                        Utils.toast(Set_Start.this, "写孔号后10字节命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 发送启动指令
                    BluetoothTools.sendData(Set_Start.this, TransProtocol.CMD_START, TransProtocol.CMD_TYPE_START);
                    resetCtrlParam();

                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_START) {
                    // 停止计时
                    stopTimer();
                    // 启动指令应答解析
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_START)) {
                        Utils.toast(Set_Start.this, "启动命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }

                    // 保存现场包括设定的延时时间、间隔时间和启动时间
                    Preferences.saveDelayandIntervalTime(Set_Start.this, mnDelayTime, mnIntervalTime);
                    Preferences.saveStartTime(Set_Start.this, Utils.getCurTime().toMillis(false));
                    Preferences.saveStep(Set_Start.this, ConstantDef.STATUS_DELAY);
                    // 创建保存数据路径
                    if (isfang == true) {//  防治水F  瓦斯W
                        if (isben == true) { // 平行空P     扇形孔S
                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";


                            File fpath = new File(FILE_PATH);
                            if (!fpath.exists()) {
                                fpath.mkdirs();
                            } else {
                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            }
                        } else {
                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            File fpath = new File(FILE_PATH);

                            if (!fpath.exists()) {
                                fpath.mkdirs();
                            } else {
                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            }
                        }
                    } else {
                        if (isben == true) {
                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";


                            File fpath = new File(FILE_PATH);
                            if (!fpath.exists()) {
                                fpath.mkdirs();
                            } else {
                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            }
                        } else {
                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            File fpath = new File(FILE_PATH);

                            if (!fpath.exists()) {
                                fpath.mkdirs();
                            } else {
                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                            }
                        }
                    }
                    Log.i("aaaaaaaaaaaaaaaaaaa", FILE_PATH);
                    // 进入确定有效点界面
                    Intent pointIntent = new Intent();
                    pointIntent.setClass(Set_Start.this, Point_Collect.class);
                    Set_Start.this.startActivity(pointIntent);
                    Set_Start.this.finish();


                    // 强制关闭蓝牙
                    // BluetoothAdapter.getDefaultAdapter().disable();
                    // BluetoothClientService.close();
                    // Utils.toast(Set_Start.this, "蓝牙已关闭！");
                    // 生成新的有效点文件
                    //if(isfang){
                    if (isben) {
                        String filePath = FILE_PATH + "P" + "-" + mEditFace.getText().toString() + "-"
                                // +mEditDrilling.getText().toString()+"-"
                                + mEditDeepNum.getText().toString() + ConstantDef.FILE_SUFFIX_VALID_POINT;
                        FileUtils.FileNewCreate(filePath);
                    } else {
                        String filePath = FILE_PATH + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + ConstantDef.FILE_SUFFIX_VALID_POINT;
                        FileUtils.FileNewCreate(filePath);
                    }
//					}else{
//						if (isben) {
//							String filePath = ConstantDef.FILE_PATH +"Y"
//									+ "-"+ "P" + "-"
//									+ mEditFace.getText().toString() + "-"
//									// +mEditDrilling.getText().toString()+"-"
//									+ mEditDeepNum.getText().toString()
//									+ ConstantDef.FILE_SUFFIX_VALID_POINT;
//							FileUtils.FileNewCreate(filePath);
//						} else {
//							String filePath = ConstantDef.FILE_PATH +"Y"
//									+ "-"+ "S" + "-"
//									+ mEditFace.getText().toString() + "-"
//									+ mEditDrilling.getText().toString() + "-"
//									+ mEditDeepNum.getText().toString()
//									+ ConstantDef.FILE_SUFFIX_VALID_POINT;
//							FileUtils.FileNewCreate(filePath);
//						}

                    //}
                }
            }
        }

    };

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Log.v(TAG, "mReSendCnt = " + mReSendCnt + " mReSendTime = " + mReSendTime);
            if ((TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_DELAY_INTERVAL) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_HOLE_ID0) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SEND_HOLE_ID1) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_START)) {
                if (mReSendCnt < 1) {
                    Log.e("mReSendCnt", mReSendCnt + "");
                    if (--mReSendTime < 1) {
                        mReSendCnt--;
                        mReSendTime = ConstantDef.MAX_RESEND_TIME;
                    }
                } else {
                    Utils.toast(Set_Start.this, "启动失败，请检查蓝牙连接情况！");
                    stopTimer();
                    TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                    return;
                }
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void startTimer() {
        mHandler.postDelayed(runnable, 1000);
    }

    protected File setToNow() {
        // TODO Auto-generated method stub
        return null;
    }

    private void stopTimer() {
        mHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_start);
        findviewById();
        setOnClick();
        init();
        getSaveData();
    }
    private SharedPreferencesHelper SPconfig;
    private void findviewById() {
        editText3 = (EditText) findViewById(R.id.text_3);
        editText4 = (EditText) findViewById(R.id.text_4);
        editText5 = (EditText) findViewById(R.id.text_5);
        editText6 = (EditText) findViewById(R.id.text_6);
        editText7 = (EditText) findViewById(R.id.text_7);
        editText8 = (EditText) findViewById(R.id.text_8);
    }

    private void setOnClick() {


    }



    private void init() {
        SPconfig = new SharedPreferencesHelper(this, "SET_START");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_READ_DATA);
        registerReceiver(bluetoothTransReceiver, intentFilter);
        setstartbutton = (Button) findViewById(R.id.btn_set_start);
        setstartbutton.setOnClickListener(this);
        findViewById(R.id.btn_back_menu).setOnClickListener(this);
        //有效点采集时间
        xiaoshi = (TextView) findViewById(R.id.tv_xiaoshi);
        tian = (TextView) findViewById(R.id.tv_tian);
        fen = (TextView) findViewById(R.id.tv_fen);
        // 本煤层孔radiobutton
        findViewById(R.id.BMceng).setOnClickListener(this);
        findViewById(R.id.YCai).setOnClickListener(this);
        findViewById(R.id.FZshui).setOnClickListener(this);
        findViewById(R.id.YCCai).setOnClickListener(this);
        // BMClayout=(LinearLayout) findViewById(R.id.BMCLayout);
        mEditFace = (EditText) findViewById(R.id.et_face);
        mEditDrilling = (EditText) findViewById(R.id.et_drilling);
        mTextDrilling = (TextView) findViewById(R.id.tv_drilling);
        mEditDeepNum = (EditText) findViewById(R.id.et_deepnum);

        mEditDeepNum.setText(Preferences.getHoleIDString(this));
        mEditDrilling.setText(Preferences.getDrillingString(this));
        mEditFace.setText(Preferences.getFaceString(this));
        ET_jiaozhun = (EditText) findViewById(R.id.et_jiaozhun);
        ET_jiaozhun.setText(Integer.toString(Preferences.getJiaoZhun(this)));
        mEditDelayTime = (EditText) findViewById(R.id.et_delaytime);
        mEditDelayTime.setText(Integer.toString(Preferences.getDelayTime(this)));
        mEditIntervalTime = (EditText) findViewById(R.id.et_intervaltime);
        mEditIntervalTime.setText(Integer.toString(Preferences.getIntervalTime(this)));


        String strmEditIntervalTime = mEditIntervalTime.getText().toString();
        Log.e("ttttttttttttt", mEditIntervalTime.getText().toString());

        jian = Integer.parseInt(strmEditIntervalTime);
        Log.e("ttttttttttttt", "jian" + jian);
        //总秒数
        zms = jian * (21000 - 1);

        //yxcl2=jian*21000/(60*60*24);
        yxcl1 = jian * (21000 - 1) / 3600;
        yxcl3 = jian * (21000 - 1) % (60 * 60) / 60;
        yxcl2 = jian * (21000 - 1) % (60 * 60) % 60;
        Log.e("ttttttttttttt", "yxcl" + yxcl1);
        if (yxcl3 < 10) {
            fen.setText("0" + String.valueOf(yxcl3));
            xiaoshi.setText(String.valueOf(yxcl1));
            tian.setText(String.valueOf(yxcl2));
        } else {

            fen.setText(String.valueOf(yxcl3));
            tian.setText(String.valueOf(yxcl2));
            xiaoshi.setText(String.valueOf(yxcl1));
        }

        mEditIntervalTime.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged:" + s + "-" + start + "-" + count + "-" + after);

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged:" + s + "-" + "-" + start + "-" + before + "-" + count);
                try {
                    String strmEditIntervalTime = mEditIntervalTime.getText().toString();
                    Log.e("ttttttttttttt", mEditIntervalTime.getText().toString());

                    jian = Integer.parseInt(strmEditIntervalTime);
                    Log.e("ttttttttttttt", "jian" + jian);
                    if (strmEditIntervalTime == null) {
                        jian = 0;

                    }


                    yxcl1 = jian * (21000 - 1) / 3600;
                    s = String.valueOf(yxcl1);
                    xiaoshi.setText(s);
                    if (yxcl1 < 10) {
                        tian.setText("0" + s);
                    } else {
                        tian.setText(s);
                    }
                    yxcl3 = jian * (21000 - 1) % (60 * 60) / 60;
                    s = String.valueOf(yxcl3);
                    if (yxcl3 < 10) {
                        fen.setText("0" + s);
                    } else {
                        fen.setText(s);
                    }
                    yxcl2 = jian * (21000 - 1) % (60 * 60) % 60;
                    s = String.valueOf(yxcl2);
                    if (yxcl2 < 10) {
                        tian.setText("0" + s);
                    } else {
                        tian.setText(s);
                    }
                    Log.e("ttttttttttttt", "yxcl" + yxcl1);
                    Log.e("ttttttttttttt", "yxcl" + yxcl1);

                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

        });

//		mEditDeepNum.addTextChangedListener(new TextWatcherMaxInt(
//				Set_Start.this, mEditDeepNum, 4, -1));
//		mEditFace.addTextChangedListener(new TextWatcherMaxInt(Set_Start.this,
//				mEditFace, 6, -1));
//		mEditDrilling.addTextChangedListener(new TextWatcherMaxInt(
//				Set_Start.this, mEditDrilling, 2, -1));

        mDelayTextWatcher = new TextWatcherMaxInt(Set_Start.this, mEditDelayTime, -1, 3600);
        mEditDelayTime.addTextChangedListener(mDelayTextWatcher);
        mEditIntervalTime.addTextChangedListener(new TextWatcherMaxInt(Set_Start.this, mEditIntervalTime, -1, 3600));

        // 本煤层孔RadioGroup

        ((RadioGroup) this.findViewById(R.id.BMCYCai)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.BMceng) {
                    isben = true;
                    mTextDrilling.setVisibility(View.GONE);
                    mEditDrilling.setVisibility(View.GONE);
                    bory = "P";
                } else if (checkedId == R.id.YCai) {
                    isben = false;
                    mTextDrilling.setVisibility(View.VISIBLE);
                    mEditDrilling.setVisibility(View.VISIBLE);
                    bory = "S";
                }

            }
        });
        ((RadioGroup) this.findViewById(R.id.FZCYCai)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.FZshui) {
                    isfang = true;
                    bory1 = "F";
                } else if (checkedId == R.id.YCCai) {
                    isfang = false;
                    bory1 = "W";
                }

            }
        });

        ((RadioGroup) this.findViewById(R.id.rg_minute_hour)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_minute) {
                    mEditDelayTime.setText("");
                    mEditDelayTime.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    mDelayTextWatcher.setMaxParams(-1, 3600);
                } else {
                    mEditDelayTime.setText("");
                    mEditDelayTime.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                    mDelayTextWatcher.setMaxParams(-1, 60);
                }
            }
        });
        mEditFace.addTextChangedListener(new TextWatcher() {
            String tmp = "";
            String digits = "/---_\"\n\t";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditFace.setSelection(s.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tmp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.equals(tmp)) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (digits.indexOf(str.charAt(i)) < 0) {
                        sb.append(str.charAt(i));
                    }
                }
                tmp = sb.toString();
                mEditFace.setText(tmp);
            }
        });
        mEditDrilling.addTextChangedListener(new TextWatcher() {
            String tmp = "";
            String digits = "/---_\"\n\t";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditDrilling.setSelection(s.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tmp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.equals(tmp)) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (digits.indexOf(str.charAt(i)) < 0) {
                        sb.append(str.charAt(i));
                    }
                }
                tmp = sb.toString();
                mEditDrilling.setText(tmp);
            }
        });
        mEditDeepNum.addTextChangedListener(new TextWatcher() {
            String tmp = "";
            String digits = "/---_\"\n\t";

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditDeepNum.setSelection(s.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tmp = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.equals(tmp)) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (digits.indexOf(str.charAt(i)) < 0) {
                        sb.append(str.charAt(i));
                    }
                }
                tmp = sb.toString();
                mEditDeepNum.setText(tmp);
            }
        });

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
        SPconfig.putValue("qinjiao", editText3.getText().toString().trim());
        SPconfig.putValue("fangwei", editText4.getText().toString().trim());
        SPconfig.putValue("kongshen", editText5.getText().toString().trim());
        SPconfig.putValue("shang", editText6.getText().toString().trim());
        SPconfig.putValue("zuo", editText7.getText().toString().trim());
        SPconfig.putValue("zong", editText8.getText().toString().trim());

String fileName;
        if (isfang) {//  防治水F  瓦斯W
            if (isben) { // 平行空P     扇形孔S
                fileName = "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() ;
            }else{
                fileName = "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() ;
            }
        }else{
            if (isben) { // 平行空P     扇形孔S
                fileName = "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() ;
            }else{
                fileName = "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() ;
            }
        }

        DbManager.DaoConfig daoConfig = new DaoConfig()
                .setDbName("SetValueBean") // 数据库的名字
                // 保存到指定路径 .setDbDir(newFile(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .setDbVersion(1)// 数据库的版本号
                .setDbUpgradeListener(new DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager arg0, int arg1, int arg2) {
                        LogUtil.e("数据库版本更新了！，次版本不联网发布不涉及数据库更新问题。");  // 数据库版本更新监听
                    }
                });
        DbManager manager = x.getDb(daoConfig);
        try {
            SetValueBean info = new SetValueBean();
            info.setQinjiao(Double.parseDouble(editText3.getText().toString().trim()));
            info.setFangwei(Double.parseDouble( editText4.getText().toString().trim()));
            info.setKongshen(Double.parseDouble( editText5.getText().toString().trim()));
            info.setShang(Double.parseDouble( editText6.getText().toString().trim()));
            info.setZuo(Double.parseDouble(editText7.getText().toString().trim()));
            info.setZong(Double.parseDouble( editText8.getText().toString().trim()));
            info.setFileName(fileName);
            manager.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
        }


    }

    /**
     * 检测输入值是否合法
     *
     * @return
     */
    private boolean checkInputVal() {

        if (mEditDeepNum.getText().toString().equals("")) {
            Utils.toast(this, "请输入孔号！");
            return false;
        }

        if (mEditDrilling.getText().toString().equals("")) {
            Utils.toast(this, "请输入钻场！");

            return false;
        }
        if (mEditFace.getText().toString().equals("")) {
            Utils.toast(this, "请输入工作面！");
            return false;
        }
        if (mEditDelayTime.getText().toString().equals("")) {
            Utils.toast(this, "请输入延时时间！");
            return false;
        }

        if (mEditIntervalTime.getText().toString().equals("")) {
            Utils.toast(this, "请输入间隔时间！");
            return false;
        }
        if (ET_jiaozhun.getText().toString().equals("")) {
            Utils.toast(this, "请输入孔深校准间隔！");
            return false;
        }

        if (Integer.parseInt(ET_jiaozhun.getText().toString()) < 1) {
            Utils.toast(this, "请输入孔深强制校准间隔不得小于1");
            return false;
        }
        if (Integer.parseInt(ET_jiaozhun.getText().toString()) > 21000) {
            Utils.toast(this, "请输入孔深强制校准间隔不得大于21000");
            return false;
        }
        mnDelayTime = (char) (Integer.parseInt(mEditDelayTime.getText().toString()));
        if (mnDelayTime < 1) {
            Utils.toast(this, "请输入延时时间不得小于1");
            return false;
        }

        if (((RadioButton) findViewById(R.id.rb_hour)).isChecked()) {
            mnDelayTime = (char) ((int) mnDelayTime * 60);
        }

        mnIntervalTime = (char) (Integer.parseInt(mEditIntervalTime.getText().toString()));
        if (mnIntervalTime < 2) {
            Utils.toast(this, "请输入间隔时间不得小于2");
            return false;
        }
        return true;
    }

    /**
     * 复位命令控制参数
     */
    private void resetCtrlParam() {
        mReSendTime = ConstantDef.MAX_RESEND_TIME;
        mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set_start: {

                if (!checkInputVal()) return;
                if (!set_Start()) return;

                String strFace = mEditFace.getText().toString();
                String strDeepNum = mEditDeepNum.getText().toString();
                String strDrilling = mEditDrilling.getText().toString();
                if (isfang == true) {
                    if (isben == true) {

                        String strname = "";
                        int i = 0;
                        if (isben) {
                            strname = "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();

                        } else {
                            strname = "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        }
                        for (i = 0; i < file.length; i++) {

                            if (file.length > 0 && strname.equals(file[i].getName())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Set_Start.this);
                                builder.setCancelable(false); // 不响应back按钮
                                builder.setMessage("本钻孔测量文件" + "\n" + strname + "\n" + "已存在，是否覆盖？"); // 对话框显示内容
                                // 设置按钮
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        setStart11(false,"P","F");

                                    }
                                });
                                builder.setNeutralButton("另存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 创建保存数据路径
                                        if (isfang == true) {
                                            if (isben == true) {
                                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-"

                                                        + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";

                                                File fpath = new File(FILE_PATH);
                                                if (!fpath.exists()) {
                                                    fpath.mkdirs();

                                                } else {
                                                    WenJianm = WenJianM.getWenJianm();
                                                    WenJianm++;
                                                    Log.e("mmmmmmmmmmmmmm", Integer.toString(WenJianm));
                                                    WenJianM.setWenJianm(WenJianm);
                                                }
                                            } else {
                                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                                                File fpath = new File(FILE_PATH);

                                                if (!fpath.exists()) {
                                                    fpath.mkdirs();
                                                } else {
                                                    WenJianm = WenJianM.getWenJianm();
                                                    WenJianm++;
                                                    WenJianM.setWenJianm(WenJianm);
                                                }
                                            }
                                        } else {

                                            if (isben == true) {

                                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-"

                                                        + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";

                                                File fpath = new File(FILE_PATH);
                                                if (!fpath.exists()) {
                                                    fpath.mkdirs();

                                                } else {
                                                    WenJianm = WenJianM.getWenJianm();
                                                    WenJianm++;
                                                    WenJianM.setWenJianm(WenJianm);
                                                }

                                            } else {
                                                FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                                                File fpath = new File(FILE_PATH);

                                                if (!fpath.exists()) {
                                                    fpath.mkdirs();
                                                } else {
                                                    WenJianm = WenJianM.getWenJianm();
                                                    WenJianm++;
                                                    WenJianM.setWenJianm(WenJianm);
                                                }
                                            }
                                        }


                                        setStart11(false,"P","F");
                                    }

                                });

                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //  Toast.makeText(Set_Start.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();

                                break;
                            }
                        }
                        System.out.println(i);
                        System.out.println(file.length);
                        if (i > file.length - 1) {

                            setStart11(false,"P","F");

                        }


                    } else if (isben == false) {

                        String strname = "";
                        int i = 0;
                        if (isben) {
                            strname = "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        } else {
                            strname = "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        }
                        for (i = 0; i < file.length; i++) {

                            if (file.length > 0 && strname.equals(file[i].getName())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Set_Start.this);
                                // builder.setTitle("普通对话框"); //标题
                                // .setIcon(R.drawable.ic_launcher) //icon
                                builder.setCancelable(false); // 不响应back按钮
                                builder.setMessage("本钻孔测量文件" + "\n" + strname + "\n" + "已存在，是否覆盖？"); // 对话框显示内容
                                // 设置按钮
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();

                                        setStart11(true,"S","F");
                                    }
                                });
                                builder.setNeutralButton("另存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 创建保存数据路径
                                        if (isben == true) {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";

                                            File fpath = new File(FILE_PATH);
                                            if (!fpath.exists()) {
                                                fpath.mkdirs();
                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);


                                            }
                                        } else {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "F" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                                            File fpath = new File(FILE_PATH);

                                            if (!fpath.exists()) {
                                                fpath.mkdirs();

                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);

                                            }
                                            Utils.toast(Set_Start.this, "生成新的文件夹是" + FILE_PATH);
                                        }
                                        setStart11(true,"S","F");

                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();

                                break;
                            }
                        }
                        if (i > file.length - 1) {

                            setStart11(true,"S","F");

                        }


                    }
                } else {
                    ///////////////////////////////////////////////////////////////////////////////////

                    if (isben == true) {

                        String strname = "";
                        int i = 0;
                        if (isben) {
                            strname = "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();

                        } else {
                            strname = "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        }
                        for (i = 0; i < file.length; i++) {

                            if (file.length > 0 && strname.equals(file[i].getName())) {
                                // Toast.makeText(Set_Start.this, "孔号已存在，请重新输入！！",
                                // 0);
                                // 创建builder
                                AlertDialog.Builder builder = new AlertDialog.Builder(Set_Start.this);
                                // builder.setTitle("普通对话框"); //标题
                                // .setIcon(R.drawable.ic_launcher) //icon
                                builder.setCancelable(false); // 不响应back按钮
                                builder.setMessage("本钻孔测量文件" + "\n" + strname + "\n" + "已存在，是否覆盖？"); // 对话框显示内容
                                // 设置按钮
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();
                                setStart11(false,"P","W");
                                    }
                                });
                                builder.setNeutralButton("另存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 创建保存数据路径
                                        if (isben == true) {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-"

                                                    + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";

                                            File fpath = new File(FILE_PATH);
                                            if (!fpath.exists()) {
                                                fpath.mkdirs();

                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);
                                            }
                                        } else {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                                            File fpath = new File(FILE_PATH);

                                            if (!fpath.exists()) {
                                                fpath.mkdirs();
                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);
                                            }
                                        }
                                        setStart11(false,"P","W");
                                    }

                                });

                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();

                                break;
                            }
                        }
                        System.out.println(i);
                        System.out.println(file.length);
                        if (i > file.length - 1) {

                            setStart11(false,"P","W");

                        }


                    } else if (isben == false) {

                        String strname = "";
                        int i = 0;
                        if (isben) {
                            strname = "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        } else {
                            strname = "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm();
                        }
                        for (i = 0; i < file.length; i++) {

                            if (file.length > 0 && strname.equals(file[i].getName())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Set_Start.this);
                                // builder.setTitle("普通对话框"); //标题
                                // .setIcon(R.drawable.ic_launcher) //icon
                                builder.setCancelable(false); // 不响应back按钮
                                builder.setMessage("本钻孔测量文件" + "\n" + strname + "\n" + "已存在，是否覆盖？"); // 对话框显示内容
                                // 设置按钮
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了确定按钮", Toast.LENGTH_SHORT).show();

                                        setStart11(true,"S","W");
                                    }
                                });
                                builder.setNeutralButton("另存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 创建保存数据路径
                                        if (isben == true) {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "P" + "-" + mEditFace.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";

                                            File fpath = new File(FILE_PATH);
                                            if (!fpath.exists()) {
                                                fpath.mkdirs();
                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);


                                            }
                                        } else {
                                            FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + "W" + "-" + "S" + "-" + mEditFace.getText().toString() + "-" + mEditDrilling.getText().toString() + "-" + mEditDeepNum.getText().toString() + "-" + Utils.getCurDateString() + "-" + WenJianM.getWenJianm() + "/";
                                            File fpath = new File(FILE_PATH);

                                            if (!fpath.exists()) {
                                                fpath.mkdirs();

                                            } else {
                                                WenJianm = WenJianM.getWenJianm();
                                                WenJianm++;
                                                WenJianM.setWenJianm(WenJianm);

                                            }
                                            Utils.toast(Set_Start.this, "生成新的文件夹是" + FILE_PATH);
                                        }
                                        setStart11(true,"S","W");

                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(Set_Start.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                                break;
                            }
                        }
                        if (i > file.length - 1) {
                            setStart11(true,"S","W");
                        }
                    }
                }


                break;
            }
            case R.id.btn_back_menu: {
                finish();
                break;
            }
            default:
                break;
        }
    }
    private void setStart11(boolean flag,String ben,String fang){
        // 防止重复点击
        findViewById(R.id.btn_set_start).setEnabled(false);
        // 保存孔号字符串
        Preferences.saveHoleIDString(Set_Start.this, mEditDeepNum.getText().toString());
        // 保存工作面字符串
        Preferences.saveFaceString(Set_Start.this, mEditFace.getText().toString());
        //zgl 2015年1月4日12:04:41 保存校准间隔
        Preferences.saveJiaoZhun(this, Integer.parseInt(ET_jiaozhun.getText().toString()));
        if (flag){
            // 保存钻场字符串
            Preferences.saveDrillingString(Set_Start.this, mEditDrilling.getText().toString());
        }else{
            Preferences.saveDrillingString(Set_Start.this, "00");
        }

        Preferences.saveSumPoint(Set_Start.this, 0);
        // 保存b or y
        Preferences.saveBenString(Set_Start.this, ben);
        Preferences.saveFangString(Set_Start.this, fang);
        // 保存时间
        Preferences.saveTime(Set_Start.this, Utils.getCurDateString());
        //保存重名
        Preferences.saveChongMString(Set_Start.this, Integer.toString(WenJianM.getWenJianm()));
        // 发送延时及间隔时间
        BluetoothTools.sendData(Set_Start.this, TransProtocol.CMD_Send_Delay_Interval.getSendCmd(mnDelayTime, mnIntervalTime), TransProtocol.CMD_TYPE_SEND_DELAY_INTERVAL);
        resetCtrlParam();
        // 开始计时
        startTimer();
    }

    private boolean set_Start(){

        if (!editBuilderS(editText6,"请填上下偏差！","请重新输入上下偏差应在0-100m。"))return false;
        if (!editBuilderS(editText7,"请填左右偏差！","请重新输入左右偏差应在0-100m。"))return false;
        if (!editBuilderS(editText8,"请填终孔偏差！","请重新输入终孔偏差应在0-100m。"))return false;


        if (editText3.getText() == null || editText3.getText().toString().trim().length() == 0) {
            editBuilder("请填设计倾角！");
            return false;
        } else {
            double jiangetemp = Double.parseDouble(editText3.getText().toString().trim());
            if (jiangetemp > 90 || jiangetemp < -90) {
                editBuilder("设计倾角应在-90-90°范围内！");
                return false;
            }
        }
        if (editText4.getText() == null || editText4.getText().toString().trim().length() == 0) {
            editBuilder("请填设计方位角！");
            return false;
        } else {
            double jiangetemp = Double.parseDouble(editText4.getText().toString().trim());
            if (jiangetemp >= 360 || jiangetemp < 0) {
                editBuilder("设计方位角应在0-360°范围内！");
                return false;
            }
        }
        if (editText5.getText() == null || editText5.getText().toString().trim().length() == 0) {
            editBuilder("请填设计孔深！");
            return false;
        } else {
            double jiangetemp = Double.parseDouble(editText5.getText().toString().trim());
            if (jiangetemp > 2000 || jiangetemp < 1) {
                editBuilder("设计孔深应在1-2000m范围内！");
                return false;
            }
        }
        saveData();
        return true;
    }
    private boolean editBuilderS(EditText edit,String str,String str1){
        if (edit.getText() == null || edit.getText().toString().length() == 0) {
            editBuilder(str);
            return false;
        } else if (0 >= Double.parseDouble(edit.getText().toString().trim()) || Double.parseDouble(edit.getText().toString().trim())> 100) {
            editBuilder(str1);
            return false;
        }
        return  true;
    }
    private void editBuilder(String str) {
        new AlertDialog.Builder(Set_Start.this).setTitle("提示").setMessage(str).setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setCancelable(false).show();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bluetoothTransReceiver);
        super.onDestroy();
    }
}
