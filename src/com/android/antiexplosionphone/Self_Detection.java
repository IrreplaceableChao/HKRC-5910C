package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.bluetooth.BluetoothTools;
import com.android.utils.ConstantDef;
import com.android.utils.TransProtocol;
import com.android.utils.TransProtocol.ANS_Rev_SelfChk;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 自检界面
 *
 * @author TY
 */
public class Self_Detection extends Activity {

    private static final String TAG = Self_Detection.class.getSimpleName();

    private int mReSendTime = ConstantDef.MAX_RESEND_TIME;
    private int mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    private int mTotalTime = ConstantDef.MAX_CHK_TIME;
    private TextView tv_time;
    private TextView mtvAngle;
    private TextView mtvTempra;
    private TextView mtvChkSum;
    private TextView mtvVoltage;
    private TextView mtvGravity;
    private Button bn_QD;
    private ProgressBar mProgressBar;
    private int mProgress = 0;
    private int mnRightCnt = 0;
    private long time;
    private boolean flag = true;
    private String timestr;
    private Handler mHandler = new Handler();
    private int i = 0;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            tv_time.setText(timestr);
        }

        ;
    };
    private BroadcastReceiver bluetoothTransReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothTools.ACTION_READ_DATA.equals(action)) {

                // 停止命令应答
                if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_STOP) {
                    // 停止命令应答，分析应答数据
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_STOP)) {
                        // Utils.toast(Self_Detection.this, "自检停止命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 发送自检指令
                    BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_SELFCHK, TransProtocol.CMD_TYPE_SELFCHK);
                    resetCtrlParam();
                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SELFCHK) {
                    // 自检命令应答,分析应答数据
                    ANS_Rev_SelfChk.ANS_SELFCHECK ans = TransProtocol.ANS_Rev_SelfChk.getAns(BluetoothTools.readData(intent));
                    showChkAnsData(ans);
                    System.out.println("ans.checksum = " + ans.checksum + " ans.voltage = " + ans.voltage);
                    if ((0.99f < ans.checksum) && (ans.checksum < 1.01f)) {
                        if ((ans.voltage >= ConstantDef.STANDARD_VOLTAGE)) {//6217000010029682518
                            // 合格的数据
                            if (++mnRightCnt == 7200) {
                                // 校验通过
                                // // 发送STOP指令
                                BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                                resetCtrlParam();
                                return;
                            }

                        } else {
                            mnRightCnt = 0;
                            reCheckDialog();
                            BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                            resetCtrlParam();
                            return;
                        }
                    } else {
                        i++;
                    }
//					Toast.makeText(getApplicationContext(), i+"", 0).show();
                    // 更新进度
                    mProgress += 10;
                    if (mProgress == 100) {


                        if (i > 6) {
                            //弹框失败
                            reCheckDialog();
                            BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                            resetCtrlParam();
                            i = 0;
                            return;
                        } else {
                            bn_QD.setVisibility(Button.VISIBLE);
                        }

                        // return;
                    }
                    mProgressBar.setProgress(mProgress);
                    // 发送自检指令
                    BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_SELFCHK, TransProtocol.CMD_TYPE_SELFCHK);
                    resetCtrlParam();
                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_END) {
                    // 停止命令应答，分析应答数据
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_STOP)) {
                        // Utils.toast(Self_Detection.this, "自检停止命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 自检结束更新进度
                    TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                }
            }
        }

    };

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            Log.v(TAG, "mReSendCnt = " + mReSendCnt + " mReSendTime = " + mReSendTime + " mTotalTime = " + mTotalTime);
            if ((TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_STOP) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_SELFCHK) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_END)) {

                if ((--mTotalTime > 0) && (mReSendCnt > 0)) {
                    if (--mReSendTime < 1) {
                        mReSendCnt--;
                        mReSendTime = ConstantDef.MAX_RESEND_TIME;
                    }
                } else {
//					Utils.toast(Self_Detection.this, "自检模块 通讯失败！");
                    stopTimer();
                    TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
//					// 询问是否重新自检
//				reCheckDialog();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Self_Detection.this);
                    builder.setTitle("自检模块 通讯失败，请返回主页重新连接！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("返回主页", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Self_Detection.this.finish();
                        }
                    }).setCancelable(false).show();
                    return;
                }
                mHandler.postDelayed(this, 1000);
            } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_NONE) {
                mProgress += 5;
                if (mProgress > 100) mProgress = 100;
                mProgressBar.setProgress(mProgress);

                //Toast.makeText(getApplicationContext(), i+"", 0).show();

                mHandler.postDelayed(this, 200);
            }

        }
    };

    private void startTimer() {
        mHandler.postDelayed(runnable, 1000);
    }

    private void stopTimer() {
        mHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_detection);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_READ_DATA);
        registerReceiver(bluetoothTransReceiver, intentFilter);

        init();
        tv_time = (TextView) findViewById(R.id.tv_time);
        //time = 86400 * 5 + 52222;
        new Thread() {
            public void run() {
                while (flag) {
                    StringBuffer sb = new StringBuffer();
                    time = time + 1;
                    long second = time;
                    if (second < 10) {
                        sb.append(0);
                    }
                    sb.append(second);
                    sb.append("秒");
                    timestr = sb.toString();
                    handler.sendEmptyMessage(0x1);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                flag = true;
            }

            ;
        }.start();
        bn_QD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 校验通过
                // // 发送STOP指令
                BluetoothTools.sendData(Self_Detection.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                resetCtrlParam();
                stopTimer();
                //Self_Detection.this.finish();
                Intent intent = new Intent();
                intent.setClass(Self_Detection.this, Set_Start.class);
                Self_Detection.this.startActivity(intent);
                Self_Detection.this.finish();
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 相关初始化
     */
    private void init() {
        bn_QD = (Button) findViewById(R.id.bn_tg);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_self_detect);
        mtvAngle = (TextView) findViewById(R.id.tv_angle);
        mtvGravity = (TextView) findViewById(R.id.tv_gravity);
        mtvTempra = (TextView) findViewById(R.id.tv_temperature);
        mtvChkSum = (TextView) findViewById(R.id.tv_checksum);
        mtvVoltage = (TextView) findViewById(R.id.tv_voltage);


        mReSendTime = ConstantDef.MAX_RESEND_TIME;
        mReSendCnt = ConstantDef.MAX_RESEND_CNT;
        mTotalTime = ConstantDef.MAX_CHK_TIME;
        mProgress = 0;
        mnRightCnt = 0;
        mProgressBar.setProgress(mProgress);

        // 发送STOP指令
        BluetoothTools.sendData(this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_STOP);
        resetCtrlParam();
        // 开始计时
        startTimer();
    }

    /*
     * 复位命令控制参数
     */
    private void resetCtrlParam() {
        mReSendTime = ConstantDef.MAX_RESEND_TIME;
        mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    }

    /**
     * 显示自检应答数据
     *
     * @param ans --自检应答结构
     */
    private void showChkAnsData(ANS_Rev_SelfChk.ANS_SELFCHECK ans) {


        DecimalFormat df1 = new DecimalFormat("0.00");
        String angle = df1.format(ans.angle) + "°";
        DecimalFormat df3 = new DecimalFormat("0");
        String tempera = df3.format(ans.tempera) + "℃";
        String gravity = df1.format(ans.position) + "°";
        DecimalFormat df2 = new DecimalFormat("0.000");
        String checksum = df2.format(ans.checksum);
        String voltage = df2.format(ans.voltage) + "V";

        if ((0.99f >= ans.checksum) || (ans.checksum >= 1.01f)) {
            mtvChkSum.setTextColor(Color.RED);
        } else {
            mtvChkSum.setTextColor(Color.BLACK);
        }
        if (ans.voltage < ConstantDef.STANDARD_VOLTAGE) {
            mtvVoltage.setTextColor(Color.RED);
        } else {
            mtvVoltage.setTextColor(Color.BLACK);
        }
        mtvAngle.setText(angle);
        mtvTempra.setText(tempera);
        mtvGravity.setText(gravity);
        mtvChkSum.setText(checksum);
        mtvVoltage.setText(voltage);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // switch (keyCode) {
        // case KeyEvent.KEYCODE_BACK:
        // return true;
        // case KeyEvent.KEYCODE_HOME:
        // return true;
        // default:
        // break;
        // }
        return super.onKeyDown(keyCode, event);
    }

    // @Override
    // public void onAttachedToWindow() {
    // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    // super.onAttachedToWindow();
    // }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        stopTimer();
        unregisterReceiver(bluetoothTransReceiver);
        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
        super.onDestroy();
    }

    /**
     * 自检失败，是否再次自检
     */
    private void reCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Self_Detection.this);
        builder.setTitle("自检不合格，是否重新自检？").setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 重新自检
                time = 0;
                init();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击取消退回主界面
                Self_Detection.this.finish();
            }
        }).show();
    }

}
