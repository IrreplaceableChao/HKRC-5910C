package com.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import org.apache.commons.codec.binary.Base64;
import android.util.Log;

import static com.android.utils.PublicValues.info;

/**
 * 状态数据存储类
 * 
 * @author TY
 * 
 */
public class Preferences {

	/**
	 * 记录延时时间和间隔时间
	 * 
	 * @param delay
	 *            -----延时时间(分)
	 * @param interval
	 *            --间隔时间(秒)
	 */
	public static void saveDelayandIntervalTime(Context context, int delay,
			int interval) {
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putInt("DELAY_TIME", delay);
		editor.putInt("INTERVAL_TIME", interval);
		editor.commit();
	}

	/**
	 * 获取记录的延时时间
	 * 
	 * @return
	 */
	public static int getDelayTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getInt("DELAY_TIME", 1);
	}

	/**
	 * 获取记录的间隔时间
	 * 
	 * @return
	 */
	public static int getIntervalTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getInt("INTERVAL_TIME", 2);
	}

	/**
	 * 记录启动时间
	 * 
	 * @param context
	 * @param time
	 */
	public static void saveStartTime(Context context, long time) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putLong("START_TIME", time);
		editor.commit();
	}

	/**
	 * 获取启动时间
	 * 
	 * @param context
	 * @return
	 */
	public static long getStartTime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getLong("START_TIME", -1);
	}
	
	/**
	 * 存储重名
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static void saveChongMString(Context context, String ChongmStr) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("ChongM", ChongmStr);
		editor.commit();
	}

	/**
	 * 获取重名
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static String getChongMString(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("ChongM", " ");
	}

	/**
	 * 存储颜色
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static void saveColour(Context context, String ColorStr) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Color", ColorStr);
		editor.commit();
	}

	/**
	 * 获取颜色
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static String getColour(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("Color", "0");
	}
	
	
	
	/**
	 * 存储蓝牙符串
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static void saveBluetooth(Context context, String bluetoothStr) {
		Editor editor = context.getSharedPreferences("Mian",
				Context.MODE_PRIVATE).edit();
		editor.putString("Bluetooth", bluetoothStr);
		editor.commit();
	}
	/**
	 * 获取蓝牙字符串
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static String getBluetooth(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("Mian",
				Context.MODE_PRIVATE);
		return prefs.getString("Bluetooth", "58C");
	}
	
	
	/**
	 * 存储工作面字符串
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static void saveFaceString(Context context, String faceStr) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Face", faceStr);
		editor.commit();
	}
	/**
	 * 获取工作面字符串
	 * 
	 * @param context
	 * @param faceStr
	 */
	public static String getFaceString(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("Face", "40807");
	}
	/**
	 * 存储钻场字符串
	 * 
	 * @param context
	 * @param drillingStr
	 */
	public static void saveDrillingString(Context context, String drillingStr) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Drilling", drillingStr);
		editor.commit();
	}
	/**
	 * 获取钻场字符串
	 * 
	 * @param context
	 * @param drillingStr
	 */
	 public static String getDrillingString(Context context) {
			SharedPreferences prefs = context.getSharedPreferences("SET_START",
					Context.MODE_PRIVATE);
			return prefs.getString("Drilling", "1");
		}
	/**
	 * 存储孔号字符串
	 * 
	 * @param context
	 * @param holeStr
	 */
	public static void saveHoleIDString(Context context, String holeStr) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("HOLE_ID", holeStr);
		editor.commit();
	}

	/**
	 * 获取孔号字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getHoleIDString(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("HOLE_ID", "1");
	}
	/**
	 * 存储编号字符串
	 * 
	 * @param context
	 * @param holeStr
	 */
	public static void saveBhsInt(Context context, int list) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putInt("1", list);
		editor.commit();
	}

	/**
	 * 获取编号字符串
	 * 
	 * @param context
	 * @return
	 */
	public static int getBhsInt(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getInt("list", 1);
	}

	
	
	
	
	/**
	 * 存储本煤层孔字符串
	 * 
	 * @param context
	 * @return
	 */
	public static void saveBenString(Context context, String benStr){
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Ben", benStr);
		editor.commit();
		
		}
	/**
	 * 获取本煤层孔字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getBenString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("Ben", "B");
	}
	/**
	 * 存储防治水孔字符串
	 * 
	 * @param context
	 * @return
	 */
	public static void saveFangString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("Fang", fangStr);
		editor.commit();
		
		}
	/**
	 * 获取防治水孔字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getFangString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("Fang", "F");
	}
	/**
	 * 保存时间
	 * 
	 * 
	 * 
	 *            -
	 */
	public static void saveTime(Context context,String strTime) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Time", strTime);
		editor.commit();
	}
	/**
	 * 获取时间字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getTime(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("Time", "141128");
	}
	
	
	/**
	 * 保存总秒数
	 * 
	 * 
	 * 
	 *            -
	 */
	public static void saveSecond(Context context,long zms) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putLong("Second", zms);
		editor.commit();
	}
	/**
	 * 获取总秒数
	 * 
	 * @param context
	 * @return
	 */
	public static long getSecond(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getLong("Second", 0);
	}
	
	
	/**
	 * 保存重名文件后缀
	 * 
	 * @param context
	 * @return
	 */
	public static void saveHz(Context context,String strHz) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Hz", strHz);
		editor.commit();
	}
	/**
	 * 获取重名文件后缀
	 * 
	 * @param context
	 * @return
	 */
	public static void getHz(Context context,String strHz) {
		Editor editor = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE).edit();
		editor.putString("Hz", "1");
		editor.commit();
	}
	/**
	 * 保存孔深生成方式手动or自动
	 * 
	 * @param context
	 * @param bManual
	 *            --是否为手动
	 */
	public static void saveDeepManual(Context context, boolean bManual) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putBoolean("DEEP_MANUAL", bManual);
		editor.commit();
	}

	/**
	 * 获取孔深生成方式是否为手动,默认为手动
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getDeepManual(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getBoolean("DEEP_MANUAL", false);
	}

	/**
	 * 保存测量方式起钻or下钻
	 * 
	 * @param context
	 * @param bToUp
	 *            --是否为起钻
	 */
	public static void saveMeasureWayToUp(Context context, boolean bToUp) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putBoolean("MEASURE_WAY_TOUP", bToUp);
		editor.commit();
	}

	/**
	 * 获取测量方式是否为起钻,默认为起钻
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getMeasureWayToUp(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getBoolean("MEASURE_WAY_TOUP", false);
	}

	/**
	 * 保存起测点孔深
	 * 
	 * @param context
	 * @param startdeep
	 *            --起测点孔深
	 */
	public static void saveStartDeep(Context context, String startdeep) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putString("START_DEEP", startdeep);
		editor.commit();
	}

	/**
	 * 获取起测点孔深,默认为1000.0f
	 * 
	 * @param context
	 * @return
	 */
	public static String getStartDeep(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getString("START_DEEP", "300.0");
	}

	/**
	 * 保存测量间隔距离
	 * 
	 * @param context
	 * @param intervalLen
	 *            --测量间隔
	 */
	public static void saveMeasureInterval(Context context, String intervalLen) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putString("INTERVAL_LEN", intervalLen);
		editor.commit();
	}

	/**
	 * 获取测量间隔距离
	 * 
	 * @param context
	 * @return
	 */
	public static String getMeasureInterval(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getString("INTERVAL_LEN", "1.5");
	}

	/**
	 * 记录出厂时间
	 */
	public static void saveProduceDate(Context context, String date) {
		Editor editor = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE).edit();
		editor.putString("PRODUCE_DATE", date);
		editor.commit();
	}

	/**
	 * 获取出厂时间
	 * 
	 * @param context
	 * @return
	 */
	public static String getProduceDate(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE);
		return prefs.getString("PRODUCE_DATE", "2014-01-01");
	}

	/**
	 * 记录与机芯配对码
	 */
	public static void saveCodeToHeart(Context context, String code) {
		Editor editor = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE).edit();
		editor.putString("CODE_TO_HEART", code);
		editor.commit();
	}

	/**
	 * 获取记录的与机芯的配对码
	 * 
	 * @param context
	 * @return
	 */
	public static String getCodeToHeart(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE);
		return prefs.getString("CODE_TO_HEART", "1234");
	}

	/**
	 * 记录机芯仪器编号
	 */
	public static void saveMachineNum(Context context, String num) {
		Editor editor = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE).edit();
		editor.putString("MACHINE_NUM", num);
		editor.commit();
	}

	/**
	 * 获取机芯仪器编号
	 * 
	 * @param context
	 * @return
	 */
	public static String getMachineNum(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("SOFT_CONFIG",
				Context.MODE_PRIVATE);
		return prefs.getString("MACHINE_NUM", "10000001");
	}
	
	/**
	 * 保存当前步骤
	 * 
	 * @param context
	 * @param step 步骤
	 */
	public static void saveStep(Context context, int step) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putInt("STEP", step);
		editor.commit();
	}

	/**
	 * 获取步骤
	 * 
	 * @param context
	 * @return
	 */
	public static int getStep(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getInt("STEP", 99);
	}
	
	/**
	 * 保存下一时间
	 * 2014-11-11 11:45:58 zgl添加
	 * @param context
	 * @param time 下一时间
	 */
	public static void saveNextMillistime(Context context, Long time) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putLong("NextMillistime", time);
		editor.commit();
	}

	/**
	 * 获取下一时间
	 * 2014-11-11 11:45:58 zgl添加
	 * @param context
	 * @return
	 */
	public static Long getNextMillistime(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getLong("NextMillistime", -1);
	}
	
	/**
	 * 保存有效点总数
	 * 2014-11-11 19:45:02 zgl添加
	 * @param context
	 * @param time 下一时间
	 */
	public static void saveSumPoint(Context context, int sumpoint) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putInt("SumPoint", sumpoint);
		editor.commit();
	}

	/**
	 * 获取有效点总数
	 * 2014年11月11日19:45:08 zgl添加
	 * @param context
	 * @return
	 */
	public static int getSumPoint(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getInt("SumPoint", 9);
	}
	
	/***
	 * @author zgl
	 * 保存校准间隔
	 * 2015年1月4日11:55:05
	 */
	public static void saveJiaoZhun(Context context, int jiaozhun) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putInt("JiaoZhun", jiaozhun);
		editor.commit();
	}
	
	/***
	 * @author zgl
	 * 获取校准间隔
	 * 2015年1月4日11:55:05
	 */
	public static int getIlist_bhs(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getInt("Ilist_bhs", 0);
	}
	public static void saveIlist_bhs(Context context, int ilist_bhs) {
		Editor editor = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE).edit();
		editor.putInt("Ilist_bhs", ilist_bhs);
		editor.commit();
	}
	
	/***
	 * @author zgl
	 * 获取校准间隔
	 * 2015年1月4日11:55:05
	 */
	public static int getJiaoZhun(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("POINT_COLLECT",
				Context.MODE_PRIVATE);
		return prefs.getInt("JiaoZhun", 0);
	}
	
	
	//地址
	public static void saveAddress(Context context,String address) {
		Editor editor = context.getSharedPreferences("BluetoothClientService",
				Context.MODE_PRIVATE).edit();
		editor.putString("Bt_address", address);
		editor.commit();
	}
	public static String getAddress(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("BluetoothClientService",
				Context.MODE_PRIVATE);
		return prefs.getString("Bt_address", null);
	}
	public static void saveOAuth(Context context,List<Integer>  bean) {
		SharedPreferences preferences =mContext.getSharedPreferences("base64", 0);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			// 创建对象输出流，并封装字节流
			oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(bean);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos
					.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString("oAuth_1", oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.i("ok", "存储成功");

		// Intent intent = new Intent(SaveInfoService.this,
		// SaveInfoService.class);
		// startService(intent);

	}

	public static List<Integer> readOAuth() {
		 List<Integer> oAuth_1 = null;
		SharedPreferences preferences = mContext.getSharedPreferences("base64", 0);
		String productBase64 = preferences.getString("oAuth_1", "");
		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
		ObjectInputStream bis = null;
		ByteArrayInputStream bais = null;
		try {
			// 封装到字节流
			bais = new ByteArrayInputStream(base64);
			// 再次封装
			bis = new ObjectInputStream(bais);
			// 读取对象
			oAuth_1 = ( List<Integer>) bis.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bais.close();
				bis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return oAuth_1;

	}

	/**
	 * 存储设计倾角
	 *
	 * @param context
	 * @return
	 */
	public static void saveqinjiaoString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("qinjiao", fangStr);
		editor.commit();

	}
	/**
	 * 获取设计倾角
	 *
	 * @param context
	 * @return
	 */
	public static String getqinjiaoString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("qinjiao",null);
	}

	/**
	 * 存储设计方位
	 *
	 * @param context
	 * @return
	 */
	public static void savefangweiString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("fangwei", fangStr);
		editor.commit();

	}
	/**
	 * 获取设计方位
	 *
	 * @param context
	 * @return
	 */
	public static String getfangweiString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("fangwei", null);
	}
	/**
	 * 存储设计孔深
	 *
	 * @param context
	 * @return
	 */
	public static void savekongshenString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("kongshen", fangStr);
		editor.commit();

	}
	/**
	 * 获取设计孔深
	 *
	 * @param context
	 * @return
	 */
	public static String getkongshenString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("kongshen", null);
	}

	/**
	 * 存储允许上下偏差
	 *
	 * @param context
	 * @return
	 */
	public static void saveshangString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("shang", fangStr);
		editor.commit();

	}

	public static String getshangString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("shang", null);
	}

	/**
	 * 存储允许左右偏差
	 *
	 * @param context
	 * @return
	 */
	public static void savezuoString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("zuo", fangStr);
		editor.commit();

	}

	/**
	 * 获取允许左右偏差
	 *
	 * @param context
	 * @return
	 */
	public static String getzuoString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",
				Context.MODE_PRIVATE);
		return prefs.getString("zuo", null);
	}

	/**
	 * 存储防治水孔字符串
	 *
	 * @param context
	 * @return
	 */
	public static void savezongString(Context context, String fangStr){
		Editor editor = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE).edit();
		editor.putString("zong", fangStr);
		editor.commit();

	}

	/**
	 * 存储防治水孔字符串
	 *
	 * @param context
	 * @return
	 */
	public static String getzongString(Context context){
		SharedPreferences prefs = context.getSharedPreferences("SET_START",Context.MODE_PRIVATE);
		return prefs.getString("zong", null);
	}


	
	private static Context mContext;
	public static void setContext(Context context){
		mContext=context;
	}
}
