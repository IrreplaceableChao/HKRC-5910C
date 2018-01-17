package com.android.utils;

import android.os.Environment;

/**
 * 常量定义类
 * 
 * @author TY
 * 
 */
public class ConstantDef {

	//public static final String BLUETOOTH_NAME_BEGIN =Preferences.getBluetooth(BluetoothClientService.this);//Leaning_PipeAdapter.class.getName().toString();//"lin";//"59A";

	// 蓝牙连接状态
	public static String CUR_BLUETOOTH_STATUS = null;

	// 电压合格阀值
	public static final float STANDARD_VOLTAGE = 4.7f;

	// 设置密码
	public static final String SETTING_PASSWORD = "bjhk";

	// N 无应答最大时长
	public static final int MAX_RESEND_TIME = 3;
	// M 命令重发最大次数
	public static final int MAX_RESEND_CNT = 3;
	// 自检模块最长时间
	public static final int MAX_CHK_TIME = 3600;

	// 确定有效点--无效状态
	public static final int STATUS_INVALID = -1;
	// 确定有效点--延时状态
	public static final int STATUS_DELAY = 0;
	// 确定有效点--确定有效点状态
	public static final int STATUS_ENSURE_POINT = 1;
	// 确定有效点--采集已满状态
	public static final int STATUS_POINT_FULL = 2;

	public static final String STR_STATUS_INVALID = "无效状态";
	public static final String STR_STATUS_DELAY = "延时状态";
	public static final String STR_STATUS_ENSURE_POINT = "采集状态";
	public static final String STR_STATUS_POINT_FULL = "测量探管采集已满";

	//文件夹名称
	public static String FOLDER_NAME = null;
	// 文件夹目录
	public static String DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/HKCX-SJ/";
	// 文件存储路径
	public static String FILE_PATH = null;
	// 文件存储路径
	public static String EXCEL_PATH = null;
	// 有效点数据存储文件后缀
	public static final String FILE_SUFFIX_VALID_POINT = ".YX";
	// 原始测量数据文件后缀
	public static final String FILE_SUFFIX_YS_POINT = ".YS";
	// 有效点测量数据文件后缀
	public static final String FILE_SUFFIX_SJ_POINT = ".SJ";

	// 确定有效点文件结构-有效点点数占用字节数
	public static final int FILE_VALID_POINT_CNT_BYTES = 2;
	// 确定有效点文件结构-单个有效点占用字节数
	public static final int FILE_VALID_POINT_SINGLE_BYTES = 16;

	// 机芯采集点单组数据长度
	public static final int DATA_REV_POINT_DATA_BYTES = 12;
	// 每个存储区最大存储点数
	public static final int MAX_PER_STORAGE_COUNT = 10500;
	// 最大采集点数目
	public static final int MAX_POINT_COUNT = 21000;
	// 第一片存储区起始地址
	public static final int DATASPACE0_START_ADDR = 0x20000;
	// 第二片存储区起始地址
	public static final int DATASPACE1_START_ADDR = 0x40000;
}
