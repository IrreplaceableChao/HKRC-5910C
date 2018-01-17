package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.InputFilter;
import android.text.format.Time;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.android.antiexplosionphone.R.string;
import com.android.bean.FileNameBean;
import com.android.utils.ConstantDef;
import com.android.utils.FileUtils;
import com.android.utils.Preferences;
import com.android.utils.TextWatcherMaxFloat;
import com.android.utils.TimerTextView;
import com.android.utils.TransProtocol;
import com.android.utils.TransProtocol.Efficient_Point;
import com.android.utils.TransProtocol.Efficient_Point.Struct_Ensure_Point;
import com.android.utils.Utils;
import com.android.utils.ValidPointInfo;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 确定有效点界面
 *
 * @author TY
 */
public class Point_Collect extends Activity implements android.view.GestureDetector.OnGestureListener {
    private static final int MSG_BACK_SLIDER_IMG = 0;
    private GestureDetector detector;
    private long RemainTime;// 倒计时时间
    private int sumPoint;
    private String fangshi;
    private boolean isqi;
    private boolean isgongshi = false;
    private boolean ist = false;
    DecimalFormat df6 = new DecimalFormat("##0.0");
    Struct_Ensure_Point[] mStructEnsurePoint;
    public static ArrayList<String> listcolor = new ArrayList<String>();
    ;
    private ViewFlipper flipper; // 滑动翻页
    private RadioGroup pointtab; // 顶部分页标签
    private int mnTabID = 0; // 当前标签ID:0,1,2对应三个标签
    private EditText mEditStartDeep;
    private Boolean isshou = false;// 手动标记、
    private Boolean isfangan = false;//取消标记
    private EditText mEditMeasureInterval;
    private EditText mEditValidDeep;
    private String strEditValidDeep;
    private StringBuffer sb = new StringBuffer();
    private String ColorStr = "0";
    public static boolean isJie = false;
    private long ii;
    private Button shanchu;
    private Button Bun_shanchu;
    private TextView tv_JG;
    private TextView tv_JZ;
    private EditText et_JZ;
    private EditText Bet_JZ;
    private Boolean isjiaozhun = false;
    private ProgressBar mProgressBar; // 剩余时间进度
    private WakeLock mWakeLock;
    private TextView mTvRemainTime; // 剩余时间提示
    private RadioGroup MYSCeng;
    private RadioGroup BMCYCai;
    private RadioButton YSceng;// 岩石层
    private RadioButton Mceng;// 煤层
    private RadioButton QZuan;// 起钻
    private RadioButton ZJin;// 钻进
    private boolean isJzhun;
    private boolean isJing;
    public static List<Integer> list = new ArrayList<Integer>();
    private Button bt_Jzhun;
    private String type = "";// 类型标示
    private String sign = "0";
    private Boolean isRadio = false;// 点击煤岩的表示位
    private boolean isok = false;
    private boolean isButton = false;
    private boolean isDialog = false;
    private boolean issjjd = false;
    private Editor editor;
    private final String REMAIN_COLLECT_TIME = "剩余采集时间:";
    private final String REMAIN_DELAY_TIME = "剩余延时时间:";
    private DecimalFormat df1 = new DecimalFormat("000");
    private DecimalFormat df2 = new DecimalFormat("#.00");
    private String strname;
    private ListView mLvPoint;
    private ArrayList<ValidPointInfo> mPointList = new ArrayList<ValidPointInfo>();
    private ValidPointAdapter mPointAdapter;
    private Boolean istime = false;
    private WifiManager wifiManager = null;
    private ListView mLvPrePoint;
    private FileNameBean fileNameBean = new FileNameBean();
    private ArrayList<ValidPointInfo> mPrePointList = new ArrayList<ValidPointInfo>();
    // private ArrayList<String> mPrePointList1 = new ArrayList<String>();
    private ValidPointAdapter mPrePointAdapter;
    // private TextView xiaoshi;
    // private TextView tian;
    // private TextView fen;
    private SharedPreferences sp;
    private boolean mbDeepmanual = true;// 是否手动计算孔深
    private boolean mbToUp; // 是否为起钻
    private int mnPointSerNum = 0; // 采集点序号
    private int mnPointNextID = 1; // 下一个对应采集点编号
    private int mnPointNextID1 = 1; // 下一个对应采集点编号
    private float mnLastDeep; // 初始值为起测点孔深,记录上一个有效点孔深
    private long mnDelay = 0; // 剩余延时时间s
    private long T1 = 0; // 启动时间ms
    private long T2 = 0; // 延时时间ms
    private long T3 = 0; // 间隔时间ms
    private int t1 = 3000;//钻具稳定时间
    private int t3 = 3000;//表示手机与探管之间的最大误差，定为3秒。
    private int K1 = ConstantDef.MAX_POINT_COUNT; // 机芯最大采集点数
    private int mCurStatus = ConstantDef.STATUS_INVALID;// 当前状态
    private long mnNextMillistime = 0; // 下一个测点采集时间
    private long mnNextMillistime1 = 0; // 下一个测点采集时间
    private long mnRemainTime = 0; // 剩余采集时间s
    private long mnLastMillistime = 0; // 记录末次记录有效点的时间
    private String mFilePath; // 确定有效点保存文件路径
    private long[] djstime = new long[4];
    private List<Integer> list_bhs = new ArrayList<Integer>();
    // zgl
    private TimerTextView timerTextView;
    private String scond;
    private int returnlevel = 0; // 当前系统电量值
    private int powervalue = 20;
    private boolean isone = false, istwo = false;
    private final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView tv_time;
    private long time;
    private long time1;
    private boolean flag = true;
    private String timestr;
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            timerTextView.setTimes(djstime);
            if (!timerTextView.isRun()) {
                timerTextView.beginRun();
            }
        }

        ;
    };

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            // 更新显示当前时间
            Time time = Utils.getCurTime();
            long T = time.toMillis(false);
            ((TextView) findViewById(R.id.tv_cur_time)).setText(time.format("%H:%M:%S"));

            // zlg 更新电量

            // Log.e("mylog", "level= " + returnlevel + "%");

            if (!isone && returnlevel < 30 && returnlevel >= 10) {
                isone = true;
                 jiexuDialog("电量过低请进行接续操作！");
                Log.e("", "电量过低请进行接续操作！");

//				Toast.makeText(Point_Collect.this, "电量过低请进行接续操作！", 0).show();
            }
            if (!istwo && returnlevel < 10 && returnlevel >= 5) {
                istwo = true;
                 jiexuDialog("控制器即将关机,请及时进行接续操作！");
                Log.e("", "控制器即将关机,请及时进行接续操作！");
            } else if (returnlevel < 10) {
                // jiexuDialog("");
            }

            if (mCurStatus == ConstantDef.STATUS_DELAY) {
                // 延时状态
                mnDelay = (T2 - (T - T1)) / 1000;
                System.out.println("mnDelay = " + mnDelay);
                if (mnDelay <= 0) {// 延时结束
                    mCurStatus = ConstantDef.STATUS_ENSURE_POINT;

                    ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(true);

                    ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_ENSURE_POINT);

                    // 更新下一测点采集时间
                    mnNextMillistime = T + T3;
                    // 显示倒计时时间
                    long remainTime = (mnNextMillistime - T) / 1000;
                    RemainTime = remainTime;
                    if (mnNextMillistime1 > 0) {
                        mnNextMillistime1--;
                    }
                    ((TextView) findViewById(R.id.tv_next_time)).setText("" + mnNextMillistime1);
                    // 机芯下一测点编号
                    mnPointNextID++;
                    // 设定剩余采集时间进度
                    mTvRemainTime.setText(REMAIN_COLLECT_TIME);
                    mProgressBar.setMax((int) mnRemainTime);
                    mProgressBar.setProgress((int) mnRemainTime);
                    // zgl
                    Preferences.saveStep(Point_Collect.this, ConstantDef.STATUS_ENSURE_POINT);
                } else {
                    mProgressBar.setProgress((int) mnDelay);
                }
            } else if (mCurStatus == ConstantDef.STATUS_ENSURE_POINT) {
                // 更新剩余采集时间进度条
                System.out.println("mnRemainTime = " + mnRemainTime);
                if (--mnRemainTime >= 0) {
                    mProgressBar.setProgress((int) mnRemainTime);
                }
                if (T > (T1 + T2 + T3 * (K1 - 1))) {
                    // 机芯采集满
                    mCurStatus = ConstantDef.STATUS_POINT_FULL;
                    Preferences.saveStep(Point_Collect.this, ConstantDef.STATUS_POINT_FULL);
                    ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(false);
                    ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_POINT_FULL);
                    Utils.toast(Point_Collect.this, "探管采集点已满！");
                    System.out.println("机芯采集已满！");
                }
                // 更新下一测点采集时间
                if (mnNextMillistime <= T) {
                    mnNextMillistime += T3;

                    long remainTime = (mnNextMillistime - T) / 1000;
                    // long remainTime = DTime.Djs(mnNextMillistime);
                    RemainTime = remainTime;
                    Preferences.saveNextMillistime(Point_Collect.this, mnNextMillistime);// zgl
                    if (mnNextMillistime1 > 0) {
                        mnNextMillistime1--;
                    }
                    ((TextView) findViewById(R.id.tv_next_time)).setText("" + mnNextMillistime1);
                    ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                    tv_JG.setVisibility(View.GONE);
                    mnPointNextID++;
                    // zgl 数据量测试用完后删除
                    // 更新显示有效点
                    /*
					 * mPointList.add(new ValidPointInfo(
					 * Integer.toString(mnPointSerNum), mEditStartDeep.getText()
					 * .toString(),
					 * Utils.millisToTimeStrColon(mnLastMillistime),
					 * Integer.toString(mnPointNextID),
					 * Utils.getCurDateString(), mnLastMillistime, type));
					 * mPointAdapter .notifyDataSetChanged(); byte[] point =
					 * TransProtocol.Efficient_Point.getFileData( mnPointNextID,
					 * mnLastDeep, Utils.getCurDateString(),
					 * Utils.millisToTimeStr(mnLastMillistime), type);
					 * FileUtils.appendWriteFile( mFilePath, point);
					 * Preferences.saveSumPoint(Point_Collect.this,
					 * mnPointSerNum); mnPointSerNum++;
					 */
                    // zgl 数据量测试用完后删除
                } else {
                    // 显示倒计时时间
                    long remainTime = (mnNextMillistime - T) / 1000;
                    // long remainTime = DTime.Djs(mnNextMillistime);
                    RemainTime = remainTime;
                    if (mnNextMillistime1 > 0) {
                        mnNextMillistime1--;
                    }
                    ((TextView) findViewById(R.id.tv_next_time)).setText("" + mnNextMillistime1);
                    tv_JG.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.VISIBLE);
                }

            } else if (mCurStatus == ConstantDef.STATUS_POINT_FULL) {
                mCurStatus = ConstantDef.STATUS_INVALID;
                ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_INVALID);
                stopTimer();
                Preferences.saveStep(Point_Collect.this, 99);// zgl
                // 退出有效点采集界面
                Point_Collect.this.finish();

            }

            // 警告文字显示条件
            if (mnDelay <= 0) {

                if (mnNextMillistime1 > 0 && issjjd == true) {
                    findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                    tv_JG.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.VISIBLE);
                } else {
                    tv_JG.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                    if (isRadio == true) {
                        findViewById(R.id.btn_ensure_point).setVisibility(View.VISIBLE);
                        isButton = true;
                    }
                    if (mbToUp == true) {
                        findViewById(R.id.btn_ensure_point).setVisibility(View.VISIBLE);
                    }

                }
//							if (RemainTime == 0
//									&& isButton == false) {
//								isButton = true;
//							}
////							if (RemainTime == 1
//									&& isButton == false) {
//								isJing = true;
//								isButton = true;
//							} else {
//								tv_JG.setVisibility(View.GONE);
//								((TextView) findViewById(R.id.tv_next_time))
//										.setVisibility(View.INVISIBLE);
//								// shanchu.setVisibility(View.GONE);
//							}
//							if (isJing == true && isButton == true) {
//								tv_JG.setVisibility(View.GONE);
//								((TextView) findViewById(R.id.tv_next_time))
//										.setVisibility(View.INVISIBLE);
//								// shanchu.setVisibility(View.GONE);
//							} else {
//								tv_JG.setVisibility(View.VISIBLE);
//								((TextView) findViewById(R.id.tv_next_time))
//										.setVisibility(View.VISIBLE);
//								// shanchu.setVisibility(View.VISIBLE);
//							}
            } else {
                tv_JG.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);

                // shanchu.setVisibility(View.GONE);
            }

            // 确定有效点按钮显示的判断条件
            if (T < (T1 + T2)) {
                findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
            } else {
                if (mbToUp == false && issjjd == true) {
                    if (isRadio == true && mnNextMillistime1 <= 0) {
                        tv_JG.setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                        findViewById(R.id.btn_ensure_point).setVisibility(View.VISIBLE);
                    } else {
                        //tv_JG.setVisibility(View.VISIBLE);
//						((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                    }
                } else if (mbToUp == true && issjjd == true) {
                    isRadio = true;
                    if (mnNextMillistime1 <= 0) {
                        tv_JG.setVisibility(View.GONE);
                        ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                        findViewById(R.id.btn_ensure_point).setVisibility(View.VISIBLE);
                        if (mbDeepmanual) {
                            if (isJzhun == true) {
                                type = "校起";
                            } else {
                                type = "手";
                            }
                        } else {
                            if (isJzhun == true) {
                                type = "校起";
                            } else {
                                type = "/";
                            }
                        }
                    } else {
                        tv_JG.setVisibility(View.VISIBLE);
                        ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.VISIBLE);
                        findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                    }
                } else if (isRadio == true) {

                } else {
                    if (mbToUp == true) {
                        findViewById(R.id.btn_ensure_point).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                    }
                }
                // 警告文字显示条件


            }
            // ////////////////////////////
            if (isJzhun == true) {
                // et_JZ.setVisibility(View.VISIBLE);
                // tv_JZ.setVisibility(View.VISIBLE);
                if (type.equals("煤")) {
                    type = "校煤";
                } else if (type.equals("岩")) {
                    type = "校岩";
                } else if (type.equals("/")) {
                    type = "校起";
                } else if (type.equals("手岩") || type.equals("手煤")) {
                    type = "校手";
                } else if (type.equals("手")) {
                    type = "校手";
                }
                et_JZ.setFilters(new InputFilter[]{Utils.getDotInputFilter(1), Utils.getLengthInputFilter(12)});
                et_JZ.addTextChangedListener(new TextWatcherMaxFloat(Point_Collect.this, et_JZ, -1, Float.MAX_VALUE));
                et_JZ.getText().toString();
            } else if (isJzhun == false) {
                tv_JZ.setVisibility(View.GONE);
                et_JZ.setVisibility(View.GONE);
            }
            if (isDialog == true) {
//				Bet_JZ.setFilters(new InputFilter[] {
//						Utils.getDotInputFilter(1),
//						Utils.getLengthInputFilter(12) });
                Bet_JZ.addTextChangedListener(new TextWatcherMaxFloat(Point_Collect.this, Bet_JZ, -1, Float.MAX_VALUE));
                Bet_JZ.getText().toString();
            }
            if (isButton == true) {
                if (Preferences.getSumPoint(Point_Collect.this) > 0) {

                    if ((Preferences.getSumPoint(Point_Collect.this)) % (Preferences.getJiaoZhun(Point_Collect.this)) == 0 && !isDialog) {

                        final Dialog dialog1 = new Dialog(Point_Collect.this);
                        isDialog = true;
                        // 设置它的ContentView
                        dialog1.setTitle("校准孔深");
                        dialog1.setContentView(R.layout.aaaaaaaaaa);
                        dialog1.setCanceledOnTouchOutside(false);
                        dialog1.setCancelable(false);
                        Bet_JZ = (EditText) dialog1.findViewById(R.id.det_jiaozhun);
                        Bet_JZ.setText(String.valueOf(mnLastDeep));
                        Bet_JZ.setSelection(Bet_JZ.getText().toString().length());
                        dialog1.findViewById(R.id.bt_jiaozhun).setOnClickListener(

                                new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                        builder.setTitle("请确认当前孔深为" + Bet_JZ.getText().toString()).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if (Bet_JZ.getText().toString().equals("")) {
                                                    Utils.toast(Point_Collect.this, "请输入校准孔深！");
                                                    return;
                                                }
                                                // ///////////////强制校准////////////////////////
                                                if (isDialog == true) {
                                                    if (type.equals("煤")) {
                                                        type = "校煤";
                                                    } else if (type.equals("岩")) {
                                                        type = "校岩";
                                                    } else if (type.equals("/")) {
                                                        type = "校起";
                                                    } else if (type.equals("手岩") || type.equals("手煤")) {
                                                        type = "校手";
                                                    } else if (type.equals("手")) {
                                                        type = "校手";
                                                    }
                                                    isjiaozhun = true;
                                                    Float deep = Float.parseFloat(Bet_JZ.getText().toString());

                                                    if (mbToUp) {

                                                        if (mPointList.get(mPointList.size() - 2).deep < deep) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                                            builder.setTitle("输入孔深应小于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).setCancelable(false).show();
                                                            //Utils.toast(Point_Collect.this, "输入孔深应小于末行孔深！");
                                                            return;
                                                        }
                                                    } else {
                                                        if (mPointList.get(mPointList.size() - 2).deep > deep) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                                            builder.setTitle("输入孔深应大于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).setCancelable(false).show();
                                                            //Utils.toast(Point_Collect.this, "输入孔深应大于末行孔深！");
                                                            //	--mnPointSerNum;
                                                            return;
                                                        }
                                                    }
                                                    Log.e("mnLastDeep", mnLastDeep + "");
                                                    Log.e("deep", deep + "");
                                                    mnLastDeep = deep;
                                                    mnPointNextID = list_bhs.get(list_bhs.size() - 1);

                                                    // 保存起测点孔深,测量间隔
                                                    Preferences.saveStartDeep(Point_Collect.this, Bet_JZ.getText().toString());
                                                    Preferences.saveMeasureInterval(Point_Collect.this, mEditMeasureInterval.getText().toString());
                                                    mnPointSerNum--;
                                                    if (mnPointSerNum == 0) {
                                                        mnPointSerNum++;
                                                        Utils.toast(Point_Collect.this, "删除失败，请保留一个采集点！");
                                                        return;
                                                    }
                                                    //mPointList.get(mnPointNextID);
                                                    if (mPointList.size() > 0) {
                                                        // 更新全预览界面
                                                        mPointList.remove(mPointList.size() - 1);
                                                    }

                                                    for (int i = 0; i < mPointList.size(); i++) {
                                                        mPointList.get(i).serNum = Integer.toString(i + 1);
                                                    }
                                                    mnLastDeep = (mPointList.get(mPointList.size() - 1).deep);
                                                    mPointAdapter.notifyDataSetChanged();

                                                    // 更新中间预览界面
                                                    mPrePointList.set(0, mPointList.get(mPointList.size() - 1));
                                                    mPrePointAdapter.notifyDataSetChanged();

                                                    // 更改文件数据,自动模式下重新写文件
                                                    FileUtils.FileNewCreate(mFilePath);
                                                    FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) (mPointList.size())));
                                                    for (int i = 0; i < mPointList.size(); i++) {
                                                        byte[] point = TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id), (mPointList.get(i).deep), mPointList.get(i).data, Utils.millisToTimeStr(mPointList.get(i).millisTime), mPointList.get(i).type);
                                                        ii = mPointList.get(i).millisTime;
                                                        Log.e("ii", ii + "");
                                                        // 写入有效点
                                                        FileUtils.appendWriteFile(mFilePath, point);
                                                    }

                                                    mPointAdapter.notifyDataSetChanged();
                                                    mPrePointAdapter.notifyDataSetChanged();


                                                    if (mbDeepmanual) {
                                                        // 手动输入孔深

                                                        // 判断是否为空
                                                        if (Bet_JZ.getText().toString().equals("")) {
                                                            Utils.toast(Point_Collect.this, "请输入采集点孔深！");
                                                            issjjd = false;
                                                            return;
                                                        }
                                                        if (mnLastMillistime == mnNextMillistime) {
                                                            return;

                                                        } else {
                                                            mnLastMillistime = mnNextMillistime;
                                                        }
                                                        mnPointSerNum++;
                                                        //	u.setText(mEditValidDeep);
                                                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());

                                                        // 增加TYPE
                                                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);

                                                        // 更新显示有效点
                                                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));

                                                        // Collections.reverse(mPointList);
                                                        // // 逆序排列
                                                        mPointAdapter.notifyDataSetChanged();

                                                        if (mPrePointList.isEmpty())
                                                            mPrePointList.add(
                                                                    // 0,
                                                                    new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                        else mPrePointList.

                                                                // set
                                                                        add(
                                                                        // 0,
                                                                        new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                        mPrePointAdapter.notifyDataSetChanged();

                                                        mLvPrePoint.setAdapter(mPointAdapter);
                                                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                                                        // 存储更新有效点点数
                                                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                                                        // 写入此次有效点
                                                        FileUtils.appendWriteFile(mFilePath, point);

                                                    } else {
                                                        // 自动生成孔深

                                                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                                                        if (mnLastMillistime == mnNextMillistime) {
                                                            return;
                                                        } else {
                                                            mnLastMillistime = mnNextMillistime;
                                                        }
                                                        mnPointSerNum++;
                                                        if (mbToUp) {
                                                            // 起钻
                                                            // if (isDialog ==
                                                            // true) {
                                                            // if(type.equals("起")){
                                                            // type="校起";
                                                            // }
                                                            // }
                                                            // if((Float.parseFloat(Bet_JZ
                                                            // .getText().toString())
                                                            // >(mnLastDeep-
                                                            // Float.parseFloat(mEditMeasureInterval
                                                            // .getText().toString())))){
                                                            // Utils.toast(Point_Collect.this,
                                                            // "检查输入值！");
                                                            // }
                                                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) + Float.parseFloat(mEditMeasureInterval.getText().toString());

                                                            mnLastDeep = mnLastDeep - Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                            mnLastDeep = getdeep(mnLastDeep);
                                                        } else {
                                                            // 下钻
                                                            // if (isDialog ==
                                                            // true) {
                                                            // if(type.equals("煤")){
                                                            // type="校煤";
                                                            // }else
                                                            // if(type.equals("岩")){
                                                            // type="校岩";
                                                            // }
                                                            // }、
                                                            // if((Float.parseFloat(Bet_JZ
                                                            // .getText().toString())
                                                            // <(mnLastDeep+Float.parseFloat(mEditMeasureInterval
                                                            //
                                                            // .getText().toString())))){
                                                            // Utils.toast(Point_Collect.this,
                                                            // "检查输入值！");
                                                            // }
                                                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) - Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                            mnLastDeep = mnLastDeep + Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                            mnLastDeep = getdeep(mnLastDeep);

                                                        }
                                                        if (mnPointSerNum == 1) {
                                                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                                                        }
                                                        if (mnLastDeep < 0) {
                                                            Utils.toast(Point_Collect.this, "孔深出现负值！");
                                                        }
                                                        // 增加TYPE
                                                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);
                                                        // 更新显示有效点
                                                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                        mPointAdapter.notifyDataSetChanged();

                                                        if (mPrePointList.isEmpty())
                                                            mPrePointList.add(
                                                                    // 0,
                                                                    new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                        else mPrePointList.
                                                                // set
                                                                        add(
                                                                        // 0,
                                                                        new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                        mPrePointAdapter.notifyDataSetChanged();

                                                        mLvPrePoint.setAdapter(mPointAdapter);
                                                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                                                        // 存储更新有效点点数
                                                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                                                        // 写入此次有效点
                                                        FileUtils.appendWriteFile(mFilePath, point);
                                                    }

                                                }
                                                isJing = false;
                                                isRadio = false;
                                                isButton = false;
                                                findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                                                dialog1.dismiss();
                                                dialog.dismiss();
                                                MYSCeng.clearCheck();
                                                Preferences.saveSumPoint(Point_Collect.this, mnPointSerNum);
                                                Log.d("mylog", "mnPointNextID = " + mnPointNextID);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated
                                                // method
                                                // stub
                                            }
                                        }).show();
                                    }
                                });

                        // dialog.findViewById(R.id.btn_jiaozhun).setOnClickListener(
                        // new OnClickListener() {
                        //
                        // @Override
                        // public void onClick(View arg0) {
                        // // TODO Auto-generated method stub
                        // isRadio=false;
                        // // MYSCeng.clearCheck();
                        // dialog.dismiss();
                        // }
                        // });
                        dialog1.show();
                    }
                }
            }
            if (Preferences.getSumPoint(Point_Collect.this) <= 0) {// 延时未结束
                bt_Jzhun.setEnabled(false);
            } else {
                bt_Jzhun.setEnabled(true);
            }
            mHandler.postDelayed(this, 1000);
            if (mPointList.size() <= 0) {
                Bun_shanchu.setEnabled(false);
            } else {
                Bun_shanchu.setEnabled(true);
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

        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        // WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.point_collect);
        timerTextView = (TimerTextView) findViewById(R.id.timer_text_view);
        Context ctx = Point_Collect.this;
        sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
        if (Preferences.getDeepManual(this)) {

            type = "手";

        }
        acquireWakeLock();
        mTvRemainTime = (TextView) findViewById(R.id.tv_remaintime);
        /**
         * 获取WIFI服务
         */
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        et_JZ = (EditText) findViewById(R.id.et_jiaozhun);
        tv_JG = (TextView) findViewById(R.id.tv_jg);
        // 有效点采集时间
        tv_time = (TextView) findViewById(R.id.tv_time);
        // tian=(TextView) findViewById(R.id.tv_tian);
        // fen=(TextView) findViewById(R.id.tv_fen);
        // 岩石类型
        YSceng = (RadioButton) findViewById(R.id.YSceng);
        Mceng = (RadioButton) findViewById(R.id.Mceng);
        bt_Jzhun = (Button) findViewById(R.id.jiaozhun);
        MYSCeng = (RadioGroup) findViewById(R.id.MYSCeng);
        Bun_shanchu = (Button) findViewById(R.id.btn_shanchu);

        Bun_shanchu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                builder.setCancelable(false); // 不响应back按钮
                builder.setMessage("        数据删除后不可恢复，确定删除末行数据吗？"); // 对话框显示内容
                // 设置按钮
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除有效点
                        mnPointSerNum--;
                        if (mnPointSerNum == 0) {
                            mnPointSerNum++;
                            Utils.toast(Point_Collect.this, "删除失败，请保留一个采集点！");
                            return;
                        }

                        if (mPointList.size() > 0) {
                            // 更新全预览界面
                            mPointList.remove(mPointList.size() - 1);
                            list_bhs.remove(list_bhs.size() - 1);

                        }
                        Preferences.saveSumPoint(Point_Collect.this, mnPointSerNum);
                        for (int i = 0; i < mPointList.size(); i++) {
                            mPointList.get(i).serNum = Integer.toString(i + 1);
                        }
                        mnLastDeep = (mPointList.get(mPointList.size() - 1).deep);
                        mPointAdapter.notifyDataSetChanged();

                        // 更新中间预览界面
                        mPrePointList.set(0, mPointList.get(mPointList.size() - 1));
                        mPrePointAdapter.notifyDataSetChanged();

                        // 更改文件数据,自动模式下重新写文件
                        FileUtils.FileNewCreate(mFilePath);
                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) (mPointList.size())));
                        for (int i = 0; i < mPointList.size(); i++) {
                            byte[] point = TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id), (mPointList.get(i).deep), mPointList.get(i).data, Utils.millisToTimeStr(mPointList.get(i).millisTime), mPointList.get(i).type);
                            // 写入有效点
                            FileUtils.appendWriteFile(mFilePath, point);
                        }

                        mPointAdapter.notifyDataSetChanged();
                        mPrePointAdapter.notifyDataSetChanged();

                    }

                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//								Toast.makeText(Point_Collect.this, "点击了取消按钮",
//										Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });
        if ("P".equals(Preferences.getBenString(this))) {
            mFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-"
                    // + Preferences.getDrillingString(this)+"-"
                    + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;

            strname = Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-"
                    // + Preferences.getDrillingString(this)+"-"
                    + Preferences.getHoleIDString(this);
        } else {
            mFilePath = ConstantDef.FILE_PATH

                    + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;

            strname = Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this);
        }

        final long T = Utils.getCurTime().toMillis(false);
        T1 = Preferences.getStartTime(this);
        T2 = Preferences.getDelayTime(this) * 60 * 1000;
        T3 = Preferences.getIntervalTime(this) * 1000;
        // mnRemainTime = (T3 * (K1 - 1)) / 1000;
        // zgl 2014-11-11 21:46:47
        mnRemainTime = ((T3 * (K1 - 1)) - (T - T1)) / 1000;
        mProgressBar = (ProgressBar) findViewById(R.id.pb_remain_time);
        // mProgressBar.setMax((int)mnRemainTime);
        // mProgressBar.setProgress((int)mnRemainTime);
        if (T < (T1 + T2)) {
            // 延时状态
            mCurStatus = ConstantDef.STATUS_DELAY;
            mnDelay = (T1 + T2 - T) / 1000;// 转换为秒
            System.out.println("mnDelay = " + mnDelay);
            ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(false);
            ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_DELAY);
            // 设定剩余延时时间进度
            mTvRemainTime.setText(REMAIN_DELAY_TIME);
            mProgressBar.setMax((int) T2 / 1000);
            mProgressBar.setProgress((int) mnDelay);
        } else {
            if (T > (T1 + T2 + T3 * (K1 - 1))) {
                // 机芯采集满
                mCurStatus = ConstantDef.STATUS_POINT_FULL;
                ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(false);
                ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_POINT_FULL);
                Utils.toast(this, "异常恢复, 机芯采集已满！");
            } else {
                mnPointNextID = (int) ((T - T1 - T2) / T3 + 1) + 1;
                mCurStatus = ConstantDef.STATUS_ENSURE_POINT;
                ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(true);
                ((TextView) findViewById(R.id.tv_ensurepoint_status)).setText(ConstantDef.STR_STATUS_ENSURE_POINT);
                mnNextMillistime = Preferences.getNextMillistime(Point_Collect.this);// zgl
                mnNextMillistime = (Math.abs(mnNextMillistime - T) / T3) * T3 + mnNextMillistime;
                recover();
                istime = true;
                System.out.println("mnPointID = " + mnPointNextID);
            }
            // 设定剩余采集时间进度
            mTvRemainTime.setText(REMAIN_COLLECT_TIME);
            mProgressBar.setMax((int) (T3 * (K1 - 1)) / 1000);
            mProgressBar.setProgress((int) mnRemainTime);
        }
        startTimer();
        init();
        System.out.println(Preferences.getSumPoint(this));
        System.out.println(Preferences.getJiaoZhun(this));

        if (istime == true) {
            //倒计时剩余时间

            time1 = (Preferences.getIntervalTime(Point_Collect.this)) * (21000 - 1);
            Log.e("time23", "time" + time);
        } else {
            //延时剩余时间
            time1 = Preferences.getDelayTime(Point_Collect.this) * 60;
            Log.e("lllllllllllkkkkkkkk", "time" + time);
        }
//
        new Thread() {
            public void run() {
                while (flag) {

                    if (time > 0) {
                        long T = Utils.getCurTime().toMillis(false);
                        if (T - T2 - T1 > 0) {
                            time = time1 - ((T - T1 - T2) / 1000);
                        } else {
                            time = time1 - ((T - T1) / 1000);
                        }
                        if (time == 0) {
                            ist = true;
                        }
                    }
                    if (time == 0) {
                        if (ist == true) {
                            time1 = (Preferences.getIntervalTime(Point_Collect.this)) * (21000 - 1);
                        }
                        if (istime == true) {
                            time = (Preferences.getIntervalTime(Point_Collect.this)) * (21000 - 1);
                        } else {
                            time = (Preferences.getIntervalTime(Point_Collect.this)) * (21000 - 1);
                        }
                    }
                    djs();

                    handler.sendEmptyMessage(0x1);

                    try {
                        sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // sb.delete(0, sb.toString().length());
                }
            }

            ;
        }.start();
        // 电量监测
        batteryLevel();
    }

    private int getlsit() {
        // TODO Auto-generated method stub
        return 0;
    }

    private void djs() {
        // long day = time / 86400;
        // tian.setText(String.valueOf(day));
        // djstime[0]=day;
        // Log.e("djstime[0]=day;", djstime[0]+"");
        long hour = time / 3600;
        djstime[1] = hour;
        // tian.setText(String.valueOf(hour));
        // Log.e("djstime[0]=day;", djstime[1]+"");
        long minute = time % 3600 / 60;
        djstime[2] = minute; // tian.setText(String.valueOf(minute));
        // Log.e("djstime[0]=day;", djstime[2]+"");
        // sb.append(minute);
        // sb.append("分");

        long second = time % 3600 % 60;
        djstime[3] = second;
        // Log.e("djstime[0]=day;", djstime[3]+"");
        // Log.e("arrays", Arrays.toString(djstime));

    }

    // //////////////////////////
    private void init() {
        // 手动校准

        bt_Jzhun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isButton == true) {
                    // TODO Auto-generated method stub

                    // flipper.setDisplayedChild(1);
                    // pointtab.check(R.id.rb_ensure_point);
                    // /////////////////////////////////////////////
                    // mLvPrePoint.setSelection(mPointList.size()+2);
                    et_JZ.setText("");
                    isJzhun = true;
                    isjiaozhun = true;
                    isDialog = true;
                    final Dialog dialog1 = new Dialog(Point_Collect.this);


                    // 设置它的ContentView
                    dialog1.setTitle("校准孔深");
                    dialog1.setContentView(R.layout.aaaaaaaaaa);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(false);
                    dialog1.findViewById(R.id.b_jiaozhun).setVisibility(Button.VISIBLE);
                    Bet_JZ = (EditText) dialog1.findViewById(R.id.det_jiaozhun);
                    //	Bet_JZ.setText(String.valueOf(mPointList.get(mPointList.size() - 2).deep));
                    Bet_JZ.setText(String.valueOf(mnLastDeep));
                    dialog1.findViewById(R.id.bt_jiaozhun).setOnClickListener(

                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                    builder.setTitle("请确认当前孔深为" + Bet_JZ.getText().toString()).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // TODO Auto-generated method
                                            // stub

                                            if (Bet_JZ.getText().toString().equals("")) {
                                                Utils.toast(Point_Collect.this, "请输入校准孔深！");
                                                return;
                                            }
                                            // ///////////////手动校准////////////////////////
                                            if (isDialog == true) {
//													if (type.equals("煤")) {
//														type = "校煤";
//													} else if (type.equals("岩")) {
//														type = "校岩";
//													} else if (type.equals("/")) {
//														type = "校起";
//													}
                                                Float deep = Float.parseFloat(Bet_JZ.getText().toString());
                                                if (mbToUp) {
                                                    if (mPointList.get(mPointList.size() - 2).deep <= deep) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                                        builder.setTitle("输入孔深应小于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        }).setCancelable(false).show();
                                                        //Utils.toast(Point_Collect.this, "输入孔深应小于末行孔深！");
                                                        return;
                                                    }
                                                } else {
                                                    if (mPointList.get(mPointList.size() - 2).deep >= deep) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                                        builder.setTitle("输入孔深应大于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        }).setCancelable(false).show();
                                                        //Utils.toast(Point_Collect.this, "输入孔深应大于末行孔深！");
                                                        //--mnPointSerNum;
                                                        return;
                                                    }
                                                }
                                                mnLastDeep = deep;
                                                mnPointNextID = list_bhs.get(list_bhs.size() - 1);
                                                // 保存起测点孔深,测量间隔
                                                Preferences.saveStartDeep(Point_Collect.this, Bet_JZ.getText().toString());
                                                Preferences.saveMeasureInterval(Point_Collect.this, mEditMeasureInterval.getText().toString());
                                                mnPointSerNum--;
                                                if (mnPointSerNum == 0) {
                                                    mnPointSerNum++;
                                                    Utils.toast(Point_Collect.this, "删除失败，请保留一个采集点！");
                                                    return;
                                                }
                                                //mPointList.get(mnPointNextID);
                                                if (mPointList.size() > 0) {
                                                    // 更新全预览界面
                                                    mPointList.remove(mPointList.size() - 1);


                                                }

                                                for (int i = 0; i < mPointList.size(); i++) {
                                                    mPointList.get(i).serNum = Integer.toString(i + 1);
                                                }
                                                mnLastDeep = (mPointList.get(mPointList.size() - 1).deep);
                                                mPointAdapter.notifyDataSetChanged();

                                                // 更新中间预览界面
                                                mPrePointList.set(0, mPointList.get(mPointList.size() - 1));
                                                mPrePointAdapter.notifyDataSetChanged();

                                                // 更改文件数据,自动模式下重新写文件
                                                FileUtils.FileNewCreate(mFilePath);
                                                FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) (mPointList.size())));
                                                for (int i = 0; i < mPointList.size(); i++) {
                                                    byte[] point = TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id), (mPointList.get(i).deep), mPointList.get(i).data, Utils.millisToTimeStr(mPointList.get(i).millisTime), mPointList.get(i).type);
                                                    // 写入有效点
                                                    FileUtils.appendWriteFile(mFilePath, point);
                                                }

                                                mPointAdapter.notifyDataSetChanged();
                                                mPrePointAdapter.notifyDataSetChanged();


                                                if (mbDeepmanual) {


                                                    // 手动输入孔深

                                                    // 判断是否为空
                                                    if (Bet_JZ.getText().toString().equals("")) {
                                                        Utils.toast(Point_Collect.this, "请输入采集点孔深！");
                                                        issjjd = false;
                                                        return;
                                                    }
                                                    if (mnLastMillistime == mnNextMillistime) {
                                                        return;

                                                    } else {
                                                        mnLastMillistime = mnNextMillistime;
                                                    }
                                                    mnPointSerNum++;
                                                    //	u.setText(mEditValidDeep);
                                                    mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());

                                                    // 增加TYPE
                                                    byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);

                                                    // 更新显示有效点
                                                    mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));

                                                    // Collections.reverse(mPointList);
                                                    // // 逆序排列
                                                    mPointAdapter.notifyDataSetChanged();

                                                    if (mPrePointList.isEmpty()) mPrePointList.add(
                                                            // 0,
                                                            new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                    else mPrePointList.

                                                            // set
                                                                    add(
                                                                    // 0,
                                                                    new ValidPointInfo(Integer.toString(mnPointSerNum), mnLastDeep, Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                    mPrePointAdapter.notifyDataSetChanged();

                                                    mLvPrePoint.setAdapter(mPointAdapter);
                                                    mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                                                    // 存储更新有效点点数
                                                    FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                                                    // 写入此次有效点
                                                    FileUtils.appendWriteFile(mFilePath, point);

                                                } else {
                                                    // 自动生成孔深

                                                    mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                                                    if (mnLastMillistime == mnNextMillistime) {
                                                        return;
                                                    } else {
                                                        mnLastMillistime = mnNextMillistime;
                                                    }
                                                    mnPointSerNum++;
                                                    if (mbToUp) {
                                                        // 起钻
                                                        // if (isDialog ==
                                                        // true) {
                                                        // if(type.equals("起")){
                                                        // type="校起";
                                                        // }
                                                        // }
                                                        // if((Float.parseFloat(Bet_JZ
                                                        // .getText().toString())
                                                        // >(mnLastDeep-
                                                        // Float.parseFloat(mEditMeasureInterval
                                                        // .getText().toString())))){
                                                        // Utils.toast(Point_Collect.this,
                                                        // "检查输入值！");
                                                        // }
                                                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) + Float.parseFloat(mEditMeasureInterval.getText().toString());

                                                        Log.e("1111111mnLastDeep", mnLastDeep + "");
                                                        mnLastDeep = mnLastDeep - Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                        mnLastDeep = getdeep(mnLastDeep);
                                                        Log.e("1111111", mnLastDeep + "");
                                                    } else {
                                                        // 下钻
                                                        // if (isDialog ==
                                                        // true) {
                                                        // if(type.equals("煤")){
                                                        // type="校煤";
                                                        // }else
                                                        // if(type.equals("岩")){
                                                        // type="校岩";
                                                        // }
                                                        // }、
                                                        // if((Float.parseFloat(Bet_JZ
                                                        // .getText().toString())
                                                        // <(mnLastDeep+Float.parseFloat(mEditMeasureInterval
                                                        //
                                                        // .getText().toString())))){
                                                        // Utils.toast(Point_Collect.this,
                                                        // "检查输入值！");
                                                        // }
                                                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) - Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                        Log.e("2222222mnLastDeep", mnLastDeep + "");
                                                        mnLastDeep = mnLastDeep + Float.parseFloat(mEditMeasureInterval.getText().toString());
                                                        Log.e("1111111mnLastDeep", mnLastDeep + "");
                                                        mnLastDeep = getdeep(mnLastDeep);

                                                    }
                                                    if (mnPointSerNum == 1) {
                                                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                                                    }
                                                    if (mnLastDeep < 0) {
                                                        Utils.toast(Point_Collect.this, "孔深出现负值！");
                                                    }
                                                    // 增加TYPE
                                                    byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);
                                                    // 更新显示有效点
                                                    mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                    mPointAdapter.notifyDataSetChanged();

                                                    if (mPrePointList.isEmpty()) mPrePointList.add(
                                                            // 0,
                                                            new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                    else mPrePointList.
                                                            // set
                                                                    add(
                                                                    // 0,
                                                                    new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                                                    mPrePointAdapter.notifyDataSetChanged();

                                                    mLvPrePoint.setAdapter(mPointAdapter);
                                                    mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                                                    // 存储更新有效点点数
                                                    FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                                                    // 写入此次有效点
                                                    FileUtils.appendWriteFile(mFilePath, point);
                                                }

                                            }
                                            isJzhun = false;
                                            isRadio = false;
                                            isButton = false;

                                            findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                                            dialog1.dismiss();
                                            dialog.dismiss();
                                            MYSCeng.clearCheck();
                                            Preferences.saveSumPoint(Point_Collect.this, mnPointSerNum);
                                            Log.d("mylog", "mnPointNextID = " + mnPointNextID);
                                            // ////////////////////////////////////////////

                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            // TODO Auto-generated method
                                            // stub
                                        }
                                    }).show();
                                }
                            });

                    dialog1.findViewById(R.id.b_jiaozhun).setOnClickListener(

                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    MYSCeng.clearCheck();
                                    isJzhun = false;
                                    isRadio = false;
                                    isButton = false;

                                    findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
                                    dialog1.dismiss();

                                }
                            });
                    dialog1.show();
                    // } else {
                    // //flipper.setDisplayedChild(1);
                    // //pointtab.check(R.id.rb_ensure_point);
                    // Utils.toast(Point_Collect.this, "请选择地质信息！");
                    // return;
                    // }
                } else if (isRadio == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                    builder.setTitle("请选择地质信息!").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setCancelable(false).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                    builder.setTitle("采集点稳定后在进行更改!").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //isfangan=true;

                        }
                    }).setCancelable(false).show();
                    //Utils.toast(Point_Collect.this, "输入孔深应小于末行孔深！");
                    return;

                }
            }
        });
        // 煤，岩层单选按钮点击事件
        Mceng.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                isRadio = true;

            }
        });
        YSceng.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                isRadio = true;
                //
            }
        });

        detector = new GestureDetector(this);
        flipper = (ViewFlipper) findViewById(R.id.flipper);

        Mceng.setVisibility(View.INVISIBLE);
        YSceng.setVisibility(View.INVISIBLE);

        MYSCeng.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.Mceng) {
                    if (isJzhun == true) {
                        type = "校煤";

                    } else {
                        type = "煤";
                    }
                } else if (checkedId == R.id.YSceng) {
                    if (isJzhun == true) {
                        type = "校岩";

                    } else {
                        type = "岩";
                    }

                }
                if (mbDeepmanual) {
                    if (checkedId == R.id.Mceng) {

                        type = "手煤";

                    } else if (checkedId == R.id.YSceng) {

                        type = "手岩";


                    }
                    if (mbToUp) {

                        type = "/";

                    }
                    //isjiaozhun=false;
                }
            }

        });
        // 显示上次记录的确定有效点方法
        mLvPrePoint = (ListView) findViewById(R.id.lv_pre_valid_point);
//		if (Preferences.getMeasureWayToUp(Point_Collect.this)) {
//		//	mbToUp = true;
//			((RadioGroup) findViewById(R.id.rg_measure_way))
//					.check(R.id.rb_toup);
//			Mceng.setVisibility(View.INVISIBLE);
//			YSceng.setVisibility(View.INVISIBLE);
//
//			// findViewById(R.id.et_start_deep).setVisibility(
//			// View.VISIBLE);
//			// findViewById(R.id.et_measure_interval).setVisibility(
//			// View.VISIBLE);
//			// findViewById(R.id.qiks2).setVisibility(
//			// View.VISIBLE);
//			// findViewById(R.id.qiks3).setVisibility(
//			// View.VISIBLE);
//			// findViewById(R.id.qiks4).setVisibility(
//			// View.VISIBLE);
//			// findViewById(R.id.qiks1).setVisibility(
//			// View.VISIBLE);
//		} else {
        mbToUp = false;

        ((RadioGroup) findViewById(R.id.rg_measure_way)).check(R.id.rb_todown);
        Mceng.setVisibility(View.VISIBLE);
        YSceng.setVisibility(View.VISIBLE);
        // findViewById(R.id.et_start_deep).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.et_measure_interval).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.qiks2).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.qiks3).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.qiks4).setVisibility(
        // View.VISIBLE);
        // findViewById(R.id.qiks1).setVisibility(
        // View.VISIBLE);
//		}
        if (Preferences.getDeepManual(Point_Collect.this)) {
            if (mbToUp == true) {
                Mceng.setVisibility(View.INVISIBLE);
                YSceng.setVisibility(View.INVISIBLE);

            } else {
                Mceng.setVisibility(View.VISIBLE);
                YSceng.setVisibility(View.VISIBLE);
            }
            mbDeepmanual = true;

            findViewById(R.id.ll_deepmanualinput).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_deepmanualinput).setVisibility(View.VISIBLE);
            findViewById(R.id.et_start_deep).setVisibility(View.GONE);
            findViewById(R.id.et_measure_interval).setVisibility(View.GONE);
            findViewById(R.id.qiks2).setVisibility(

                    View.GONE);
            findViewById(R.id.qiks3).setVisibility(View.GONE);
            findViewById(R.id.qiks4).setVisibility(View.GONE);
            findViewById(R.id.qiks1).setVisibility(View.GONE);
            // TODO Auto-generated method stub
            // LinearLayout.LayoutParams linearParams
            // =(LinearLayout.LayoutParams) mLvPrePoint.getLayoutParams();
            // //取控件textView当前的布局参数
            // linearParams.height = 120;// 控件的高强制设成
            // mLvPrePoint.setLayoutParams(linearParams);

            // findViewById(R.id.tv_measure_method).setVisibility(View.GONE);
            // findViewById(R.id.rg_measure_way).setVisibility(View.GONE);
            ((RadioGroup) findViewById(R.id.rg_deep_manual_auto)).check(R.id.rb_deepmanual);

        } else {
            mbDeepmanual = false;
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mLvPrePoint.getLayoutParams(); // 取控件textView当前的布局参数
            linearParams.height = 240;// 控件的高强制设成
            mLvPrePoint.setLayoutParams(linearParams);
            findViewById(R.id.ll_deepmanualinput).setVisibility(View.GONE);
            findViewById(R.id.tv_deepmanualinput).setVisibility(View.GONE);
            findViewById(R.id.tv_measure_method).setVisibility(View.VISIBLE);
            findViewById(R.id.rg_measure_way).setVisibility(View.VISIBLE);
            ((RadioGroup) findViewById(R.id.rg_deep_manual_auto)).check(R.id.rb_deepauto);

        }
        tv_JZ = (TextView) findViewById(R.id.tv_jiaozhun);

        mEditStartDeep = (EditText) findViewById(R.id.et_start_deep);
//		mEditStartDeep.setFilters(new InputFilter[] {
//				Utils.getDotInputFilter(1), Utils.getLengthInputFilter(6) });
        mEditStartDeep.addTextChangedListener(new TextWatcherMaxFloat(Point_Collect.this, mEditStartDeep, -1, 9999.9f));
        mEditStartDeep.setText(Preferences.getStartDeep(Point_Collect.this));

        mEditMeasureInterval = (EditText) findViewById(R.id.et_measure_interval);
        //mEditMeasureInterval.setFilters(new InputFilter[] {Utils.getDotInputFilter(1), Utils.getLengthInputFilter(4) });
        mEditMeasureInterval.addTextChangedListener(new TextWatcherMaxFloat(Point_Collect.this, mEditMeasureInterval, -1, 99.9f));
        mEditMeasureInterval.setText(Preferences.getMeasureInterval(Point_Collect.this));

        mEditValidDeep = (EditText) findViewById(R.id.et_valid_deep);
//		mEditValidDeep.setFilters(new InputFilter[] {
//				Utils.getDotInputFilter(1), Utils.getLengthInputFilter(12) });
        mEditValidDeep.addTextChangedListener(new TextWatcherMaxFloat(Point_Collect.this, mEditValidDeep, -1, Float.MAX_VALUE));

        mPrePointAdapter = new ValidPointAdapter(this, mPrePointList);
        mLvPrePoint.setAdapter(mPrePointAdapter);

        mLvPoint = (ListView) findViewById(R.id.lv_valid_point);
        mPointAdapter = new ValidPointAdapter(this, mPointList);
        mLvPoint.setAdapter(mPointAdapter);

        mLvPoint.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //initPopupWindow(position);

                return false;
            }
        });

//		 mLvPoint.setSelection(mPointList.size());
//
//		 mLvPoint.setOnItemClickListener(new OnItemClickListener() {
//
//		 public void onItemClick(AdapterView<?> arg0, View arg1, final int
//		 arg2, long arg3) {
//
//		 final EditText inputDeep = new EditText(Point_Collect.this);
//		 inputDeep.setInputType(InputType.TYPE_CLASS_NUMBER |
//		 InputType.TYPE_NUMBER_FLAG_DECIMAL);
//		 inputDeep.setFilters(new InputFilter[] { Utils.getDotInputFilter(1),
//		 Utils.getLengthInputFilter(12) });
//		 inputDeep.addTextChangedListener(new
//		 TextWatcherMaxFloat(Point_Collect.this, mEditStartDeep, -1,
//		 Float.MAX_VALUE));
//		 AlertDialog.Builder builder = new
//		 AlertDialog.Builder(Point_Collect.this);
//		 builder.setTitle("更新有效点，输入新孔深").setIcon(android.R.drawable.ic_dialog_info).setView(inputDeep).setNeutralButton("取消",
//		 null);
//		 builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//		 {
//
//		 public void onClick(DialogInterface dialog, int which) {
//
//		 String strDeep = inputDeep.getText().toString();
//		 float deep = Float.parseFloat(strDeep);
//		 if (strDeep.equals("")) {
//		 Utils.toast(Point_Collect.this, "新孔深输入为空,更新失败！");
//		 return;
//		 }
//		 // 判断当前是自动模式还是手动模式
//		 if (mbDeepmanual) {// 手动模式仅仅更新当前选项
//
//		 mPointList.get(arg2).deep = 	Float.parseFloat(strDeep);
//		 mPointAdapter.notifyDataSetChanged();
//
//		 mPrePointList.get(0).deep = mPointList.get(mPointList.size() -
//		 1).deep;
//		 mPrePointAdapter.notifyDataSetChanged();
//
//		 // 更改文件数据
//		 FileUtils.RandomWriteFile(mFilePath,
//		 ConstantDef.FILE_VALID_POINT_CNT_BYTES +
//		 ConstantDef.FILE_VALID_POINT_SINGLE_BYTES * arg2 + 2,
//		 Utils.getBytes(deep));
//		 } else {// 自动模式更新后面所有条目
//
//		 for (int i = arg2; i < mPointList.size(); i++) {
//		 mPointList.get(i).deep = (deep);
//		 if (mbToUp) {
//		 deep = deep -
//		 Float.parseFloat(mEditMeasureInterval.getText().toString());
//		 } else {
//		 deep = deep +
//		 Float.parseFloat(mEditMeasureInterval.getText().toString());
//		 }
//		 }
//		 mnLastDeep = (mPointList.get(mPointList.size() -
//		 1).deep);
//		 mPointAdapter.notifyDataSetChanged();
//
//		 mPrePointList.get(0).deep = mPointList.get(mPointList.size() -
//		 1).deep;
//		 mPrePointAdapter.notifyDataSetChanged();
//
//		 // 更改文件数据,自动模式下重新写文件
//		 FileUtils.FileNewCreate(mFilePath);
//		 FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char)
//		 (mPointList.size())));
//		 for (int i = 0; i < mPointList.size(); i++) {
//		 byte[] point =
//		 TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id),
//		 (mPointList.get(i).deep), mPointList.get(i).data,
//		 Utils.millisToTimeStr(mPointList.get(i).millisTime));
//		 // 写入有效点
//		 FileUtils.appendWriteFile(mFilePath, point);
//		 }
//
//		 }
//		 }
//		 });
//		 builder.setNegativeButton("删除", new DialogInterface.OnClickListener()
//		 {
//
//		 @Override
//		 public void onClick(DialogInterface dialog, int which) {
//		 // 删除有效点
//		 mnPointSerNum--;
//		 if (mnPointSerNum == 0) {
//		 mnPointSerNum++;
//		 Utils.toast(Point_Collect.this, "删除失败，请保留一个有效点！");
//		 return;
//		 }
//		 // 更新全预览界面
//		 mPointList.remove(arg2);
//		 for (int i = 0; i < mPointList.size(); i++) {
//		 mPointList.get(i).serNum = Integer.toString(i + 1);
//		 }
//		 mnLastDeep =(mPointList.get(mPointList.size() -
//		 1).deep);
//		 mPointAdapter.notifyDataSetChanged();
//
//		 // 更新中间预览界面
//		 mPrePointList.set(0, mPointList.get(mPointList.size() - 1));
//		 mPrePointAdapter.notifyDataSetChanged();
//
//		 // 更改文件数据,自动模式下重新写文件
//		 FileUtils.FileNewCreate(mFilePath);
//		 FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char)
//		 (mPointList.size())));
//		 for (int i = 0; i < mPointList.size(); i++) {
//		 byte[] point =
//		 TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id),
//		 (mPointList.get(i).deep), mPointList.get(i).data,
//		 Utils.millisToTimeStr(mPointList.get(i).millisTime), type);
//		 // 写入有效点
//		 FileUtils.appendWriteFile(mFilePath, point);
//		 }
//		 }
//
//		 });
//		 builder.show();
//		 }
//		 });

        // 测量方式:起钻或下钻
        RadioGroup measurewayGroup = (RadioGroup) findViewById(R.id.rg_measure_way);
        measurewayGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                findViewById(R.id.jiaozhun).setVisibility(View.VISIBLE);

                if (checkedId == R.id.rb_toup) {// 向上起钻
                    mbToUp = true;
                    Preferences.saveMeasureWayToUp(Point_Collect.this, true);
                    Mceng.setVisibility(View.INVISIBLE);
                    YSceng.setVisibility(View.INVISIBLE);
                    // findViewById(R.id.et_start_deep).setVisibility(
                    // View.VISIBLE);
                    // findViewById(R.id.et_measure_interval).setVisibility(
                    // View.VISIBLE);
                    // findViewById(R.id.qiks2).setVisibility(
                    // View.VISIBLE);
                    // findViewById(R.id.qiks3).setVisibility(
                    // View.VISIBLE);
                    // findViewById(R.id.qiks4).setVisibility(
                    // View.VISIBLE);
                    // findViewById(R.id.qiks1).setVisibility(
                    // View.VISIBLE);
                    if (isJzhun == true || isDialog == true) {
                        type = "校起";
                    } else {
                        type = "/";
                    }

                } else if (checkedId == R.id.rb_todown) {
                    mbToUp = false;
                    isRadio = false;
                    Mceng.setVisibility(View.VISIBLE);
                    YSceng.setVisibility(View.VISIBLE);
                    Mceng.setChecked(true);
                    Preferences.saveMeasureWayToUp(Point_Collect.this, false);

                    // type = "煤";
                    MYSCeng.clearCheck();
                }

            }
        });

        // 顶部标签栏
        pointtab = (RadioGroup) this.findViewById(R.id.rg_pointtab);
        pointtab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_ensure_method) {
                    flipper.setInAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_right_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_right_out));
                    flipper.setDisplayedChild(0);
                    mnTabID = 0;
                } else if (checkedId == R.id.rb_ensure_point) {
                    // 判断是否为空
                    if (mEditStartDeep.getText().toString().equals("")) {
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "请输入起测点孔深！");
                        return;
                    }
                    if (mEditMeasureInterval.getText().toString().equals("")) {
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "请输入选点间隔！");
                        return;
                    } else if (Double.parseDouble(mEditMeasureInterval.getText().toString()) < 0) {
                        //2017-6-26 15:20:12   从<1 更改为 0。    更改后未进行归档交由单旭升进行使用
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "选点间隔第一位不得为0！");
                        return;
                    }
                    if (mnTabID == 0) {
                        flipper.setInAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_left_in));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_left_out));
                    } else if (mnTabID == 2) {
                        flipper.setInAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_right_in));
                        flipper.setOutAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_right_out));
                    }
                    flipper.setDisplayedChild(1);
                    mnTabID = 1;
                } else {
                    // 判断是否为空
                    if (mEditStartDeep.getText().toString().equals("")) {
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "请输入起测点孔深！");
                        return;
                    }
                    if (mEditMeasureInterval.getText().toString().equals("")) {
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "请输入选点间隔！");
                        return;
                    } else if (mEditMeasureInterval.getText().toString().equals("0")) {
                        pointtab.check(R.id.rb_ensure_method);
                        Utils.toast(Point_Collect.this, "选点间隔不得为0！");
                        return;
                    }
                    flipper.setInAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_left_in));
                    flipper.setOutAnimation(AnimationUtils.loadAnimation(Point_Collect.this, R.anim.push_left_out));
                    flipper.setDisplayedChild(2);
                    mnTabID = 2;
                }

            }
        });
        // 人工或手动确定有效点孔深
        RadioGroup deepGroup = (RadioGroup) this.findViewById(R.id.rg_deep_manual_auto);
        deepGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_deepmanual) {// 手动输入有效点孔深
                    mbDeepmanual = true;
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mLvPrePoint.getLayoutParams(); // 取控件textView当前的布局参数
                    linearParams.height = 180;// 控件的高强制设成
                    mLvPrePoint.setLayoutParams(linearParams);
                    // findViewById(R.id.jiaozhun).setVisibility(
                    // View.GONE);

                    findViewById(R.id.et_start_deep).setVisibility(View.INVISIBLE);
                    findViewById(R.id.et_measure_interval).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks3).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks4).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks1).setVisibility(View.INVISIBLE);
//					if (isJzhun == true) {
//						type = "校手";
//
//					} else {
//						type = "手";
//					}

                    findViewById(R.id.ll_deepmanualinput).setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_deepmanualinput).setVisibility(View.VISIBLE);
                    // findViewById(R.id.tv_measure_method).setVisibility(
                    // View.GONE);
                    //
                    // findViewById(R.id.rg_measure_way).setVisibility(View.GONE);
                    // Mceng.setVisibility(View.INVISIBLE);
                    // YSceng.setVisibility(View.INVISIBLE);
                    findViewById(R.id.et_start_deep).setVisibility(View.INVISIBLE);
                    findViewById(R.id.et_measure_interval).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks3).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks4).setVisibility(View.INVISIBLE);
                    findViewById(R.id.qiks1).setVisibility(View.INVISIBLE);

                    Preferences.saveDeepManual(Point_Collect.this, true);
                } else if (checkedId == R.id.rb_deepauto) {
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mLvPrePoint.getLayoutParams(); // 取控件textView当前的布局参数
                    linearParams.height = 240;// 控件的高强制设成
                    mLvPrePoint.setLayoutParams(linearParams);
                    mbDeepmanual = false;
                    // LinearLayout.LayoutParams linearParams
                    // =(LinearLayout.LayoutParams)
                    // mLvPrePoint.getLayoutParams(); //取控件textView当前的布局参数
                    // linearParams.height = 130;// 控件的高强制设成
                    // mLvPrePoint.setLayoutParams(linearParams);
                    findViewById(R.id.jiaozhun).setVisibility(View.VISIBLE);
                    findViewById(R.id.ll_deepmanualinput).setVisibility(View.GONE);
                    findViewById(R.id.tv_deepmanualinput).setVisibility(View.GONE);
                    findViewById(R.id.tv_measure_method).setVisibility(View.VISIBLE);
                    findViewById(R.id.rg_measure_way).setVisibility(View.VISIBLE);
                    Preferences.saveDeepManual(Point_Collect.this, false);
                    findViewById(R.id.et_start_deep).setVisibility(View.VISIBLE);
                    findViewById(R.id.et_measure_interval).setVisibility(View.VISIBLE);
                    findViewById(R.id.qiks2).setVisibility(View.VISIBLE);
                    findViewById(R.id.qiks3).setVisibility(View.VISIBLE);
                    findViewById(R.id.qiks4).setVisibility(View.VISIBLE);
                    findViewById(R.id.qiks1).setVisibility(View.VISIBLE);
                }
            }
        });

        // 确定有效点按钮
        findViewById(R.id.btn_ensure_point).setOnClickListener(new OnClickListener() {
            private int position;

            @Override
            public void onClick(View v) {
                shijianbuchang();
                if (isJzhun == true) {
                    if (Bet_JZ.getText().toString().equals("")) {
                        Utils.toast(Point_Collect.this, "请输入校准孔深！");
                        return;
                    }
                    // 保存起测点孔深,测量间隔
                    Preferences.saveStartDeep(Point_Collect.this, Bet_JZ.getText().toString());
                    Preferences.saveMeasureInterval(Point_Collect.this, mEditMeasureInterval.getText().toString());
                    if (mbDeepmanual) {// 手动输入孔深

                        // 判断是否为空
                        if (mEditStartDeep.getText().toString().equals("")) {
                            Utils.toast(Point_Collect.this, "请输入采集点孔深！");
                            issjjd = false;
                            return;
                        }
                        if (mnLastMillistime == mnNextMillistime) {
                            return;

                        } else {
                            mnLastMillistime = mnNextMillistime;
                        }
                        mnPointSerNum++;
                        float deep = Float.parseFloat(mEditStartDeep.getText().toString());

                        // 增加TYPE
                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, deep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);

                        // 更新显示有效点
                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.parseFloat(mEditStartDeep.getText().toString()), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPointAdapter.notifyDataSetChanged();

                        if (mPrePointList.isEmpty()) mPrePointList.add(
                                // 0,
                                new ValidPointInfo(Integer.toString(mnPointSerNum), Float.parseFloat(mEditStartDeep.getText().toString()), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        else mPrePointList.

                                // set
                                        add(
                                        // 0,
                                        new ValidPointInfo(Integer.toString(mnPointSerNum), Float.parseFloat(mEditStartDeep.getText().toString()), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPrePointAdapter.notifyDataSetChanged();

                        mLvPrePoint.setAdapter(mPointAdapter);
                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                        // 存储更新有效点点数
                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                        // 写入此次有效点
                        FileUtils.appendWriteFile(mFilePath, point);

                    } else {// 自动生成孔深
                        mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                        if (mnLastMillistime == mnNextMillistime) {
                            return;
                        } else {
                            mnLastMillistime = mnNextMillistime + K * T3;
                        }
                        mnPointSerNum++;
                        if (mbToUp) {
                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) + Float.parseFloat(mEditMeasureInterval.getText().toString());
                            Log.e("33333333mnLastDeep", mnLastDeep + "");
                            mnLastDeep = mnLastDeep - Float.parseFloat(mEditMeasureInterval.getText().toString());
                            Log.e("1111111mnLastDeep", mnLastDeep + "");
                            mnLastDeep = getdeep(mnLastDeep);

                        } else {

                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString()) - Float.parseFloat(mEditMeasureInterval.getText().toString());
                            Log.e("4444444444mnLastDeep", mnLastDeep + "");
                            mnLastDeep = mnLastDeep + Float.parseFloat(mEditMeasureInterval.getText().toString());
                            Log.e("1111111mnLastDeep", mnLastDeep + "");
                            mnLastDeep = getdeep(mnLastDeep);

                        }
                        if (mnPointSerNum == 1) {
                            mnLastDeep = Float.parseFloat(Bet_JZ.getText().toString());
                        }
                        if (mnLastDeep < 0) {
                            Utils.toast(Point_Collect.this, "孔深出现负值！");
                        }
                        // 增加TYPE
                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);
                        // 更新显示有效点
                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPointAdapter.notifyDataSetChanged();

                        if (mPrePointList.isEmpty()) mPrePointList.add(
                                // 0,
                                new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        else mPrePointList.
                                // set
                                        add(
                                        // 0,
                                        new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPrePointAdapter.notifyDataSetChanged();

                        mLvPrePoint.setAdapter(mPointAdapter);
                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                        // 存储更新有效点点数
                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                        // 写入此次有效点

                        FileUtils.appendWriteFile(mFilePath, point);

                    }
                    // //////////////// //////////////////////
                } else

                {
                    // 保存起测点孔深,测量间隔
                    Preferences.saveStartDeep(Point_Collect.this, mEditStartDeep.getText().toString());
                    Preferences.saveMeasureInterval(Point_Collect.this, mEditMeasureInterval.getText().toString());
                    if (mbDeepmanual == false) {// 自动生成孔深
                        // if(isshou==true){
                        // mnLastDeep=Float.parseFloat(mEditValidDeep.getText().toString());
                        // }
                        if (mnLastMillistime == mnNextMillistime) {
                            return;
                        } else {
                            mnLastMillistime = mnNextMillistime + K * T3;
                        }
                        mnPointSerNum++;
                        if (mbToUp) {
                            if (isshou == true) {
                                mnLastDeep = Float.parseFloat(mEditValidDeep.getText().toString()) - Float.parseFloat(mEditMeasureInterval.getText().toString());
                            } else {
                                mnLastDeep = mnLastDeep - Float.parseFloat(mEditMeasureInterval.getText().toString());
                            }
                            if (isshou == true) {
                                isshou = false;
                            }
                            mnLastDeep = getdeep(mnLastDeep);

                        } else {
                            Log.e("isshou", isshou + "");
                            if (isshou == true) {
                                mnLastDeep = Float.parseFloat(mEditValidDeep.getText().toString()) + Float.parseFloat(mEditMeasureInterval.getText().toString());
                            } else {
                                mnLastDeep = mnLastDeep + Float.parseFloat(mEditMeasureInterval.getText().toString());
                            }
                            if (isshou == true) {
                                isshou = false;
                            }
                            mnLastDeep = getdeep(mnLastDeep);

                        }
                        if (mnPointSerNum == 1) {
                            mnLastDeep = Float.parseFloat(mEditStartDeep.getText().toString());
                        }
                        if (mnLastDeep < 0) {
                            Utils.toast(Point_Collect.this, "孔深出现负值！");
                        }
                        // 增加TYPE
                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);
                        // 更新显示有效点
                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPointAdapter.notifyDataSetChanged();

                        if (mPrePointList.isEmpty()) mPrePointList.add(
                                // 0,
                                new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        else mPrePointList.
                                // set
                                        add(
                                        // 0,
                                        new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPrePointAdapter.notifyDataSetChanged();

                        mLvPrePoint.setAdapter(mPointAdapter);
                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                        // 存储更新有效点点数
                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                        // 写入此次有效点
                        FileUtils.appendWriteFile(mFilePath, point);
                    } else {

                        // 手动采集
                        // if(isshou==true){
                        // mnLastDeep=Float.parseFloat(mEditValidDeep.getText().toString());
                        // }
                        if (mEditValidDeep.getText().toString().equals("")) {
                            Utils.toast(Point_Collect.this, "请输入采集点孔深！");
                            issjjd = false;
                            return;
                        }
                        Float deep;
                        deep = Float.parseFloat(mEditValidDeep.getText().toString());
                        if (mbToUp) {
                            if (mnLastDeep <= deep) {
                                issjjd = false;
                                AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                builder.setTitle("输入孔深应小于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_JG.setVisibility(View.GONE);
                                        ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                                        ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(true);
                                    }
                                }).setCancelable(false).show();
                                //Utils.toast(Point_Collect.this, "输入孔深应小于末行孔深！");
                                return;
                            }
                        } else {
                            if (mnLastDeep >= deep) {
                                issjjd = false;
                                AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                                builder.setTitle("输入孔深应大于上一行孔深！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tv_JG.setVisibility(View.GONE);
                                        ((TextView) findViewById(R.id.tv_next_time)).setVisibility(View.GONE);
                                        ((Button) findViewById(R.id.btn_ensure_point)).setEnabled(true);
                                    }
                                }).setCancelable(false).show();
                                //Utils.toast(Point_Collect.this, "输入孔深应大于末行孔深！");
                                return;
                            }
                        }
                        if (mnLastMillistime == mnNextMillistime) {
                            return;
                        } else {
                            mnLastMillistime = mnNextMillistime + K * T3;
                        }

                        if (mEditValidDeep.getText().toString().equals("")) {
                            Utils.toast(Point_Collect.this, "请输入采集点孔深！");
                            issjjd = false;
                            return;
                        }


                        if (mnLastDeep < 0) {
                            Utils.toast(Point_Collect.this, "孔深出现负值！");
                        }
                        if (Preferences.getSumPoint(Point_Collect.this) > 0) {
                            mnLastDeep = mPointList.get(mPointList.size() - 1).deep;
                        } else {
                            mnLastDeep = 0;
                        }

                        mnPointSerNum++;
                        Log.e("mnLastDeep", mnLastDeep + "");
                        mnLastDeep = deep;

                        // 增加TYPE
                        byte[] point = TransProtocol.Efficient_Point.getFileData(mnPointNextID, mnLastDeep, Utils.getCurDateString(), Utils.millisToTimeStr(mnLastMillistime), type);
                        // 更新显示有效点
                        mPointList.add(new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPointAdapter.notifyDataSetChanged();

                        if (mPrePointList.isEmpty()) mPrePointList.add(
                                // 0,
                                new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        else mPrePointList.
                                // set
                                        add(
                                        // 0,
                                        new ValidPointInfo(Integer.toString(mnPointSerNum), Float.valueOf(mnLastDeep), Utils.millisToTimeStrColon(mnLastMillistime), Integer.toString(mnPointNextID), Utils.getCurDateString(), mnLastMillistime, type));
                        // Collections.reverse(mPointList); // 逆序排列
                        mPrePointAdapter.notifyDataSetChanged();

                        mLvPrePoint.setAdapter(mPointAdapter);
                        mLvPrePoint.setSelection(Preferences.getSumPoint(Point_Collect.this) - 1);
                        // 存储更新有效点点数
                        FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char) mnPointSerNum));
                        // 写入此次有效点
                        FileUtils.appendWriteFile(mFilePath, point);


                    }
                    //重要

                    list_bhs.add(mnPointNextID);

                }

                // 按钮的标示
                isJing = false;
                isDialog = false;
                isButton = false;
                isRadio = false;
                isJzhun = false;
                issjjd = true;
                // ColorStr="0";
                Preferences.saveColour(Point_Collect.this, ColorStr);
                MYSCeng.clearCheck();
                ((Button) findViewById(R.id.btn_ensure_point)).setVisibility(View.INVISIBLE);
                for (int ilist_bhs = 0; ilist_bhs <= list_bhs.size(); ilist_bhs++) {
                    Preferences.saveIlist_bhs(Point_Collect.this, ilist_bhs);
                }
                Preferences.saveSumPoint(Point_Collect.this, mnPointSerNum);
                Preferences.saveOAuth(Point_Collect.this, list_bhs);
                Log.d("mylog", "mnPointNextID = " + mnPointNextID);
                if (mbDeepmanual) {
                    fangshi = "手动";
                } else {
                    if (mbToUp) {
                        fangshi = "起钻";
                        type = "/";
                    } else {
                        fangshi = "下钻";

                    }

                }
                Log.e("fangshi", fangshi);

                mEditStartDeep.setFocusable(false);
                mEditMeasureInterval.setFocusable(false);
            }

        });

        // 退出按钮 zgl 2014-11-13 11:55:26修改
        findViewById(R.id.btn_quit_pointcollect).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 复制配置文件
                try {
                    FileUtils.copyFile(new File("/data/data/com.android.antiexplosionphone/shared_prefs/SET_START.xml"), new File(ConstantDef.DIRECTORY_PATH + "SET_START.txt"));
                    FileUtils.copyFile(new File("/data/data/com.android.antiexplosionphone/shared_prefs/POINT_COLLECT.xml"), new File(ConstantDef.DIRECTORY_PATH + "POINT_COLLECT.txt"));
                    FileUtils.copyFile(new File(mFilePath), new File(ConstantDef.DIRECTORY_PATH + strname + ".mp4"));
                } catch (IOException e) {
                    Log.e("eeeeee:","复制文件错误："+e);
                }

                Log.e("mp4 格式文件：", mFilePath);
                Log.e("mFilePathmFilePath", ConstantDef.DIRECTORY_PATH + strname + ".mp4");
                AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                builder.setTitle("接续功能提示").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isJie = true;
                        for (int ilist_bhs = 0; ilist_bhs <= list_bhs.size(); ilist_bhs++) {
                        }
                        // 退出界面
                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        uris.add(Uri.fromFile(new File(ConstantDef.DIRECTORY_PATH + "SET_START.txt")));
                        uris.add(Uri.fromFile(new File(ConstantDef.DIRECTORY_PATH + "POINT_COLLECT.txt")));
//                        uris.add(Uri.fromFile(new File(ConstantDef.DIRECTORY_PATH + "SOFT_CONFIG.txt")));
                        uris.add(Uri.fromFile(new File(ConstantDef.DIRECTORY_PATH + strname + ".mp4")));
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                        // intent.setType("application/octet-stream");
                        intent.setType("*/*"); //
                        // intent.setClassName("com.android.bluetooth"
                        // ,
                        // "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                        intent.putExtra(Intent.EXTRA_STREAM, uris);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method
                        // stub
                    }
                }).show();
            }
        });

        // 预览界面退出按钮
        findViewById(R.id.btn_quit_pointpreview).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                builder.setTitle("结束选点,后续测量将不能继续自动选点，您确定结束选点？").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Point_Collect.this);
                        builder.setTitle("请再次确认是否结束选点并返回主页。").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,

                                                int which) {
                                // 退出界面
                                Preferences.saveStep(Point_Collect.this, 99);
                                Point_Collect.this.finish();
                                // 强制打开蓝牙
                                BluetoothAdapter.getDefaultAdapter().enable();
                                for (int ilist_bhs = 0; ilist_bhs <= list_bhs.size(); ilist_bhs++) {
                                    Preferences.saveIlist_bhs(Point_Collect.this, ilist_bhs);
                                }
                                Preferences.saveOAuth(Point_Collect.this, list_bhs);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO
                                // Auto-generated
                                // method
                                // stub
                            }
                        }).show();

                        // // 退出界面
                        // Preferences.saveStep(
                        // Point_Collect.this, 99);
                        // Point_Collect.this.finish();
                        // // 强制打开蓝牙
                        // BluetoothAdapter
                        // .getDefaultAdapter()
                        // .enable();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method
                        // stub
                    }
                }).show();
            }
        });

        // btnQuit.setOnTouchListener(new OnTouchListener() {
        // int[] temp = new int[] { 0, 0 };
        // boolean isClick = false;
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // int eventaction = event.getAction();
        //
        // int x = (int) event.getRawX();
        // int y = (int) event.getRawY();
        //
        // switch (eventaction) {
        //
        // case MotionEvent.ACTION_DOWN: // touch down so check if the
        // temp[0] = (int) event.getX();
        // temp[1] = y - v.getTop();
        // isClick = false;
        // break;
        //
        // case MotionEvent.ACTION_MOVE: // touch drag with the ball
        // v.layout(x - temp[0], y - temp[1], x + v.getWidth() - temp[0], y -
        // temp[1] + v.getHeight());
        // break;
        //
        // case MotionEvent.ACTION_UP:
        // break;
        // }
        //
        // return false;
        // }
        // });

        // 关闭蓝牙
        BluetoothAdapter.getDefaultAdapter().disable();
        wifiManager.setWifiEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                // AlertDialog.Builder builder = new AlertDialog.Builder(
                // Point_Collect.this);
                // builder.setTitle("您确定要退出并返回主页？")
                // .setIcon(android.R.drawable.ic_dialog_alert)
                // .setPositiveButton("确定",
                // new DialogInterface.OnClickListener() {
                //
                // @Override
                // public void onClick(DialogInterface dialog,
                // int which) {
                // // 退出界面
                // Point_Collect.this.finish();
                // }
                // })
                // .setNegativeButton("取消",
                // new DialogInterface.OnClickListener() {
                //
                // @Override
                // public void onClick(DialogInterface dialog,
                // int which) {
                // // TODO Auto-generated method stub
                // }
                // }).show();
                return true;
            }
            case KeyEvent.KEYCODE_HOME:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        releaseWakeLock();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 120) {// 向左滑动

            // 判断是否为空
            if (mEditStartDeep.getText().toString().equals("")) {
                pointtab.check(R.id.rb_ensure_method);
                Utils.toast(Point_Collect.this, "请输入起测点孔深！");
                return false;
            }
            if (mEditMeasureInterval.getText().toString().equals("")) {
                pointtab.check(R.id.rb_ensure_method);
                Utils.toast(Point_Collect.this, "请输入选点间隔！");
                return false;
            }
            if (mnTabID < 2) {
                mnTabID++;
            } else {
                mnTabID = 2;
                return false;
            }
            flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
            if (mnTabID == 1) {
                flipper.setDisplayedChild(1);
                pointtab.check(R.id.rb_ensure_point);
            } else if (mnTabID == 2) {
                flipper.setDisplayedChild(2);
                pointtab.check(R.id.rb_point_preview);
            }

        } else if (e2.getX() - e1.getX() > 120) {// 向右滑动

            if (mnTabID > 0) {
                mnTabID--;
            } else {
                mnTabID = 0;
                return false;
            }
            flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
            flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
            if (mnTabID == 0) {
                flipper.setDisplayedChild(0);
                pointtab.check(R.id.rb_ensure_method);
            } else if (mnTabID == 1) {
                flipper.setDisplayedChild(1);
                pointtab.check(R.id.rb_ensure_point);
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    // // 申请设备电源锁
    public void acquireWakeLock() {
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NotificationService");
            if (null != mWakeLock) {
                mWakeLock.acquire();
            }
        }
    }

    // 释放设备电源锁
    public void releaseWakeLock() {
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    // 现场恢复显示更新数据
    private void recover() {
        findViewById(R.id.btn_ensure_point).setVisibility(View.INVISIBLE);
        istime = true;
        string f;
        mLvPrePoint = (ListView) findViewById(R.id.lv_pre_valid_point);
        mPrePointAdapter = new ValidPointAdapter(this, mPrePointList);
        mLvPrePoint.setAdapter(mPrePointAdapter);
        list_bhs = Preferences.readOAuth();
        if (list_bhs == null) {
            Log.e("list_bhs", Preferences.getIlist_bhs(this)+"");
            list_bhs = new ArrayList<Integer>();
            list_bhs.add(Preferences.getIlist_bhs(this));
        }
        Log.e("list_bhs", Preferences.readOAuth() + "");
        mLvPoint = (ListView) findViewById(R.id.lv_valid_point);
        mPointAdapter = new ValidPointAdapter(this, mPointList);
        mLvPoint.setAdapter(mPointAdapter);

        sumPoint = Preferences.getSumPoint(Point_Collect.this);
        mStructEnsurePoint = new Struct_Ensure_Point[sumPoint];
        if (("P").equals(Preferences.getBenString(this))) {
            String mYXFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;
            for (int i = 0; i < sumPoint; i++) {
                byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + i * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                if (bytesEnsure != null) {
                    mStructEnsurePoint[i] = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                    // 更新显示有效点
                    mPointList.add(new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    // Log.e("testlog","typelength="+mStructEnsurePoint[i].type.length());
                    // "岩"));
                    Log.d("mylog", "id=" + mStructEnsurePoint[i].id + "  deep=" + mStructEnsurePoint[i].deep + "  time=" + mStructEnsurePoint[i].time);
                    // mPointAdapter = new ValidPointAdapter(this, mPointList);
                    // Collections.reverse(mPointList); // 逆序排列
                    mPointAdapter.notifyDataSetChanged();

                    if (mPrePointList.isEmpty()) mPrePointList.add(
                            // 0,
                            new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    else mPrePointList.add(
                            // 0,
                            new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    // mPrePointAdapter = new ValidPointAdapter(this,
                    // mPrePointList);
                    // mLvPrePoint.setAdapter(mPrePointAdapter);
                    // Collections.reverse(mPointList); // 逆序排列
                    mPrePointAdapter.notifyDataSetChanged();
                }

            }
        } else {
            String mYXFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;
            for (int i = 0; i < sumPoint; i++) {
                byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + i * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                if (bytesEnsure != null) {
                    mStructEnsurePoint[i] = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                    // 更新显示有效点
                    mPointList.add(new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    // Log.e("testlog","typelength="+mStructEnsurePoint[i].type.length());
                    // "岩"));
                    Log.d("mylog", "id=" + mStructEnsurePoint[i].id + "  deep=" + mStructEnsurePoint[i].deep + "  time=" + mStructEnsurePoint[i].time);
                    // mPointAdapter = new ValidPointAdapter(this, mPointList);
                    // Collections.reverse(mPointList); // 逆序排列
                    mPointAdapter.notifyDataSetChanged();

                    if (mPrePointList.isEmpty()) mPrePointList.add(
                            // 0,
                            new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    else mPrePointList.add(
                            // 0,
                            new ValidPointInfo(Integer.toString(i + 1), Float.valueOf(mStructEnsurePoint[i].deep), Utils.strToTime(mStructEnsurePoint[i].time), Integer.toString(mStructEnsurePoint[i].id), mStructEnsurePoint[i].time, Long.parseLong(mStructEnsurePoint[i].time), mStructEnsurePoint[i].type.trim()));
                    // mPrePointAdapter = new ValidPointAdapter(this,
                    // mPrePointList);
                    // mLvPrePoint.setAdapter(mPrePointAdapter);
                    // Collections.reverse(mPointList); // 逆序排列
//				String	strfangshi;
//					if(mStructEnsurePoint[i].type.trim()=="/"||mStructEnsurePoint[i].type.trim()=="校起"){
//						strfangshi="起钻";
//					}
//					if(mStructEnsurePoint[i].type.trim()=="煤"||mStructEnsurePoint[i].type.trim()=="岩"||mStructEnsurePoint[i].type.trim()=="校煤"||mStructEnsurePoint[i].type.trim()=="校岩"){
//						strfangshi="钻进";
//					}
//					if(mStructEnsurePoint[i].type.trim()=="手煤"||mStructEnsurePoint[i].type.trim()=="手岩"){
//						strfangshi="钻进";
//					}
//					if(mStructEnsurePoint[i].type.trim()=="手"){
//						strfangshi="起钻";
//					}

                    mPrePointAdapter.notifyDataSetChanged();
                }

            }
        }

        mLvPrePoint.setSelection(sumPoint - 1);
        mLvPoint.setSelection(sumPoint - 1);
        // mPointAdapter.notifyDataSetInvalidated();
        // mPrePointAdapter.notifyDataSetInvalidated();

        mnPointSerNum = sumPoint;
        if (sumPoint != 0 && mStructEnsurePoint != null) {
            mnLastDeep = Float.parseFloat(mStructEnsurePoint[sumPoint - 1].deep);
        }

    }

    /**
     * 发送接续数据对话框 zgl
     */
     private void jiexuDialog(String info) {
    //
     AlertDialog.Builder builder = new AlertDialog.Builder(
     Point_Collect.this);
     builder.setTitle(info)
     .setIcon(android.R.drawable.ic_dialog_alert)
     .setPositiveButton("确定", new DialogInterface.OnClickListener() {
    // @Override
     public void onClick(DialogInterface dialog, int which) {
    // mBtAdapter.enable();
    // // 复制配置文件
    // try {
    // FileUtils
    // .copyFile(
    // new File(
    // "/data/data/com.android.antiexplosionphone/shared_prefs/SET_START.xml"),
    // new File(ConstantDef.DIRECTORY_PATH
    // + "SET_START.txt"));
    // FileUtils
    // .copyFile(
    // new File(
    // "/data/data/com.android.antiexplosionphone/shared_prefs/POINT_COLLECT.xml"),
    // new File(ConstantDef.DIRECTORY_PATH
    // + "POINT_COLLECT.txt"));
    // FileUtils
    // .copyFile(
    // new File(
    // "/data/data/com.android.antiexplosionphone/shared_prefs/SOFT_CONFIG.xml"),
    // new File(ConstantDef.DIRECTORY_PATH
    // + "SOFT_CONFIG.txt"));
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
//     }
    //
    // ArrayList<Uri> uris = new ArrayList<Uri>();
    // uris.add(Uri.fromFile(new File(
    // ConstantDef.DIRECTORY_PATH + "SET_START.txt")));
    // uris.add(Uri.fromFile(new File(
    // ConstantDef.DIRECTORY_PATH
    // + "POINT_COLLECT.txt")));
    // uris.add(Uri
    // .fromFile(new File(ConstantDef.DIRECTORY_PATH
    // + "SOFT_CONFIG.txt")));
    // uris.add(Uri.fromFile(new File(mFilePath)));
    // Intent intent = new Intent();
    // intent.setAction(Intent.ACTION_SEND_MULTIPLE);
    // // intent.setType("application/octet-stream");
    // intent.setType("*/*"); //
    // // intent.setClassName("com.android.bluetooth" ,
    // // "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
    // intent.putExtra(Intent.EXTRA_STREAM, uris);
    // startActivity(intent);
     }
     })
//     .setNegativeButton("否", new DialogInterface.OnClickListener() {
//     @Override
//     public void onClick(DialogInterface dialog, int which) {
//     // TODO Auto-generated method stub
//     powervalue -= 5;
//     }
//     })
             .setCancelable(false).show();
     }
    private float getdeep(float f) {
        float f1 = 1;
        f1 = Float.parseFloat(df2.format(f));

        return f1;
    }

//	private void initPopupWindow(final int position) {
//		LayoutInflater inflater = LayoutInflater.from(this);
//		// 引入窗口配置文件
//		View view = inflater.inflate(R.layout.popupwindow, null);
//		// shanchu=(Button) findViewById(R.id.button2);
//		// 创建PopupWindow对象
//		final PopupWindow pop = new PopupWindow(view,
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
//		// 需要设置一下此参数，点击外边可消失
//		pop.setBackgroundDrawable(new BitmapDrawable());
//		// 设置点击窗口外边窗口消失
//		pop.setOutsideTouchable(true);
//		// 设置此参数获得焦点，否则无法点击
//		pop.setFocusable(true);
//		pop.showAtLocation(this.findViewById(R.id.itme_main), Gravity.CENTER,
//				0, 0);
//		((Button) view.findViewById(R.id.button1))
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						String bianhao = " ";
//
//						// // 更改文件数据,自动模式下重新写文件
//						// FileUtils.FileNewCreate(mFilePath);
//						// FileUtils.RandomWriteFile(mFilePath, 0,
//						// Utils.getLittleBytes((char)
//						// (mPointList.size())));
//
//						// for (int i = 0; i < mPointList.size(); i++) {
//						// //sign="1";
//						//
//						// byte[] point =
//						// TransProtocol.Efficient_Point.getFileData(
//						// Integer.parseInt(mPointList.get(i).id),
//						// Float.parseFloat(mPointList.get(i).deep),
//						// mPointList.get(i).data,
//						// Utils.millisToTimeStr(mPointList
//						// .get(i).millisTime), type);
//						// // 写入有效点
//						//
//						// FileUtils.appendWriteFile(mFilePath, point);
//						//
//						//
//						// }
//						getViewByPosition(position, mLvPoint).findViewById(
//								R.id.itme_main).setBackgroundColor(Color.RED);
//						bianhao = String.valueOf(mPointList.get(position).id);
//						listcolor.add(bianhao);
//						Log.e("String.valueOf(mPointList.get(i).id)", listcolor
//								+ "");
//
//						list.add(position);
//
//						Log.e("position", list + "");
//						// ColorStr = "1";
//						// Preferences.saveColour(Point_Collect.this, ColorStr);
//						pop.dismiss();
//					}
//				});
//		((Button) view.findViewById(R.id.button2))
//				.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// listcolor = new ArrayList<String>();
//						// 更改文件数据,自动模式下重新写文件
//						// FileUtils.FileNewCreate(mFilePath);
//						// FileUtils.RandomWriteFile(mFilePath, 0,
//						// Utils.getLittleBytes((char)
//						// (mPointList.size())));
//						// for (int i = 0; i < mPointList.size(); i++) {
//						// byte[] point =
//						// TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id),
//						// Float.parseFloat(mPointList.get(i).deep),
//						// mPointList.get(i).data,
//						// Utils.millisToTimeStr(mPointList.get(i).millisTime),
//						// type);
//						// 写入有效点
//						// if(i==position){
//						// Log.e("listcolor", mPointList.get(i).id);
//						// ColorStr="1";
//						// Preferences.saveColour(Point_Collect.this, ColorStr);
//						// }
//						// }
//						getViewByPosition(position, mLvPoint).findViewById(
//								R.id.itme_main).setBackgroundColor(Color.WHITE);
//
//						list.add(position);
//						listcolor.add(String.valueOf(position + 1));
//						Log.e("position", list + "");
//						ColorStr = "1";
//						Preferences.saveColour(Point_Collect.this, ColorStr);
//						pop.dismiss();
//					}
//				});
//		// Bun_shanchu.setOnClickListener(new OnClickListener() {
//		//
//		// @Override
//		// public void onClick(View v) {
//		//
//		// // 删除有效点
//		// mnPointSerNum--;
//		// if (mnPointSerNum == 0) {
//		// mnPointSerNum++;
//		// Utils.toast(Point_Collect.this, "删除失败，请保留一个有效点！");
//		// return;
//		// }
//		// if(mPointList.size()>0){
//		// // 更新全预览界面
//		// mPointList.remove(mPointList.size()-1);
//		//
//		// }
//		//
//		// for (int i = 0; i < mPointList.size(); i++) {
//		// mPointList.get(i).serNum = Integer.toString(i + 1);
//		// }
//		// mnLastDeep = Float.parseFloat(mPointList.get(mPointList.size() -
//		// 1).deep);
//		// mPointAdapter.notifyDataSetChanged();
//		//
//		// // 更新中间预览界面
//		// mPrePointList.set(0, mPointList.get(mPointList.size() - 1));
//		// mPrePointAdapter.notifyDataSetChanged();
//		//
//		// // 更改文件数据,自动模式下重新写文件
//		// FileUtils.FileNewCreate(mFilePath);
//		// FileUtils.RandomWriteFile(mFilePath, 0, Utils.getLittleBytes((char)
//		// (mPointList.size())));
//		// for (int i = 0; i < mPointList.size(); i++) {
//		// byte[] point =
//		// TransProtocol.Efficient_Point.getFileData(Integer.parseInt(mPointList.get(i).id),
//		// Float.parseFloat(mPointList.get(i).deep), mPointList.get(i).data,
//		// Utils.millisToTimeStr(mPointList.get(i).millisTime), type);
//		// // 写入有效点
//		// getViewByPosition(position,
//		// mLvPoint).findViewById(R.id.itme_main).setBackgroundColor(Color.WHITE);
//		// FileUtils.appendWriteFile(mFilePath, point);
//		// }
//		//
//		// mPointAdapter.notifyDataSetChanged();
//		// mPrePointAdapter.notifyDataSetChanged();
//		// pop.dismiss();
//		// }
//		// });
//	}

    int K = 0;

    public void shijianbuchang() {
//			for (int k = 0; (RemainTime/1000+k*(T3/1000))>6; k++) {
//				isgongshi=true;
//				Log.e("mnPointNextID1896", mnPointNextID1+"");
//				Log.e("mnNextMillistime1987654", mnNextMillistime1+"");
//				mnPointNextID1=mnPointNextID+k;
//				K++;
//				Log.e("mnNextMillikkkkkkkkkkkkkkkkkkkkk", K+"");
//			}.
        if (RemainTime >= 5) {
            K = 0;
        } else {
            if (RemainTime + (T3 / 1000) >= 5) {
                K = 1;
            } else {
                if (RemainTime + 2 * (T3 / 1000) >= 5) {
                    K = 2;
                }
                //else {
//					if (RemainTime+3*(T3/1000)>=5) {
//						K=3;
//					}
                //}

            }

        }
        mnPointNextID1 += 1;
//		Log.e("mnNextMillikkkkkkk11111111111111", K+"");
        mnNextMillistime1 = RemainTime + K * (T3 / 1000) + 3;
//		mnNextMillistime1=mnNextMillistime1*1000;
        Log.e("mnNextMillistime1234321", mnNextMillistime1 + "");
        Log.e("RemainTime12344321", RemainTime + "");
    }

    private void batteryLevel() {

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {

                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                // 获得总电量
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;

                }
                returnlevel = level;

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
