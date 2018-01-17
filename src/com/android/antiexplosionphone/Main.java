package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.antiexplosionphone.FileAdapter.ViewHolder;
import com.android.bean.SetValueBean;
import com.android.bluetooth.BluetoothClientService;
import com.android.bluetooth.BluetoothTools;
import com.android.help.Help;
import com.android.utils.ConstantDef;
import com.android.utils.CreateUserPopWin;
import com.android.utils.FileUtils;
import com.android.utils.POINT_COLLECT;
import com.android.utils.Preferences;
import com.android.utils.SET_START;
import com.android.utils.SOFT_CONFIG;
import com.android.utils.SharedPreferencesHelper;
import com.android.utils.Utils;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 主界面
 *
 * @author TY
 */
public class Main extends Activity implements OnClickListener {

    private static final String TAG = Main.class.getSimpleName();
    private String btPath = Environment.getExternalStorageDirectory() + "/bluetooth/";// 蓝牙接收数据存放位置
    private String dataPath = ConstantDef.DIRECTORY_PATH;// 测量数据存放位置
    private int times = 0;
    private Boolean isgui = false;
    private Boolean isshou = false;
    private int ischeng;
    public Dialog dialogs;
    Dialog dialog;

    /**
     * 接收蓝牙连接成功广播
     */
    private BroadcastReceiver bluetoothConnReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Log.e("action", action);
            if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                ConstantDef.CUR_BLUETOOTH_STATUS = BluetoothTools.ACTION_CONNECT_SUCCESS;
                // 蓝牙连接成功
                ischeng = 1;
                Log.e("", "123434567890123434567890");
                findViewById(R.id.btn_track_measure).setEnabled(true);
                findViewById(R.id.btn_data_receive).setEnabled(true);
                Log.v(TAG, "" + isBtndatareceive);
                System.out.println("蓝牙设备连接成功！");
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("isBtndatareceive", isBtndatareceive + "");
                if (isshou == true && isBtndatareceive) {
                    Log.e("isBtndatareceive2", isBtndatareceive + "");
                    isBtndatareceive = false;
                    isshou = false;
                    // 跳转数据接收模块
                    Intent intent_rev = new Intent();
                    //Utils.toast(Main.this, "蓝牙设备连接成功！");
                    intent_rev.setClass(Main.this, Data_Receive.class);
                    Main.this.startActivity(intent_rev);
                } else if (isgui == true && isBtndatareceive) {
                    isBtndatareceive = false;
                    isgui = false;
                    // 跳转数据接收模块
                    //Utils.toast(Main.this, "蓝牙设备连接成功！");
                    Intent intent_self = new Intent();
                    intent_self.setClass(Main.this, Self_Detection.class);
                    Main.this.startActivity(intent_self);
                }
            } else if (BluetoothTools.ACTION_CONNECT_ERROR.equals(action)) {
                ConstantDef.CUR_BLUETOOTH_STATUS = BluetoothTools.ACTION_CONNECT_ERROR;
                // 蓝牙连接失败
//				if (isBtndatareceive == true) {
                ischeng = 2;

                //Utils.toast(Main.this, "蓝牙设备连接失败！");
                Log.e("", "123434567890");
                Log.v(TAG, "蓝牙设备连接失败！");
                mProgressDialog.dismiss();
                isBtndatareceive = true;
            } else if (BluetoothTools.ACTION_NOT_FOUND_DEVICE.equals(action)) {
                ConstantDef.CUR_BLUETOOTH_STATUS = BluetoothTools.ACTION_NOT_FOUND_DEVICE;
                // 未搜索到蓝牙设备
                //Utils.toast(Main.this, "未搜索到蓝牙设备！");
                Log.v(TAG, "未搜索到蓝牙设备！");
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isBtndatareceive = false;
            } else if (BluetoothTools.ACTION_PAIR_DEVICE_FAILED.equals(action)) {
                ConstantDef.CUR_BLUETOOTH_STATUS = BluetoothTools.ACTION_PAIR_DEVICE_FAILED;
                // 蓝牙设备配对失败
                Utils.toast(Main.this, "未搜索到蓝牙设备");
                Log.v(TAG, "未搜索到蓝牙设备");
                mProgressDialog.dismiss();
                isBtndatareceive = false;
            } else
                // if (BluetoothTools.ACTION_PAIR_DEVICE_START.equals(action)) {
                // mProgressDialog = ProgressDialog.show(Main.this, "机芯自动配对中...",
                // "请稍等...", true, false);
                // }

                if ((BluetoothTools.ACTION_CONNECT_ERROR.equals(action) || BluetoothTools.ACTION_PAIR_DEVICE_FAILED.equals(action)) && times < 3 && times != 0) {
                    //startTimer();
                    times++;
                    isBtndatareceive = true;
                }

            if (ischeng == 1 && ischeng == 2) {
                Log.e("", "00000000000000");
                Utils.toast(Main.this, "蓝牙设备连接成功！");
            } else if (ischeng == 2) {
                if (ischeng != 1) {
                    //Utils.toast(Main.this, "蓝牙设备连接失败！");
                }
            }
        }

    };

    private boolean isBtndatareceive = false;
    private ProgressDialog mProgressDialog;
    private final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private int mDelayTime = 0;

    private int step;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {

            mDelayTime++;
            if (mDelayTime == 4) {
                // 开启后台service
                Intent service = new Intent(Main.this, BluetoothClientService.class);
                startService(service);
            } else if (mDelayTime == 50) {
                // 蓝牙连接50s无响应，隐藏配对进度对话框
                stopTimer();
                // Utils.toast(Main.this, "搜索蓝牙设备配对失败！");
                mProgressDialog.dismiss();
            } else {
                mHandler.postDelayed(this, 1000);
            }
        }
    };

    private void startTimer() {
        mHandler.postDelayed(runnable, 1000);
    }

    private void stopTimer() {
        mHandler.removeCallbacks(runnable);
    }

    // 最小可用容量10M
    private final long LIMIT_AVAILABLESIZE = 10 * 1024 * 1024;
    // 是否空间不足退出
    private boolean isForceQuit = false;
    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    isForceQuit = false;
                    // 判断SD容量是否不足
                    if (getSDAvailableSize() < LIMIT_AVAILABLESIZE) {
                        //showFileBrowser();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                        builder.setTitle("内存空间不足请清理！").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(Main.this, Data_Query.class);
                                Main.this.startActivity(intent);
                            }
                        }).setCancelable(false).show();

                    } else {
                        init();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Preferences.setContext(this);
        File f = new File(Environment.getExternalStorageDirectory() + "/HKCX-SJ/");
        if (!f.exists()) {
            f.mkdirs();
        }
        msgHandler.sendEmptyMessageDelayed(0, 100);
        //	jingshiinfo("正在打开蓝牙...",true);
//		 dialogs=new ProgressDialog(Main.this);
//	     ((ProgressDialog) dialogs).setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	     ((AlertDialog) dialogs).setMessage("正在打开蓝牙...");
//	   dialogs.show();
//	   new Thread(){
//			public void run() {
//				try {
//					sleep(5000);
//					 
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				fhandler.sendEmptyMessage(0x1);
//		}
//	   }.start();

    }

//	protected void onStart() {
//		 dialogs=new ProgressDialog(Main.this);
//	     ((ProgressDialog) dialogs).setProgressStyle(ProgressDialog.STYLE_SPINNER);
//	     ((AlertDialog) dialogs).setMessage("正在打开蓝牙...");
//	   dialogs.show();
//	   new Thread(){
//			public void run() {
//				try {
//					sleep(5000);
//					 
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				fhandler.sendEmptyMessage(0x1);
//		}
//	   }.start();
//	   super.onStart();
//	}
//	 Handler fhandler=new Handler(){
//			public void handleMessage(android.os.Message msg) {
//				dialogs.dismiss();
//				
//		}
//		};

    /**
     * 初始化界面及服务相关
     */
    private void init() {
        SharedPreferencesHelper SPconfig = new SharedPreferencesHelper(this, "SET_START");
        // 强制打开蓝牙
        BluetoothAdapter.getDefaultAdapter().enable();
        findViewById(R.id.btn_track_measure).setOnClickListener(this);
        findViewById(R.id.btn_data_receive).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_leaning).setOnClickListener(Main.this);
        findViewById(R.id.btn_dataquery).setOnClickListener(Main.this);
        findViewById(R.id.btn_exit).setOnClickListener(this);
        findViewById(R.id.btn_help).setOnClickListener(this);
        findViewById(R.id.btn_当地参数).setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_ERROR);
        intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_DEVICE);
        intentFilter.addAction(BluetoothTools.ACTION_PAIR_DEVICE_FAILED);
        intentFilter.addAction(BluetoothTools.ACTION_PAIR_DEVICE_START);
        registerReceiver(bluetoothConnReceiver, intentFilter);

        // findViewById(R.id.btn_track_measure).setEnabled(true);//测试用
        // findViewById(R.id.btn_data_receive).setEnabled(true);//测试用
        // 获取是否异常退出的步骤标记
        step = Preferences.getStep(this);
        // 是否现场恢复，是否接续，是否正常工作
        if (new File(btPath).exists() && (new File(btPath)).listFiles().length > 0) {
            jieXu();
        } else {
            if (step != 99) {
                huifuDialog();
            } else {
                //startTimer();
                // mProgressDialog = ProgressDialog.show(Main.this,
                // "机芯自动配对中...",
                // "请稍等...", true, false);
            }
        }
        // ///ZN
        if ("F".equals(Preferences.getFangString(this))) {
            if (("P").equals(Preferences.getBenString(this))) {
                // 初始化存储路径为上次记录
                ConstantDef.FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                Log.e("aaaaaaaaaaa", ConstantDef.FILE_PATH);
            } else {
                // 初始化存储路径为上次记录
                ConstantDef.FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                Log.e("bbbbbbbbbbbbbbbbbbbbbb", ConstantDef.FILE_PATH);
            }
        } else {
            if (("P").equals(Preferences.getBenString(this))) {
                // 初始化存储路径为上次记录
                ConstantDef.FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                Log.e("cccccccccccc", ConstantDef.FILE_PATH);
            } else {
                // 初始化存储路径为上次记录
                ConstantDef.FILE_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                Log.e("dddddddddddddddddddddds", ConstantDef.FILE_PATH);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_track_measure: {
                // 强制打开蓝牙
                // BluetoothAdapter.getDefaultAdapter().enable();

                isgui = true;
                if (ConstantDef.CUR_BLUETOOTH_STATUS != null && ConstantDef.CUR_BLUETOOTH_STATUS.equals(BluetoothTools.ACTION_CONNECT_SUCCESS)) {
                    isBtndatareceive = true;
                    intent.setClass(this, Self_Detection.class);
                    this.startActivity(intent);
                } else {
                    mBtAdapter.enable();
                    if (mBtAdapter.isDiscovering()) {
                        mBtAdapter.cancelDiscovery();
                    }
                    mBtAdapter.startDiscovery();
                    startTimer();
                    mProgressDialog = ProgressDialog.show(Main.this, "正在连接请稍等...", "请稍等...", true, false);
                    isBtndatareceive = true;
                }
                break;
            }
            case R.id.btn_data_receive: {
                isshou = true;
                if (ConstantDef.CUR_BLUETOOTH_STATUS != null && ConstantDef.CUR_BLUETOOTH_STATUS.equals(BluetoothTools.ACTION_CONNECT_SUCCESS)) {
                    isBtndatareceive = true;
                    intent.setClass(this, Data_Receive.class);
                    this.startActivity(intent);
                } else {
                    mBtAdapter.enable();
                    if (mBtAdapter.isDiscovering()) {
                        mBtAdapter.cancelDiscovery();
                    }
                    mBtAdapter.startDiscovery();
                    startTimer();
                    mProgressDialog = ProgressDialog.show(Main.this, "正在连接请稍等...", "请稍等...", true, false);
                    isBtndatareceive = true;

                }

                break;
            }
            case R.id.btn_setting:
                intent.setClass(this, Soft_Config.class);
                this.startActivity(intent);
                break;

            case R.id.btn_leaning:
                // // 强制打开蓝牙
                // BluetoothAdapter.getDefaultAdapter().enable();
                intent.setClass(this, Leaning_PipeAdapter.class);
                startActivity(intent);
                break;

            case R.id.btn_dataquery:
                               intent.setClass(this, Data_Query.class);
                startActivity(intent);
                break;

            case R.id.btn_help:

                intent.setClass(this, Help.class);
                startActivity(intent);
                break;
            case R.id.btn_当地参数:
                showEditPopWin();

                break;
            case R.id.btn_save_pop:
                SimpleDateFormat df = new SimpleDateFormat("yyMMdd");//设置日期格式
                String data = df.format(new Date());
                String mobile1 = createUserPopWin.getText_mobile().getText().toString().trim();
                if (data.equals(mobile1)) {
                    createUserPopWin.dismiss();
                    intent.setClass(this,Set_Parameter.class);
                    Main.this.startActivity(intent);
                }else {
                    createUserPopWin.dismiss();
                    Utils.toast(Main.this, "密码错误！");
                }
                break;
            case R.id.btn_exit:
                quitDialog();
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            case KeyEvent.KEYCODE_HOME:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // @Override
    // public void onAttachedToWindow() {
    // this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
    // super.onAttachedToWindow();
    // }

    @Override
    protected void onDestroy() {
        if (!isForceQuit) {
            // 关闭后台service
            Intent service = new Intent(this, BluetoothClientService.class);
            stopService(service);
            // 注销receiver
            unregisterReceiver(bluetoothConnReceiver);
            // 强制关闭蓝牙
            // BluetoothAdapter.getDefaultAdapter().disable();
        }
        super.onDestroy();
    }

    /**
     * 退出提示框
     */
    private void quitDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("您确定要退出？").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isForceQuit) {
                    filePopupWindow.dismiss();
                }
                // 强制关闭蓝牙
                BluetoothAdapter.getDefaultAdapter().disable();
                // 退出界面
                //Main.this.finish();
                System.exit(0);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        }).show();
    }

    private PopupWindow filePopupWindow;
    private int checkNum; // 记录选中的条目数量
    private TextView tv_showNum; // 用于显示选中的条目数量
    private FileAdapter mFileAdapter;
    private ArrayList<HashMap<String, String>> itemList;

    /**
     * 显示文件列表
     */
    private void showFileBrowser() {
        if (filePopupWindow != null && filePopupWindow.isShowing()) return;

        // DisplayMetrics dm = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
        // int screenHeight = dm.heightPixels * 6 / 7;
        // int screenWidth = dm.widthPixels * 6 / 7;

        LayoutInflater factory = LayoutInflater.from(Main.this);
        View view = factory.inflate(R.layout.filebrowser, null);
        view.setFocusable(true); // 这个很重要
        view.setFocusableInTouchMode(true);

        ListView lv_file = (ListView) view.findViewById(R.id.lv_filelist);
        // 全选按钮
        Button btn_selectall = (Button) view.findViewById(R.id.btn_selectall);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancelselectall);
        Button btn_deselectall = (Button) view.findViewById(R.id.btn_deselectall);
        Button btn_confirmdelete = (Button) view.findViewById(R.id.btn_confirmdelete);
        Button btn_canceldelete = (Button) view.findViewById(R.id.btn_canceldelete);
        Button btn_quit = (Button) view.findViewById(R.id.btn_quit);

        tv_showNum = (TextView) view.findViewById(R.id.tv_select);

        itemList = new ArrayList<HashMap<String, String>>();
        // 为Adapter准备数据
        initDate();
        mFileAdapter = new FileAdapter(itemList, this);
        lv_file.setAdapter(mFileAdapter);

        // 全选按钮
        btn_selectall.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < itemList.size(); i++) {
                    itemList.get(i).put("flag", "true");
                }
                // 数量设为list的长度
                checkNum = itemList.size();
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });
        // 取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).get("flag").equals("true")) {
                        itemList.get(i).put("flag", "false");
                        checkNum--;// 数量减1
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });
        // 反选按钮
        btn_deselectall.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 遍历list的长度，将已选的设为未选，未选的设为已选
                for (int i = 0; i < itemList.size(); i++) {
                    if (itemList.get(i).get("flag").equals("true")) {
                        itemList.get(i).put("flag", "false");
                        checkNum--;
                    } else {
                        itemList.get(i).put("flag", "true");
                        checkNum++;
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });

        // 确认删除
        btn_confirmdelete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Iterator<HashMap<String, String>> iterator = itemList.iterator();
                while (iterator.hasNext()) {
                    HashMap<String, String> temp = iterator.next();
                    if (temp.get("flag").equals("true")) {
                        iterator.remove();
                        FileUtils.RecursionDeleteFile(new File(temp.get("path")));
                    }
                }
                checkNum = 0;
                // 通知列表数据修改
                dataChanged();
            }
        });

        // 删除完成
        btn_canceldelete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (getSDAvailableSize() < LIMIT_AVAILABLESIZE) {
                    Utils.toast(Main.this, "存储空间仍不足，请继续删除无用数据");
                    return;
                }
                filePopupWindow.dismiss();
                init();
            }
        });

        // 退出软件
        btn_quit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isForceQuit = true;
                quitDialog();
                // ////////////////////////////////////
                // android.os.Process.killProcess(checkNum);
            }
        });

        lv_file.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                ViewHolder holder = (ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                // 调整选定条目
                if (holder.cb.isChecked() == true) {
                    itemList.get(arg2).put("flag", "true");
                    checkNum++;
                } else {
                    itemList.get(arg2).put("flag", "false");
                    checkNum--;
                }
                // 用TextView显示
                tv_showNum.setText("已选中" + checkNum + "项");

            }

        });

        // 创建AlertDialog
        filePopupWindow = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        // filePopupWindow.setBackgroundDrawable(getResources().getDrawable(
        // R.drawable.popbg));
        filePopupWindow.setFocusable(true);
        // filePopupWindow.setWidth(screenWidth);
        // filePopupWindow.setHeight(screenHeight);
        filePopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        filePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            public void onDismiss() {

            }
        });
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        ArrayList<FileInfo> list = getFileNameList(dataPath);
        for (FileInfo info : list) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("content", info.name);
            map.put("flag", "false");
            map.put("path", info.path);
            itemList.add(map);
        }
    }

    /**
     * 刷新listview和TextView的显示
     */
    private void dataChanged() {
        mFileAdapter.notifyDataSetChanged();
        tv_showNum.setText("已选中" + checkNum + "项");
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private long getSDAvailableSize() {

        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获取制定文件夹下文件夹列表
     *
     * @param path
     * @return
     */
    private ArrayList<FileInfo> getFileNameList(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.name = file.getName();
                    fileInfo.path = file.getPath();
                    fileInfo.lastModified = file.lastModified();
                    fileList.add(fileInfo);
                }
            }
        }
        Collections.sort(fileList, new FileComparator());

        return fileList;
    }

    public class FileInfo {
        public String name;
        public String path;
        public long lastModified;
    }

    /**
     * 修改时间排序
     */
    public class FileComparator implements Comparator<FileInfo> {
        public int compare(FileInfo file1, FileInfo file2) {
            if (file1.lastModified < file2.lastModified) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * 过滤后缀为txt
     */
    public FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            String tmp = file.getName().toLowerCase();
            if (tmp.endsWith(".txt")) {
                return true;
            }
            return false;
        }
    };

    /**
     * 现场恢复提示框 zgl
     */
    private void huifuDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("是否恢复异常退出前状态？").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                findViewById(R.id.btn_data_receive).setEnabled(true);
                Intent intent = new Intent();
                intent.setClass(Main.this, Point_Collect.class);
                Main.this.startActivity(intent);
            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Preferences.saveStep(Main.this, 99);
                //startTimer();
                // mProgressDialog = ProgressDialog.show(Main.this,
                // "机芯自动配对中...",
                // "请稍等...", true, false);
            }
        }).show();
    }

    /**
     * 接续操作 zgl 2014-11-13 12:24:00
     */
    private void jieXu() {
        POINT_COLLECT point_collect = new POINT_COLLECT();
        SET_START set_start = new SET_START();
        SOFT_CONFIG soft_config = new SOFT_CONFIG();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(btPath)).listFiles(); // 接收文件中全部的文件
        String[] dataFiles = new String[2]; // 保存yx文件的文件名数组 0为名称，1为扩展名
        String dataFileAll = "";// 保存全部文件名用于拆分
        System.out.println(file.length);
        //
        // Log.e("llllll","getINTERVAL_LEN"+point_collect.getINTERVAL_LEN());
        // Log.e("llllll","getNextMillistime"+point_collect.getNextMillistime());
        // Log.e("llllll","getSTART_DEEP"+point_collect.getSTART_DEEP());
        // Log.e("llllll","getSTEP"+point_collect.getSTEP());
        // Log.e("llllll","getSumPoint"+point_collect.getSumPoint());
        // Log.e("llllll","getSumPoint"+point_collect.isDEEP_MANUAL());
        //
        // Log.e("cccccc","getDELAY_TIME"+set_start.getDELAY_TIME());
        // Log.e("cccccc","getHOLE_ID"+set_start.getHOLE_ID());
        // Log.e("cccccc","getINTERVAL_TIME"+set_start.getINTERVAL_TIME());
        // Log.e("cccccc","getSTART_TIME"+set_start.getSTART_TIME());
        //
        Log.e("dddddd", "getCODE_TO_HEART" + point_collect.getIlist_bhs());
        // Log.e("dddddd","getMACHINE_NUM"+soft_config.getMACHINE_NUM());
        try {
            FileUtils.copyFile(new File("/sdcard/bluetooth/SET_START.txt"), new File("/sdcard/bluetooth/" + "SET_START.xml"));
            FileUtils.copyFile(new File("/sdcard/bluetooth/POINT_COLLECT.txt"), new File("/sdcard/bluetooth/" + "POINT_COLLECT.xml"));
//            FileUtils.copyFile(new File("/sdcard/bluetooth/SOFT_CONFIG.txt"), new File("/sdcard/bluetooth/" + "SOFT_CONFIG.xml"));
            // FileUtils.copyFile(new File("/sdcard/bluetooth/SOFT_CONFIG.txt"),
            // new File("/sdcard/bluetooth/" + "SOFT_CONFIG.xml"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        for (int i = 0; i < file.length; i++) {
            if (file[i].getName().contains(".mp4")) {
                dataFiles = file[i].getName().split("\\.");
                dataFileAll = dataFiles[0];
                point_collect = Utils.parsePOINT_COLLECT(btPath + "POINT_COLLECT.xml");
                Log.e("file name:", "btPath=" + btPath);
                set_start = Utils.parseSET_START(btPath + "SET_START.xml");
                updataxml(point_collect, set_start);
                // ////////////////////////////
                if (("F".equals(Preferences.getFangString(this)))) {
                    if (("P").equals(Preferences.getBenString(this))) {
                        dataFileAll = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                        fileName = Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this);
                    } else {
                        dataFileAll = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                        fileName = Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this);
                    }
                } else {
                    if (("P").equals(Preferences.getBenString(this))) {
                        dataFileAll = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                        fileName = Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this);

                    } else {
                        dataFileAll = Environment.getExternalStorageDirectory() + "/HKCX-SJ/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
                        fileName = Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this);

                    }
                }
                FileUtils.FileCreate(dataFileAll, null);
                try {
                    // FileUtils.copyFile(new File(btPath+"SET_START.xml"),new
                    // File("/data/data/com.android.antiexplosionphone/shared_prefs/SET_START.xml"));
                    // FileUtils.copyFile(new
                    // File(btPath+"POINT_COLLECT.xml"),new
                    // File("/data/data/com.android.antiexplosionphone/shared_prefs/POINT_COLLECT.xml"));
                    // FileUtils.copyFile(new File(btPath+"SOFT_CONFIG.xml"),new
                    // File("/data/data/com.android.antiexplosionphone/shared_prefs/SOFT_CONFIG.xml"));
                    FileUtils.copyFile(new File(btPath + file[i].getName()), new File(dataFileAll + "/" + file[i].getName().split("\\.")[0] + ".YX"));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Preferences.saveSumPoint(this, 10); //测试用
                // Log.e("mylog","SET_START.xml="+Preferences.getSumPoint(this));
                // //测试用
            }
        }
        saveDataSQL();
        jiexuDialog();
    }

    private String fileName;

    private void saveDataSQL() {

        DbManager.DaoConfig daoConfig = new DaoConfig().setDbName("SetValueBean") // 数据库的名字
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
            info.setQinjiao(Double.parseDouble(Preferences.getqinjiaoString(Main.this).trim()));
            info.setFangwei(Double.parseDouble(Preferences.getfangweiString(Main.this).trim()));
            info.setKongshen(Double.parseDouble(Preferences.getkongshenString(Main.this).trim()));
            info.setShang(Double.parseDouble(Preferences.getshangString(Main.this).trim()));
            info.setZuo(Double.parseDouble(Preferences.getzuoString(Main.this).trim()));
            info.setZong(Double.parseDouble(Preferences.getzongString(Main.this).trim()));
            info.setFileName(fileName);
            manager.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接续提示框 zgl
     */
    private void jiexuDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("检测到接收文件,是否接续工作？").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                findViewById(R.id.btn_data_receive).setEnabled(true);
                Intent intent = new Intent();
                intent.setClass(Main.this, Point_Collect.class);
                Main.this.startActivity(intent);
                FileUtils.RecursionDeleteFiles(new File(btPath));

            }
        }).setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // 要删除蓝牙接收文件夹内的文件
                FileUtils.RecursionDeleteFiles(new File(btPath));
                if (step != 99) {
                    huifuDialog();
                } else {
                    //startTimer();
                    // mProgressDialog = ProgressDialog.show(Main.this,
                    // "机芯自动配对中...",
                    // "请稍等...", true, false);
                }
            }
        }).show();
    }

    /***
     * xml数据更新
     *
     * @author zgl
     * 1552710911
     */
    private void updataxml(POINT_COLLECT pc, SET_START ss) {

        Preferences.saveMeasureInterval(this, pc.getINTERVAL_LEN());
        Preferences.saveNextMillistime(this, pc.getNextMillistime());
        Preferences.saveStartDeep(this, pc.getSTART_DEEP());
        Preferences.saveStep(this, pc.getSTEP());
        Preferences.saveSumPoint(this, pc.getSumPoint());
        Preferences.saveDeepManual(this, pc.isDEEP_MANUAL());
        Preferences.saveMeasureWayToUp(this, pc.isMeasureWayToUp());
        Preferences.saveIlist_bhs(this, pc.getIlist_bhs());
        Log.e("meicunshang", pc.getIlist_bhs() + "");
        Preferences.saveDelayandIntervalTime(this, ss.getDELAY_TIME(), ss.getINTERVAL_TIME());
        Preferences.saveHoleIDString(this, ss.getHOLE_ID());
        Preferences.saveStartTime(this, ss.getSTART_TIME());
        Preferences.saveDrillingString(this, ss.getDrilling());
        Preferences.saveBenString(this, ss.getBen());
        Preferences.saveFaceString(this, ss.getFace());
        Preferences.saveChongMString(this, ss.getChongM());
        Preferences.saveTime(this, ss.getTime());
        Log.e("ss.getTime()==>", ss.getTime() + "==============================<<<<<<");
        Preferences.saveJiaoZhun(this, ss.getJiaoZhun());
        Preferences.saveFangString(this, ss.getFang());
/**********achao*****/
        Preferences.saveqinjiaoString(this, ss.getQinjiao());
        Preferences.savefangweiString(this, ss.getFangwei());
        Preferences.savekongshenString(this, ss.getKongshen());
        Preferences.saveshangString(this, ss.getShang());
        Preferences.savezuoString(this, ss.getZuo());
        Preferences.savezongString(this, ss.getZong());

    }
    CreateUserPopWin createUserPopWin;
    public void showEditPopWin() {
        createUserPopWin = new CreateUserPopWin(Main.this,Main.this);
        createUserPopWin.showAtLocation(findViewById(R.id.btn_当地参数), Gravity.CENTER, 0, 0);
    }
}
