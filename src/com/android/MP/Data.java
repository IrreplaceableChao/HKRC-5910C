package com.android.MP;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.MPFragment.ZuanKongGuiJi;
import com.android.antiexplosionphone.R;
import com.android.bean.SetParameterBean;
import com.android.bean.SetValueBean;
import com.android.utils.ConstantDef;
import com.android.utils.CreateUserPopWin;
import com.android.utils.FormulaUtil;
import com.android.utils.SharedPreferencesHelper;
import com.android.utils.Utils;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.List;

import static com.android.utils.FileUtils.RandomReadFile;
import static com.android.utils.FormulaUtil.$B$1;
import static com.android.utils.FormulaUtil.$C$2;
import static com.android.utils.FormulaUtil.$D$1;
import static com.android.utils.FormulaUtil.$E$2;
import static com.android.utils.FormulaUtil.$F$1;
import static com.android.utils.FormulaUtil.kongshen;
import static com.android.utils.FormulaUtil.shang;
import static com.android.utils.FormulaUtil.zong;
import static com.android.utils.FormulaUtil.zuo;


/**
 * Created by jiangchao on 2016/10/23.
 */
public class Data extends Activity implements View.OnClickListener {
    private Button form_data;
    private Button zhiliang;
    private Button fanhui;
    private TextView tv_use, tv_type, tv_workspace, tv_farm, tv_namber, tv_date, tv_qinjiao, tv_fangwei, tv_kongshen, long_tv;
    private LinearLayout LL_farm;
    public static double[] qj;
    public static double[] ks;
    public static double[] fw;
    public static String[] dz;
    public static double[] sx; //上下偏差
    public static double[] zy; //左右偏差
    public static int x_num;
    SharedPreferencesHelper SPconfig;
    public static String mSJFilePath;  // 采集有效点数据文件
    public static String mYSFilePath;  // 采集有效点数据文件
    public static String[] spStr;
    private List<SetValueBean> Svalue;
    private String fileNamesql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        findviewbyid();

        setonclick();

        init();
    }

    //    boolean open = false;   //true 代表打开状态，false代表关闭状态    默认关闭状态
    private void setonclick() {
        form_data.setOnClickListener(this);
        zhiliang.setOnClickListener(this);
        fanhui.setOnClickListener(this);
        long_tv.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                createUserPopWin = new CreateUserPopWin(Data.this, Data.this);
                createUserPopWin.showAtLocation(findViewById(R.id.button), Gravity.CENTER, 0, 0);
                return false;
            }
        });

    }

    CreateUserPopWin createUserPopWin;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent();
                intent.setClass(Data.this, ZuanKongGuiJi.class);
                startActivity(intent);
                break;
            case R.id.button2:
                intent = new Intent();
//                java.lang.ArrayIndexOutOfBoundsException: length=20; index=20
                intent.putExtra("x", formula.x_()[formula.x_().length - 1] + "");
                intent.putExtra("y", formula.y_()[formula.y_().length - 1] + "");
                intent.putExtra("z", formula.z_()[formula.z_().length - 1] + "");
                intent.setClass(Data.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button3:
                finish();
                break;
            case R.id.btn_save_pop:
                String str = createUserPopWin.getText_mobile().getText().toString().trim();
                String[] str1;
                try {
                    str1 = str.split(",");
                    addSQL(Double.parseDouble(str1[0]), Double.parseDouble(str1[1]), Double.parseDouble(str1[2]));
                } catch (Exception e) {
                    createUserPopWin.dismiss();
                    Utils.toast(Data.this, "修改失败！");
                    break;
                }
                createUserPopWin.dismiss();
                Utils.toast(Data.this, "修改成功！");
                tv_qinjiao.setText(str1[0]);
                tv_fangwei.setText(str1[1]);
                tv_kongshen.setText(str1[2]);
                break;
        }
    }

    private void findviewbyid() {
        form_data = (Button) findViewById(R.id.button);
        zhiliang = (Button) findViewById(R.id.button2);
        fanhui = (Button) findViewById(R.id.button3);
        tv_use = (TextView) findViewById(R.id.tv_use);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_workspace = (TextView) findViewById(R.id.tv_workspace);
        tv_farm = (TextView) findViewById(R.id.tv_farm);
        tv_namber = (TextView) findViewById(R.id.tv_namber);
        tv_date = (TextView) findViewById(R.id.tv_date);
        LL_farm = (LinearLayout) findViewById(R.id.LL_farm);
        tv_qinjiao = (TextView) findViewById(R.id.tv_qinjiao);
        tv_fangwei = (TextView) findViewById(R.id.tv_fangwei);
        tv_kongshen = (TextView) findViewById(R.id.tv_kongshen);
        long_tv = findViewById(R.id.Long_tv);
    }

    private DecimalFormat df = new DecimalFormat("0.00");

    private void init() {
        SPconfig = new SharedPreferencesHelper(this, "SET_START");
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        fileNamesql = path;
        spStr = path.split("-");
        /**** 截取文件名 ***/
        if (spStr[0].equals("F")) {
            tv_use.setText("防治水");
        } else {
            tv_use.setText("瓦斯抽(采)");
        }
        if (spStr[1].equals("P")) {
            tv_type.setText("平行孔");
            LL_farm.setVisibility(View.GONE);
            tv_workspace.setText(spStr[2]);
            tv_namber.setText(spStr[3]);
            String d_str = spStr[4];
            tv_date.setText("20" + d_str.substring(0, 2) + "-" + d_str.substring(2, 4) + "-" + d_str.substring(4, 6));
            mSJFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HKCX-SJ/" + "/" + path + "/" + spStr[1] + "-" + spStr[2] + "-" + spStr[3] + ConstantDef.FILE_SUFFIX_SJ_POINT;
            mYSFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HKCX-SJ/" + "/" + path + "/" + spStr[1] + "-" + spStr[2] + "-" + spStr[3] + ConstantDef.FILE_SUFFIX_YS_POINT;
        } else {
            tv_type.setText("扇形孔");
            LL_farm.setVisibility(View.VISIBLE);
            tv_workspace.setText(spStr[2]);
            tv_farm.setText(spStr[3]);
            tv_namber.setText(spStr[4]);
            String d_str = spStr[5];
            tv_date.setText("20" + d_str.substring(0, 2) + "-" + d_str.substring(2, 4) + "-" + d_str.substring(4, 6));
            mSJFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HKCX-SJ/" + path + "/" + spStr[1] + "-" + spStr[2] + "-" + spStr[3] + "-" + spStr[4] + ConstantDef.FILE_SUFFIX_SJ_POINT;
            mYSFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HKCX-SJ/" + path + "/" + spStr[1] + "-" + spStr[2] + "-" + spStr[3] + "-" + spStr[4] + ConstantDef.FILE_SUFFIX_YS_POINT;
        }
        getDbParameter();

        seachinfo();

        /**添加表头*/
        handler.obtainMessage(2).sendToTarget();
        Thread.start();
    }

    int h = 40;
    TableRow.LayoutParams tv_190 = new TableRow.LayoutParams(180, h);
    TableRow.LayoutParams tv_130 = new TableRow.LayoutParams(138, h);
    TableRow.LayoutParams tv_125 = new TableRow.LayoutParams(122, h);
    TableRow.LayoutParams tv_128 = new TableRow.LayoutParams(126, h);
    TableRow.LayoutParams tv_100 = new TableRow.LayoutParams(75, h);

    private void addData_top() {
        String[] items_ = {"序号", "孔深,m", "倾角,º", "方位角,º", "地质信息", "上下偏差,m", "左右偏差,m"};
        LinearLayout tablelayout =  findViewById(R.id.linear_top);
        for (int i = 0; i < 7; i++) {
            TextView tv_num = new TextView(this);
            tv_num.setGravity(Gravity.CENTER);
            tv_num.setTextSize(20);
            tv_num.setTextColor(Color.rgb(0x40, 0x40, 0x40));
            tv_num.setBackgroundResource(R.drawable.bg_top_third);
            tv_num.setText(items_[i]);
            if (i == 0) {
                tv_num.setLayoutParams(tv_100);
            } else if (i == 1) {
                tv_num.setLayoutParams(tv_125);
            } else if (i == 2) {
                tv_num.setLayoutParams(tv_125);
            } else if (i == 3) {
                tv_num.setLayoutParams(tv_130);
            } else if (i == 4) {
                tv_num.setLayoutParams(tv_128);
            } else if (i == 5) {
                tv_num.setLayoutParams(tv_190);
            } else if (i == 6) {
                tv_num.setLayoutParams(tv_190);
            }
            tablelayout.addView(tv_num);
        }
    }

    private void addData(String[] item) {
        TableLayout table = (TableLayout) findViewById(R.id.tl_receive_data);
        TableRow tablerow = new TableRow(this);
        TableRow.LayoutParams lparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tablerow.setLayoutParams(lparams);
        //		序号
        for (int i = 0; i < 7; i++) {
            TextView tv_num = new TextView(this);
            tv_num.setGravity(Gravity.CENTER | Gravity.RIGHT);
            tv_num.setTextSize(20);
            tv_num.setTextColor(Color.rgb(0x40, 0x40, 0x40));
            tv_num.setBackgroundResource(R.drawable.bg_item_third);
            tv_num.setText(item[i]);
            if (i == 0) {
                tv_num.setGravity(Gravity.CENTER);
                tv_num.setLayoutParams(tv_100);
            } else if (i == 1) {
                tv_num.setLayoutParams(tv_125);
            } else if (i == 2) {
                tv_num.setLayoutParams(tv_125);
            } else if (i == 3) {
                tv_num.setLayoutParams(tv_130);
            } else if (i == 4) {
                tv_num.setGravity(Gravity.CENTER);
                tv_num.setLayoutParams(tv_128);
            } else if (i == 5) {
                tv_num.setLayoutParams(tv_190);
            } else if (i == 6) {
                tv_num.setLayoutParams(tv_190);
            }
            tablerow.addView(tv_num);
        }
        table.addView(tablerow);
    }

    FormulaUtil formula;
    int num = 0, num0 = 0, num1 = 0;
    private double max = 0, min = 0;
    private java.lang.Thread Thread = new Thread() {
        @Override
        public void run() {

            /**读取sj文件*/
            byte[] a = RandomReadFile(mSJFilePath, 0, 2);
            num = Utils.getChar(a);
            if (a[0] < 0) {
                num0 = 256 + a[0];
            } else {
                num0 = 256 + a[0] - 256;
            }
            if (a[1] < 0) {
                num1 = 256 + a[1];
            } else {
                num1 = 256 + a[1] - 256;
            }
            num = num0 + num1 * 256 + 1;
            qj = new double[num];
            ks = new double[num];
            fw = new double[num];
            dz = new String[num];
            sx = new double[num];
            zy = new double[num];
//            Math.asin((Math.sin(80.51 * $F$1) * Math.sin(-0.66 * $F$1)) / Math.sin(0.65 * $F$1)) / $F$1;
            /**
             钻进测量：若有效采集数据中孔深最小的数据为0米，则作为起始点；若＞0米，则自动生成一个孔深0米的数据，其测量数据（倾角、方位角、地质信息）与有效采集数据中孔深最小的数据一致。
             起钻测量：首先滤掉有效采集数据中孔深小于0米的数据。然后按照钻进测量的处理方法进行处理，注意没有地质信息。
             先钻进测量，再起钻测量：先让用户选择测量方式，然后按照选择的是“钻进测量”还是“起钻测量”进行处理。
             */
            double ks1 = Double.valueOf(Float.toString(Utils.getFloat(RandomReadFile(mSJFilePath, 0 * 28 + 2 + 2, 4), 0)));
            double ks2 = Double.valueOf(Float.toString(Utils.getFloat(RandomReadFile(mSJFilePath, 1 * 28 + 2 + 2, 4), 0)));
            //012343212

            for (int i = 0; i < num - 1; i++) {
                //孔深
                ks[i] = Double.valueOf(Float.toString(Utils.getFloat(RandomReadFile(mSJFilePath, i * 28 + 2 + 2, 4), 0)));
            }
            if (ks1 < ks2) {//钻进

                for (int i = 1; i < ks.length - 1; i++) {
                    if (ks[i - 1] < ks[i]) {
                        //记录钻进多少个点。
                        if (endpoint) {
                            return;
                        }
                        startnum = i;//钻进点0-5    0 1 2 3 4
                    } else {
                        endnum = i; // 5 - 8     4 3 2 1
                        endpoint = true;
                        //记录从第几个点开始起钻到第几个点
                    }
                }
            }

            if (ks1 < ks2 && ks1 == 0) {
                /**钻进有起点*/
                计算JS文件数据(0, 1);
                isStartPoing = true;
            } else if (ks1 < ks2 && ks1 > 0) {
                /**钻进无起点*/
                计算JS文件数据(1, 0);
                ks[0] = 0;
                qj[0] = qj[1];
                fw[0] = fw[1];
                dz[0] = "无";
                startnum++;
            } else if (ks1 > ks2) {
                计算JS文件数据(0, 1);
                ks[num - 1] = 0;
                qj[num - 1] = $C$2;
                fw[num - 1] = $E$2;
                dz[num - 1] = "无";
            }

            if (endpoint) {
                handler.obtainMessage(3).sendToTarget();
            } else if (isStartPoing) {

                计算偏差方法(ks, qj, fw, num - 1);
            } else {
                计算偏差方法(ks, qj, fw, num);
            }
        }
    };
    int endnum = 0;
    int startnum = 0;
    boolean endpoint = false;
    boolean isStartPoing = false;

    private void SelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Data.this);
        builder.setTitle("请选择钻进测量还是起钻测量").setIcon(android.R.drawable.ic_dialog_alert).setCancelable(false).setPositiveButton("钻进测量", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                计算偏差方法(ks, qj, fw, startnum);
            }
        }).setNegativeButton("起钻测量", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                double a1[] = new double[endnum - startnum];
                double a2[] = new double[endnum - startnum];
                double a3[] = new double[endnum - startnum];

                System.arraycopy(ks, startnum, a1, 0, endnum - startnum);
                System.arraycopy(qj, startnum, a2, 0, endnum - startnum);
                System.arraycopy(fw, startnum, a3, 0, endnum - startnum);
                计算偏差方法(a1, a2, a3, endnum - startnum);
//                计算偏差方法(ks,qj,fw,num);
            }
        }).show();
    }

    public void 计算偏差方法(double[] ks, double[] qj, double[] fw, int num) {
        formula = new FormulaUtil(ks, qj, fw, num);
        for (int i = 0; i < num; i++) {
            double I = formula.y_()[i] * Math.cos((270 + $E$2 + 0) * $F$1) - formula.x_()[i] * Math.sin((270 + $E$2 + 0) * $F$1);
            sx[i] = Utils.BD(I * Math.sin((360 - $C$2) * $F$1) + formula.z_()[i] * Math.cos((360 - $C$2) * $F$1));
            zy[i] = Utils.BD(formula.y_()[i] * Math.sin((270 + $E$2 + 0) * $F$1) + formula.x_()[i] * Math.cos((270 + $E$2 + 0) * $F$1));
            String[] items = {i + 1 + "", ks[i] + "", df.format(qj[i]) + "", df.format(fw[i]) + "", dz[i], df.format(sx[i]) + "", df.format(zy[i]) + ""};
            handler.obtainMessage(1, items).sendToTarget();
        }
        if (kongshen > (max - min)) {
            x_num = Integer.parseInt(df1.format(kongshen));
        } else {
            x_num = (int) (max - min);
        }
    }

    public void 计算JS文件数据(int int1, int int2) {
        for (int i = int1; i < num - int2; i++) {
            //孔深
            ks[i] = Double.valueOf(Float.toString(Utils.getFloat(RandomReadFile(mSJFilePath, (i - int1) * 28 + 2 + 2, 4), 0)));
            if (ks[i] > max) max = ks[i];
            if (i == 0) min = ks[i];
            if (ks[i] < min) min = ks[i];

            byte[] q = RandomReadFile(mSJFilePath, (i - int1) * 28 + 16 + 2, 2);//倾角
            qj[i] = Utils.BD(Double.parseDouble(df.format(Utils.getChar(q) / 100.0 - 90.0)));
            byte[] f = RandomReadFile(mSJFilePath, (i - int1) * 28 + 18 + 2, 2);//方位角
            fw[i] = Utils.getChar(f) / 100.0;
            //地质信息
            dz[i] = Utils.getString(RandomReadFile(mSJFilePath, (i - int1) * 28 + 12 + 2, 4));
            /**添加数据*/
        }
    }

    private DecimalFormat df1 = new DecimalFormat("0");
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String[] str = (String[]) msg.obj;
                    addData(str);
                    break;
                case 2:
                    //添加表头字段
                    addData_top();
                    break;
                case 3:
                    SelectDialog();
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void seachinfo() {
        DbManager.DaoConfig daoConfig = new DaoConfig().setDbName("SetValueBean") // 数据库的名字
                // 保存到指定路径 .setDbDir(newFile(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .setDbVersion(1)// 数据库的版本号
                .setDbUpgradeListener(new DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager arg0, int arg1, int arg2) {
                        LogUtil.e("数据库版本更新了！，此版本不联网发布不涉及数据库更新问题。");  // 数据库版本更新监听
                    }
                });
        DbManager manager = x.getDb(daoConfig);
        /***
         * 添加设计数据
         try {
         SetValueBean info = new SetValueBean();  info.setQinjiao(2);
         info.setFangwei(60);
         info.setKongshen(65);
         info.setShang(5);
         info.setZuo(5);
         info.setZong(5);
         info.setFileName(fileNamesql);
         manager.saveOrUpdate(info);
         } catch (DbException e) {
         e.printStackTrace();
         }
         * **/
        try {
            List<SetValueBean> findAll = manager.findAll(SetValueBean.class);
            for (int i = 0; i < findAll.size(); i++) {
                Log.e("find", findAll.get(i).getFileName() + ":path=" + fileNamesql);
                if (findAll.get(i).getFileName().equals(fileNamesql)) {
                    tv_qinjiao.setText(findAll.get(i).getQinjiao() + "");
                    double azimuth = findAll.get(i).getFangwei();

                    if (azimuth + $D$1 - $B$1 >= 360) {
                        azimuth = azimuth + $D$1 - $B$1 - 360;
                    } else if (azimuth + $D$1 - $B$1 < 0) {
                        azimuth = azimuth + $D$1 - $B$1 + 360;
                    } else {
                        azimuth = azimuth + $D$1 - $B$1;
                    }

                    tv_fangwei.setText(df.format(azimuth));
                    kongshen = findAll.get(i).getKongshen();
                    tv_kongshen.setText(kongshen + "");
                    $E$2 = findAll.get(i).getFangwei();
                    $C$2 = findAll.get(i).getQinjiao();
                    shang = findAll.get(i).getShang();
                    zuo = findAll.get(i).getZuo();
                    zong = findAll.get(i).getZong();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tv_qinjiao.getText().toString().equals("0")) {
            $E$2 = 0;//设计磁方位角
            $C$2 = 0;//设计倾角
            kongshen = 0;
            shang = 0;
            zuo = 0;
            zong = 0;
        }
    }

    public void addSQL(double qj, double fw, double ks) {
        DbManager.DaoConfig daoConfig = new DaoConfig().setDbName("SetValueBean") // 数据库的名字
                // 保存到指定路径 .setDbDir(newFile(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .setDbVersion(1)// 数据库的版本号
                .setDbUpgradeListener(new DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager arg0, int arg1, int arg2) {
                        LogUtil.e("数据库版本更新了！，此版本不联网发布不涉及数据库更新问题。");  // 数据库版本更新监听
                    }
                });
        DbManager manager = x.getDb(daoConfig);

        try {
            SetValueBean info = new SetValueBean();
            info.setQinjiao(qj);
            info.setFangwei(fw);
            info.setKongshen(ks);
//         info.setShang(5);
//         info.setZuo(5);
//         info.setZong(5);
            info.setFileName(fileNamesql);
            manager.saveOrUpdate(info);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    /***
     * 添加当地子午线收敛角、磁偏角到数据库
     * int 1 为存数据
     * int 2 为取数据
     * AChao 2018-1-4 15:34:32
     */
    private void getDbParameter() {
        DbManager.DaoConfig daoConfig = new DaoConfig().setDbName("SetParameter") // 数据库的名字
                // 保存到指定路径 .setDbDir(newFile(Environment.getExternalStorageDirectory().getAbsolutePath()))
                .setDbVersion(1)// 数据库的版本号
                .setDbUpgradeListener(new DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager arg0, int arg1, int arg2) {
                        LogUtil.e("数据库版本更新了！，此版本不联网发布不涉及数据库更新问题。");  // 数据库版本更新监听
                    }
                });
        DbManager manager = x.getDb(daoConfig);
        try {
            List<SetParameterBean> findAll = manager.findAll(SetParameterBean.class);
            $D$1 = findAll.get(findAll.size() - 1).getDeclination();
            $B$1 = findAll.get(findAll.size() - 1).getConvergenceOfMeridians();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}