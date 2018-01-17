package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.MP.Data;
import com.android.bluetooth.BluetoothTools;
import com.android.utils.ConstantDef;
import com.android.utils.CreateExcel;
import com.android.utils.FileUtils;
import com.android.utils.Preferences;
import com.android.utils.TransProtocol;
import com.android.utils.TransProtocol.ANS_Rev_Read_CollectCnt_etc;
import com.android.utils.TransProtocol.ANS_Rev_Read_CollectCnt_etc.ANS_READ_COLLECTCNT_ETC;
import com.android.utils.TransProtocol.ANS_Rev_Read_Storage_data;
import com.android.utils.TransProtocol.ANS_Rev_Read_Storage_data.Struct_ReadStorage_Data;
import com.android.utils.TransProtocol.Efficient_Point;
import com.android.utils.TransProtocol.Efficient_Point.Struct_Ensure_Point;
import com.android.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据接收界面
 *
 * @author TY
 */
public class Data_Receive extends Activity {

    private Handler mHandler = new Handler();
    private String ColorStr;
    private static final String TAG = Data_Receive.class.getSimpleName();

    private int mReSendTime = ConstantDef.MAX_RESEND_TIME;
    private int mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    private String BS = "0";
    private String mYXFilePath; // 确定有效点文件
    private String mYSFilePath; // 原始文件
    private String mSJFilePath; // 采集有效点数据文件

    private Struct_Ensure_Point mStructEnsurePoint;
    private Struct_ReadStorage_Data mStructReadStorage;

    private int mnCurEnsurePointCnt = 0;// 当前读取确定有效点个数
    private int mnORGMaxPointCnt = 0; // 机芯采集点总数目
    private int mnNextRevPoint = 0; // 下一个要读取点编号

    private ProgressBar mProgressBar; // 进度条
    private int mnProgress = 0;
    private WakeLock mWakeLock;

    private TextView tv_num;
    private TextView tv_count;//2017-8-4 09:42:04编写用于测试
    private Button exitL;
    private Button guiji;
    private String filename = "";
    private List<Map<String, String>> excledatas = new ArrayList<Map<String, String>>();
    DecimalFormat df = new DecimalFormat("##0.000");
    DecimalFormat df1 = new DecimalFormat("##0.00");
    DecimalFormat df2 = new DecimalFormat("##0.0");
    DecimalFormat df3 = new DecimalFormat("##0");


    private BroadcastReceiver bluetoothTransReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (BluetoothTools.ACTION_READ_DATA.equals(action)) {

                // 停止命令应答
                if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_STOP) {
                    // 停止命令应答，分析应答数据
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_STOP)) {
                        Utils.toast(Data_Receive.this, "数据接收停止命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 发送读取采集点数、延时时间、间隔时间指令
                    BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_READ_COLLECTCNT_ETC, TransProtocol.CMD_TYPE_READ_COLLECTCNT_ETC);
                    resetCtrlParam();
                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_COLLECTCNT_ETC) {
                    // 读取采集点数、延时时间、间隔时间指令应答
                    ANS_READ_COLLECTCNT_ETC ans = ANS_Rev_Read_CollectCnt_etc.getAns(BluetoothTools.readData(intent));
                    byte[] bytes = ANS_Rev_Read_CollectCnt_etc.getAnsDelayIntervalBytes(BluetoothTools.readData(intent));
                    System.out.println("collectCnt = " + ans.collectCnt + " delayTime = " + ans.delayTime + " intervalTime = " + ans.intervalTime);
                    mnORGMaxPointCnt = ans.collectCnt;
                    if (mnORGMaxPointCnt > 100) mProgressBar.setMax(mnORGMaxPointCnt);
                    mnNextRevPoint = 1;
                    // 创建原始测量数据文件
                    FileUtils.FileNewCreate(mYSFilePath);
                    FileUtils.appendWriteFile(mYSFilePath, Utils.getLittleBytes((char) (mnORGMaxPointCnt)));
                    FileUtils.appendWriteFile(mYSFilePath, bytes);
                    // 创建
                    FileUtils.FileNewCreate(mSJFilePath);
                    FileUtils.appendWriteFile(mSJFilePath, FileUtils.RandomReadFile(mYXFilePath, 0, ConstantDef.FILE_VALID_POINT_CNT_BYTES));

                    // 读取确定有效点文件,获取编号
                    byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                    if (bytesEnsure != null) {
                        FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                        mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                        mnCurEnsurePointCnt += 1;
                    }
                    // 发送读取第一片存储区
                    BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_Send_ReadStorageData.getSendCmd(ConstantDef.DATASPACE0_START_ADDR + (mnNextRevPoint - 1) * ConstantDef.DATA_REV_POINT_DATA_BYTES, true), TransProtocol.CMD_TYPE_READ_STORAGE_DATA0);
                    resetCtrlParam();
                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_STORAGE_DATA0) {
                    // 接收第一片存储区数据
                    byte[] bytes = null;
                    if ((mnNextRevPoint <= (ConstantDef.MAX_PER_STORAGE_COUNT - 1)) && (mnNextRevPoint < mnORGMaxPointCnt)) {
                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), true);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
                            String[] items = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,};


                            addReceiveData(items);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//							{Log.e("i", i+"");
//									if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//										
//										BS="1";
//									}else{
//										BS="0";
//									}
//									
//								}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)

                            };
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }

                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), false);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//						{Log.e("i", i+"");
//								if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//									BS="1";
//								}else{
//									BS="0";
//								}
//								
//							}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] items = {

                                    Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,

                            };
                            addReceiveData(items);
//							for (int i = 0; i <Point_Collect.list.size(); i++) 
//							{
//								Log.e("mStructEnsurePoint.id", mStructEnsurePoint.id+"");
//								Log.e("i", Point_Collect.list.get(i)+"");
//								int colour=Integer.parseInt(tv_num.getText().toString());
//								Log.e("colour",colour+"");
//								if(colour==(Point_Collect.list.get(i)+1)){
//									
//								ColorStr="1";
//								Preferences.saveColour(Data_Receive.this, ColorStr);
//								}else{
//									ColorStr="0";
//									Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)};
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }

                    } else if ((mnNextRevPoint == ConstantDef.MAX_PER_STORAGE_COUNT) || (mnNextRevPoint == mnORGMaxPointCnt)) {
                        Log.e("25252525", String.valueOf(mnNextRevPoint));
                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), true);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//						{Log.e("i", i+"");
//								if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//							
//									BS="1";
//								}else{
//									BS="0";
//								}
//							}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] items = {

                                    Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,};
                            addReceiveData(items);
//							for (int i = 0; i <Point_Collect.list.size(); i++) 
//							{
//								Log.e("i", Point_Collect.list.get(i)+"");
//								int colour=Integer.parseInt(tv_num.getText().toString());
//								if(colour==(Point_Collect.list.get(i)+1)){
//								ColorStr="1";
//								Preferences.saveColour(Data_Receive.this, ColorStr);
//								}else{
//									ColorStr="0";
//									Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)};
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }
                    }
                    System.out.println("mnNextRevPoint = " + mnNextRevPoint);
                    // 是否全部读取完毕
                    if (mnNextRevPoint <= mnORGMaxPointCnt) {

                        if (mnNextRevPoint <= ConstantDef.MAX_PER_STORAGE_COUNT) {
                            BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_Send_ReadStorageData.getSendCmd(ConstantDef.DATASPACE0_START_ADDR + (mnNextRevPoint - 1) * ConstantDef.DATA_REV_POINT_DATA_BYTES, true), TransProtocol.CMD_TYPE_READ_STORAGE_DATA0);
                            resetCtrlParam();
                        } else {// 读取第二片数据区
                            BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_Send_ReadStorageData.getSendCmd(ConstantDef.DATASPACE1_START_ADDR + (mnNextRevPoint - 1 - ConstantDef.MAX_PER_STORAGE_COUNT) * ConstantDef.DATA_REV_POINT_DATA_BYTES, false), TransProtocol.CMD_TYPE_READ_STORAGE_DATA1);
                            resetCtrlParam();
                        }
                    } else {
                        System.out.println("CMD_STOP 0001");
                        // 发送STOP命令表示通讯结束
                        BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                        resetCtrlParam();
                    }

                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_STORAGE_DATA1) {
                    // 接收第二片存储区数据
                    byte[] bytes = null;
                    if ((mnNextRevPoint <= (ConstantDef.MAX_POINT_COUNT - 1)) && (mnNextRevPoint < mnORGMaxPointCnt)) {
                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), true);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//							{Log.e("i", i+"");
//								if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//										BS="1";
//									}else{
//										BS="0";
//									}
//									
//								}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] items = {

                                    Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,};
                            addReceiveData(items);
//							for (int i = 1; i <Point_Collect.listcolor.size(); i++) 
//							{
//								if(mStructEnsurePoint.id==i){
//									Log.e("", mStructEnsurePoint.id+"");
//									//Log.e("", mStructEnsurePoint.i+"");
//								ColorStr="1";
//								Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//								else{
//									ColorStr="0";
//									Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)};
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }

                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), false);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//							{Log.e("i", i+"");
//								if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//										BS="1";
//									}else{
//										BS="0";
//									}
//									
//								}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] items = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,};
                            addReceiveData(items);
//							for (int i = 0; i <Point_Collect.list.size(); i++) 
//							{
//								Log.e("mStructEnsurePoint.id", mStructEnsurePoint.id+"");
//								
//								Log.e("i", Point_Collect.list.get(i)+"");
//								int colour=Integer.parseInt(tv_num.getText().toString());
//								Log.e("colour",colour+"");
//								if(colour==(Point_Collect.list.get(i)+1)){
//									
//								ColorStr="1";
//								Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//								else{
//									ColorStr="0";
//									Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)};
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }
                    } else if ((mnNextRevPoint == ConstantDef.MAX_POINT_COUNT) || (mnNextRevPoint == mnORGMaxPointCnt)) {

                        bytes = ANS_Rev_Read_Storage_data.getAnsBytes(BluetoothTools.readData(intent), true);
                        mnNextRevPoint += 1;
                        // 写原始测量数据文件
                        FileUtils.appendWriteFile(mYSFilePath, bytes);
                        updateProgress();
                        // 判断是否是匹配的采集点
                        if (mStructEnsurePoint.id == (mnNextRevPoint - 1)) {
                            FileUtils.appendWriteFile(mSJFilePath, bytes);
                            mStructReadStorage = ANS_Rev_Read_Storage_data.getReadStorageData_Struct(bytes);
//							for (int i = 0; i <Point_Collect.listcolor.size(); i++) 
//							{Log.e("i", i+"");
//								if(Integer.parseInt(Point_Collect.listcolor.get(i))==mStructEnsurePoint.id){
//										BS="1";
//									}else{
//										BS="0";
//									}
//									
//								}
//							if(BS=="1"){
//								BS="×";
//							}else{
//								BS="√";
//							}
                            String[] items = {

                                    Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), df3.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df3.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), Integer.toString(mStructEnsurePoint.id), number2time(mStructEnsurePoint.time), BS,};
                            addReceiveData(items);
//							for (int i = 0; i <Point_Collect.list.size(); i++) 
//							{
//								Log.e("mStructEnsurePoint.id", mStructEnsurePoint.id+"");
//								Log.e("i", Point_Collect.list.get(i)+"");
//								int colour=Integer.parseInt(tv_num.getText().toString());
//								Log.e("colour",colour+"");
//								if(colour==(Point_Collect.list.get(i)+1)){
//									
//								ColorStr="1";
//								Preferences.saveColour(Data_Receive.this, ColorStr);
//								}else{
//									ColorStr="0";
//									Preferences.saveColour(Data_Receive.this, ColorStr);
//								}
//							}
                            String[] itemss = {Integer.toString(mnCurEnsurePointCnt), mStructEnsurePoint.deep, df1.format(Double.parseDouble(mStructReadStorage.angle)), df1.format(Double.parseDouble(mStructReadStorage.position)), mStructEnsurePoint.type, df1.format(Double.parseDouble(mStructReadStorage.magnetic)), df.format(Double.parseDouble(mStructReadStorage.checksum)), df1.format(Double.parseDouble(mStructReadStorage.tempera)), df1.format(Double.parseDouble(mStructReadStorage.gravity)), df.format(Double.parseDouble(mStructReadStorage.voltage)), number2time(mStructEnsurePoint.time), Integer.toString(mStructEnsurePoint.id)};
                            excledatas.add(array2map(itemss));
                            byte[] bytesEnsure = FileUtils.RandomReadFile(mYXFilePath, ConstantDef.FILE_VALID_POINT_CNT_BYTES + mnCurEnsurePointCnt * ConstantDef.FILE_VALID_POINT_SINGLE_BYTES, ConstantDef.FILE_VALID_POINT_SINGLE_BYTES);
                            if (bytesEnsure != null) {
                                FileUtils.appendWriteFile(mSJFilePath, bytesEnsure);
                                mStructEnsurePoint = Efficient_Point.getEnsurePointStruct(bytesEnsure);
                                mnCurEnsurePointCnt += 1;
                            }
                        }
                    }
                    System.out.println("mnNextRevPoint = " + mnNextRevPoint);
                    // 是否全部读取完毕
                    if (mnNextRevPoint <= mnORGMaxPointCnt) {
                        if (mnNextRevPoint <= ConstantDef.MAX_POINT_COUNT) {
                            BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_Send_ReadStorageData.getSendCmd(ConstantDef.DATASPACE1_START_ADDR + (mnNextRevPoint - 1 - ConstantDef.MAX_PER_STORAGE_COUNT) * ConstantDef.DATA_REV_POINT_DATA_BYTES, false), TransProtocol.CMD_TYPE_READ_STORAGE_DATA1);
                            resetCtrlParam();
                        } else {
                            System.out.println("CMD_STOP 0002");
                            // 发送STOP命令表示通讯结束
                            BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                            resetCtrlParam();
                        }
                    } else {
                        System.out.println("CMD_STOP 0003");
                        // 发送STOP命令表示通讯结束
                        BluetoothTools.sendData(Data_Receive.this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_END);
                        resetCtrlParam();
                    }

                } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_END) {
                    // 停止命令应答，分析应答数据
                    if (!Arrays.equals(BluetoothTools.readData(intent), TransProtocol.CMD_STOP)) {
                        Utils.toast(Data_Receive.this, "数据接收停止命令校验失败");
                        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                        return;
                    }
                    // 数据接收更新进度
                    TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                }



                tv_count.setText(mnNextRevPoint-1+"/"+mnORGMaxPointCnt);
            }
        }
    };

    /*
     * 复位命令控制参数
     */
    private void resetCtrlParam() {
        mReSendTime = ConstantDef.MAX_RESEND_TIME;
        mReSendCnt = ConstantDef.MAX_RESEND_CNT;
    }

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            Log.v(TAG, "mReSendCnt = " + mReSendCnt + " mReSendTime = " + mReSendTime);
            if ((TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_STOP) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_COLLECTCNT_ETC) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_STORAGE_DATA0) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_READ_STORAGE_DATA1) || (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_END)) {
                if (mReSendCnt > 0) {
                    if (--mReSendTime < 1) {
                        mReSendCnt--;
                        mReSendTime = ConstantDef.MAX_RESEND_TIME;
                    }
                } else {
                    Utils.toast(Data_Receive.this, "数据接收模块 应答超时！");
                    stopTimer();
                    TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
                    return;
                }
                mHandler.postDelayed(this, 1000);
            } else if (TransProtocol.getCurCMDType() == TransProtocol.CMD_TYPE_NONE) {
                mnProgress += 5;
                if (mnProgress > mProgressBar.getMax()) mnProgress = mProgressBar.getMax();
                mProgressBar.setProgress(mnProgress);
                if (mnProgress == mProgressBar.getMax()) {
                    stopTimer();
                    System.out.println("Data Receive Over");
                    CreateExcel ce = new CreateExcel();
                    String head[] = {"序号", "孔深 m", "倾角 °", "磁方位角 °", "备注", "磁场强度 μT", "校验和", "温度 ℃", "重力高边 °", "电压V", "选点时间", "点号"};
                    ce.pullArray(ConstantDef.EXCEL_PATH, ConstantDef.EXCEL_PATH + filename, head, excledatas);
                    pullok("文件路径:" + EXCEL_PATH_DIALOG + "\n文件名称:" + filename + ".xls");
                    Utils.toast(Data_Receive.this, "数据接收完成！");

                    //Data_Receive.this.finish();
                    return;
                }
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
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.data_receive);

        //acquireWakeLock();
            if (Preferences.getBenString(this).equals("P")) {
                EXCEL_PATH_6();
                mYXFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;
                mYSFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_YS_POINT;
                mSJFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_SJ_POINT;
                filename = Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-"  + Preferences.getHoleIDString(this);
            } else {
                EXCEL_PATH_7();
                mYXFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_VALID_POINT;
                mYSFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_YS_POINT;
                mSJFilePath = ConstantDef.FILE_PATH + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + ConstantDef.FILE_SUFFIX_SJ_POINT;
                filename = Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this);
            }
        init();
    }
private  String EXCEL_PATH_DIALOG;
    private void EXCEL_PATH_6() {
        ConstantDef.EXCEL_PATH = Environment.getExternalStorageDirectory() + "/HKEXCLE/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
        EXCEL_PATH_DIALOG = "手机内存" +                                      "/HKEXCLE/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
        ConstantDef.FOLDER_NAME =   Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) ;
    }

    private void EXCEL_PATH_7() {
        ConstantDef.EXCEL_PATH = Environment.getExternalStorageDirectory() + "/HKEXCLE/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
        EXCEL_PATH_DIALOG = "手机内存" +                                      "/HKEXCLE/" + Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this) + "/";
        ConstantDef.FOLDER_NAME = Preferences.getFangString(this) + "-" + Preferences.getBenString(this) + "-" + Preferences.getFaceString(this) + "-" + Preferences.getDrillingString(this) + "-" + Preferences.getHoleIDString(this) + "-" + Preferences.getTime(this) + "-" + Preferences.getChongMString(this);
    }
    private void init() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_READ_DATA);
        registerReceiver(bluetoothTransReceiver, intentFilter);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_data_receive);
        tv_count = (TextView) findViewById(R.id.tv_count);
        guiji = (Button) findViewById(R.id.guiji);
        exitL = (Button) findViewById(R.id.exitL);
        guiji.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("path", ConstantDef.FOLDER_NAME);
                intent.setClass(Data_Receive.this, Data.class);
                startActivity(intent);
//                CreateExcel ce = new CreateExcel();
//                String head[] = {"序号", "孔深 m", "倾角 °", "磁方位角 °", "备注", "磁场强度 μT", "校验和", "温度 ℃", "重力高边 °", "电压V", "选点时间", "点号"};
//                ce.pullArray(ConstantDef.EXCEL_PATH, filename, head, excledatas);
//                pullok("        文件导出路径为" + filename);
            }
        });
        exitL.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(Data_Receive.this);
                builder.setTitle("数据接收完成,您确定要返回主页？").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 退出界面
                        Data_Receive.this.finish();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).setCancelable(false).show();
            }
        });
        // 发送STOP指令
        BluetoothTools.sendData(this, TransProtocol.CMD_STOP, TransProtocol.CMD_TYPE_STOP);
        resetCtrlParam();
        // 开始计时
        startTimer();

        // String[] data =
        // {"0","1000","00:58:50","1","0.4","148.7","54.78","1.002","18.74","3.944"};
        // for(int i=0; i<15; i++) {
        // data[0] = String.valueOf(i);
        // addReceiveData(data);
        // }
    }

    private void addReceiveData(String[] item) {
        TableLayout table = (TableLayout) findViewById(R.id.tl_receive_data);

        TableRow tablerow = new TableRow(this);
        TableRow.LayoutParams lparams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tablerow.setLayoutParams(lparams);

        tv_num = new TextView(this);
        tv_num.setGravity(Gravity.CENTER);
        tv_num.setTextSize(20);
        tv_num.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_num.setBackgroundResource(R.drawable.bg_item_third);
        tv_num.setTypeface(Typeface.DEFAULT_BOLD);
        tv_num.setText(item[0]);
        tablerow.addView(tv_num);

//		TextView tv_biaoshi = new TextView(this);
//		tv_biaoshi.setGravity(Gravity.CENTER);
//		tv_biaoshi.setTextSize(20);
//		tv_biaoshi.setTextColor(Color.rgb(0x40, 0x40, 0x40));
//		tv_biaoshi.setBackgroundResource(R.drawable.bg_item_third);
//		tv_biaoshi.setTypeface(Typeface.DEFAULT_BOLD);
//		tv_biaoshi.setText(item[11]);
//		tablerow.addView(tv_biaoshi);

        TextView tv_deep = new TextView(this);
        tv_deep.setGravity(Gravity.RIGHT);
        tv_deep.setTextSize(20);
        tv_deep.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_deep.setBackgroundResource(R.drawable.bg_item_third);
        tv_deep.setTypeface(Typeface.DEFAULT_BOLD);
        tv_deep.setText(item[1]);
        tablerow.addView(tv_deep);


        TextView tv_id = new TextView(this);
        tv_id.setGravity(Gravity.RIGHT);
        tv_id.setTextSize(20);
        tv_id.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_id.setBackgroundResource(R.drawable.bg_item_third);
        tv_id.setTypeface(Typeface.DEFAULT_BOLD);
        tv_id.setText(item[2]);
        tablerow.addView(tv_id);

        TextView tv_dipangle = new TextView(this);
        tv_dipangle.setGravity(Gravity.RIGHT);
        tv_dipangle.setTextSize(20);
        tv_dipangle.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_dipangle.setBackgroundResource(R.drawable.bg_item_third);
        tv_dipangle.setTypeface(Typeface.DEFAULT_BOLD);
        tv_dipangle.setText(item[3]);
        tablerow.addView(tv_dipangle);

        TextView tv_posandle = new TextView(this);
        tv_posandle.setGravity(Gravity.RIGHT);
        tv_posandle.setTextSize(20);
        tv_posandle.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_posandle.setBackgroundResource(R.drawable.bg_item_third);
        tv_posandle.setTypeface(Typeface.DEFAULT_BOLD);
        tv_posandle.setText(item[4]);
        tablerow.addView(tv_posandle);

        TextView tv_magnetic = new TextView(this);
        tv_magnetic.setGravity(Gravity.RIGHT);
        tv_magnetic.setTextSize(20);
        tv_magnetic.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_magnetic.setBackgroundResource(R.drawable.bg_item_third);
        tv_magnetic.setTypeface(Typeface.DEFAULT_BOLD);
        tv_magnetic.setText(item[5]);
        tablerow.addView(tv_magnetic);

        TextView tv_checksum = new TextView(this);
        tv_checksum.setGravity(Gravity.RIGHT);
        tv_checksum.setTextSize(20);
        tv_checksum.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_checksum.setBackgroundResource(R.drawable.bg_item_third);
        tv_checksum.setTypeface(Typeface.DEFAULT_BOLD);
        tv_checksum.setText(item[6]);
        tablerow.addView(tv_checksum);

        TextView tv_temperature = new TextView(this);
        tv_temperature.setGravity(Gravity.RIGHT);
        tv_temperature.setTextSize(20);
        tv_temperature.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_temperature.setBackgroundResource(R.drawable.bg_item_third);
        tv_temperature.setTypeface(Typeface.DEFAULT_BOLD);
        tv_temperature.setText(item[7]);
        tablerow.addView(tv_temperature);

        TextView tv_gravity = new TextView(this);
        tv_gravity.setGravity(Gravity.RIGHT);
        tv_gravity.setTextSize(20);
        tv_gravity.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_gravity.setBackgroundResource(R.drawable.bg_item_third);
        tv_gravity.setTypeface(Typeface.DEFAULT_BOLD);
        tv_gravity.setText(item[9]);
        tablerow.addView(tv_gravity);


        TextView tv_voltage = new TextView(this);
        tv_voltage.setGravity(Gravity.RIGHT);
        tv_voltage.setTextSize(20);
        tv_voltage.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_voltage.setBackgroundResource(R.drawable.bg_item_third);
        tv_voltage.setTypeface(Typeface.DEFAULT_BOLD);
        tv_voltage.setText(item[8]);
        tablerow.addView(tv_voltage);

        TextView tv_time = new TextView(this);
        tv_time.setGravity(Gravity.RIGHT);
        tv_time.setTextSize(20);
        tv_time.setTextColor(Color.rgb(0x40, 0x40, 0x40));
        tv_time.setBackgroundResource(R.drawable.bg_item_third);
        tv_time.setTypeface(Typeface.DEFAULT_BOLD);
        tv_time.setText(item[10]);
        tablerow.addView(tv_time);


        table.addView(tablerow);
    }

    /**
     * 更新进度条
     */
    private void updateProgress() {
        mnProgress += 1;
        if (mnProgress > mProgressBar.getMax()) {
            mnProgress = mProgressBar.getMax();

        }
        mProgressBar.setProgress(mnProgress);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
//			AlertDialog.Builder builder = new AlertDialog.Builder(
//					Data_Receive.this);
//			builder.setTitle("数据接收完成您确定要退出？")
//					.setIcon(android.R.drawable.ic_dialog_alert)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// 退出界面
//									Data_Receive.this.finish();
//									
//								}
//							})
//					.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//										int which) {
//									// TODO Auto-generated method stub
//								}
//							}).show();
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
        unregisterReceiver(bluetoothTransReceiver);
        TransProtocol.setCurCMDType(TransProtocol.CMD_TYPE_NONE);
        releaseWakeLock();
        super.onDestroy();
    }

//	// 申请设备电源锁
//	public void acquireWakeLock() {
//		if (null == mWakeLock) {
//			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
//					"NotificationService");
//			if (null != mWakeLock) {
//				mWakeLock.acquire();
//			}
//		}
//	}

    // 释放设备电源锁
    public void releaseWakeLock() {
//		if (null != mWakeLock) {
//			mWakeLock.release();
//			mWakeLock = null;
//		}
    }

    /***
     * @author zgl
     * 2014年12月31日14:39:47
     */
    private void pullok(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Data_Receive.this);
        builder.setTitle("文件导出路径及名称").setMessage(s).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitL.setVisibility(Button.VISIBLE);
                guiji.setVisibility(Button.VISIBLE);

            }
        }).setCancelable(false).show();
    }

    /***
     * @return
     * @author zgl
     * 2014年12月31日15:32:34
     */
    private Map<String, String> array2map(String[] ss) {
        Map<String, String> data = new HashMap<String, String>();
        String head[] = {"序号", "孔深 m", "倾角 °", "磁方位角 °", "备注", "磁场强度 μT", "校验和", "温度 ℃", "重力高边 °", "电压V", "选点时间", "点号"};

        for (int i = 0; i < ss.length; i++) {
            data.put(head[i], ss[i]);
        }
        return data;
    }

    /***
     * @author zgl
     * 2014年12月31日15:32:34
     */

    private String number2time(String number) {
        String[] time = number.split("");
        return "" + time[1] + time[2] + ":" + time[3] + time[4] + ":" + time[5] + time[6];
    }
}
