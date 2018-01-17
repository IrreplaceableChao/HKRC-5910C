package com.android.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

/**
 * 基本工具类
 * 
 * @author TY
 * 
 */
public class Utils {

	/**
	 * 显示toast提示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void toast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 将string转换成byte[]
	 * 
	 * @eg:汉字转成双字节
	 */
	public static byte[] getBytes(String str) {
		byte[] bytes = null;
		try {
			bytes = str.getBytes("gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 将char转换成byte数组
	 */
	public static byte[] getBytes(char data) {
		byte[] bytes = new byte[2];
		for (int i = 0; i < 2; i++) {
			bytes[i] = (byte) (data >> (8 - i * 8));
		}
		return bytes;
	}

	/**
	 * 将char转换成byte数组,小端模式，高位在高地址
	 */
	public static byte[] getLittleBytes(char data) {
		byte[] bytes = new byte[2];
		for (int i = 0; i < 2; i++) {
			bytes[i] = (byte) (data >> ((i + 1) * 8 - 8));
		}
		return bytes;
	}

	/**
	 * 将short转换成byte数组
	 */
	public static byte[] getBytes(short data) {
		byte[] bytes = new byte[2];
		for (int i = 0; i < 2; i++) {
			bytes[i] = (byte) (data >> (8 - i * 8));
		}
		return bytes;
	}

	/**
	 * 将int转换成byte数组
	 */
	public static byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		for (int i = 0; i < 4; i++) {
			bytes[i] = (byte) (data >> (24 - i * 8));
		}
		return bytes;
	}

	/**
	 * 将long转换成byte数组
	 */
	public static byte[] getBytes(long data) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) (data >> (56 - i * 8));
		}
		return bytes;
	}

	/**
	 * 将float转换成byte数组
	 */
	public static byte[] getBytes(float data) {
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}

	/**
	 * 将double转换成byte数组
	 */
	public static byte[] getBytes(double data) {
		long intBits = Double.doubleToLongBits(data);
		return getBytes(intBits);
	}

	/**
	 * 将byte数组转换成String型
	 */
	public static String getString(byte[] b) {
		String str = null;
		try {
			str = new String(b, "gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将byte数组转换成char型
	 */
	public static char getChar(byte[] b) {
		return (char) ((0xff & b[1]) | (0xff00 & (b[0] << 8)));
	}

	/**
	 * 将byte数组转换成short型
	 */
	public static short getShort(byte[] b) {
		return (short) ((0xff & b[1]) | (0xff00 & (b[0] << 8)));
	}

	/**
	 * 将byte数组转换成int型
	 * 
	 * @param offset
	 *            --数组内偏移
	 */
	public static int getInt(byte[] b, int offset) {
		return (int) (0xff & b[3 + offset]) | (0xff00 & (b[2 + offset] << 8))
				| (0xff0000 & (b[1 + offset] << 16))
				| (0xff000000 & (b[0 + offset] << 24));
	}

	/**
	 * 将byte数组转换成long型
	 */
	public static long getLong(byte[] b) {
		return (0xffL & (long) b[7]) | (0xff00L & ((long) b[6] << 8))
				| (0xff0000L & ((long) b[5] << 16))
				| (0xff000000L & ((long) b[4] << 24))
				| (0xff00000000L & ((long) b[3] << 32))
				| (0xff0000000000L & ((long) b[2] << 40))
				| (0xff000000000000L & ((long) b[1] << 48))
				| (0xff00000000000000L & ((long) b[0] << 56));
	}

	/**
	 * 将byte数组转换成float型
	 */
	public static float getFloat(byte[] bytes, int offset) {
		return Float.intBitsToFloat(getInt(bytes, offset));
	}

	/**
	 * 将byte数组转换成double型
	 */
	public static double getDouble(byte[] bytes) {
		return Double.longBitsToDouble(getLong(bytes));
	}

	/**
	 * 将两个ASCII字符合成一个byte型
	 * 
	 * @eg:"EF"–> 0xEF
	 */
	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 将指定String，以每两个字符分割转换为16进制形式byte[]
	 * 
	 * @eg:"20131016" –> byte[]{0x20, 0x13, 0x10, 0x16}
	 */
	public static byte[] hexStringToBytes(String src) {
		if (src.length() % 2 != 0)
			return null;
		byte[] ret = new byte[src.length() / 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < src.length() / 2; i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}
		return ret;
	}

	/**
	 * byte[]转换成对应16进制String
	 * 
	 * @eg:byte[]{0x20, 0x13, 0x10, 0x16} –> "20131016"
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取小数点位数InputFilter
	 * 
	 * @param diglen
	 *            --小数点后保留位数
	 * @return
	 */
	public static InputFilter getDotInputFilter(final int doglen) {
		InputFilter lengthfilter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// 删除等特殊字符，直接返回
				if ("".equals(source.toString())) {
					return null;
				}
				String dValue = dest.toString();
				String[] splitArray = dValue.split("\\.");
				if (splitArray.length > 1) {
					String dotValue = splitArray[1];
					int diff = dotValue.length() + 1 - doglen;
					if (diff > 0) {
						return source.subSequence(start, end - diff);
					}
				}
				return null;
			}
		};
		return lengthfilter;
	}

	/**
	 * 获取长度限制InputFilter
	 * 
	 * @param length
	 *            --字符长度
	 * @return
	 */
	public static InputFilter getLengthInputFilter(final int length) {
		InputFilter lengthfilter = new InputFilter.LengthFilter(length);
		return lengthfilter;
	}

	/**
	 * 获取当前系统日期
	 * 
	 * @eg:131216
	 */
	public static String getCurDateString() {
		Time localTime = new Time("Asia/Hong_Kong");
		localTime.setToNow();
		String date = localTime.format("%Y%m%d");
		return  date.substring(2);
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @eg:221106
	 */
	public static String getCurTimeString() {
		Time localTime = new Time("Asia/Hong_Kong");
		localTime.setToNow();
		return localTime.format("%H%M%S");
	}

	/**
	 * 获取当前系统时间
	 * 
	 * @return--Time
	 */
	public static Time getCurTime() {
		Time localTime = new Time("Asia/Hong_Kong");
		localTime.setToNow();
		return localTime;
	}

	/**
	 * 将毫秒转化为时间格式
	 * 
	 * @param time
	 * @return @eg:22:11:06
	 */
	public static String millisToTimeStrColon(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(time);
	}

	/**
	 * 将毫秒转化为时间格式
	 * 
	 * @param time
	 * @return @eg:221106
	 */
	public static String millisToTimeStr(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
		return formatter.format(time);
	}
	
	/***
	 * 将保存的时间转化为   时：分：秒
	 * @param str 格式：时分秒
	 * @return
	 */
	public static String strToTime(String str){
		String time[] = str.split("");
		return time[1]+time[2]+":"+time[3]+time[4]+":"+time[5]+time[6];
	}
	
	 /**
     *解析POINT_COLLECT.xml文件
     * @author zgl
     * 
     */
	public static POINT_COLLECT parsePOINT_COLLECT(String fileName){
		InputStream is = null;
		POINT_COLLECT point_collect = new POINT_COLLECT();
		try {
			is = new FileInputStream(new File(fileName));
			// 使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");

			int eventType = parser.getEventType();
			String name = "";
			eventType = parser.nextTag();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;

				case XmlPullParser.START_TAG:
					Logger.d("myLogger", "START_TAG_");
					if ("map".equals(parser.getName())) {
//
					} else if ("long".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("int".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("boolean".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("string".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					}else if ("list".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					}
					if (null != name) {
						if ("NextMillistime".equals(name)) {
							Logger.d("START_TAG_NextMillistime", parser.getAttributeValue(1));
							point_collect.setNextMillistime(Long.parseLong(parser.getAttributeValue(1)) );
						
						} else if ("START_DEEP".equals(name)) {

						
						} else if ("STEP".equals(name)) {
							Logger.d("START_TAG_STEP", parser.getAttributeValue(1));
							point_collect.setSTEP(Integer.parseInt(parser.getAttributeValue(1)));
						
						} else if ("DEEP_MANUAL".equals(name)) {
							Logger.d("START_TAG_DEEP_MANUAL", parser.getAttributeValue(1));
							point_collect.setDEEP_MANUAL(Boolean.parseBoolean(parser.getAttributeValue(1)));
						
						}else if ("MEASURE_WAY_TOUP".equals(name)) {
							Logger.d("MEASURE_WAY_TOUP", parser.getAttributeValue(1));
							point_collect.setMeasureWayToUp(Boolean.parseBoolean(parser.getAttributeValue(1)));
						
						}else if ("SumPoint".equals(name)) {
							Logger.d("START_TAG_SumPoint",parser.getAttributeValue(1));
							point_collect.setSumPoint(Integer.parseInt(parser.getAttributeValue(1)));
						
						} else if ("INTERVAL_LEN".equals(name)) {

						}else if("Ilist_bhs".equals(name)){
							point_collect.setIlist_bhs(Integer.parseInt(parser.getAttributeValue(1)));
						}
					}
					Logger.e("myLogger_name", name);
					break;

				case XmlPullParser.TEXT:
					Logger.d("myLogger", "TEXT");
					Logger.d("myLogger", "TEXT=" + parser.getText());
					if (null != name) {
						if ("NextMillistime".equals(name)) {
							Logger.d("NextMillistime", parser.getAttributeValue(1));
						
						} else if ("START_DEEP".equals(name)) {
							Logger.d("START_DEEP", parser.getText());
							point_collect.setSTART_DEEP(parser.getText());
						
						} else if ("STEP".equals(name)) {
							Logger.d("STEP", parser.getAttributeValue(1));
						
						} else if ("DEEP_MANUAL".equals(name)) {
							Logger.d("DEEP_MANUAL", parser.getAttributeValue(1));
						
						} else if ("SumPoint".equals(name)) {
							Logger.d("SumPoint",parser.getAttributeValue(1));
						
						} else if ("INTERVAL_LEN".equals(name)) {
							Logger.d("INTERVAL_LEN", parser.getText());
							point_collect.setINTERVAL_LEN(parser.getText());
						}

					}
					break;

				case XmlPullParser.END_TAG:
					Logger.d("myLogger", "END_TAG");
					Logger.d("myLogger", "END_TAG=" + parser.getText());
					if ("person".equals(parser.getName())) {
						// personsList.add(person);
						// person = null;
					}
					name = null;
					break;
				}
			
			eventType = parser.next(); // 下一个事件类型
			}
		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return point_collect;
	}
	
	 /**
     *解析SET_START.xml文件
     *@author zgl
     * 
     */
	public static SET_START parseSET_START(String fileName){
		InputStream is = null;
		SET_START set_start = new SET_START();
		try {
			is = new FileInputStream(new File(fileName));
			// 使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");

			int eventType = parser.getEventType();
			String name = "";
			eventType = parser.nextTag();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;

				case XmlPullParser.START_TAG:
					Logger.d("myLogger", "START_TAG_");
					if ("map".equals(parser.getName())) {
//
					} else if ("long".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("int".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("boolean".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} else if ("string".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					}
					if (null != name) {

						if ("DELAY_TIME".equals(name)) {
							Logger.d("DELAY_TIME", parser.getAttributeValue(1));
							set_start.setDELAY_TIME(Integer.parseInt(parser.getAttributeValue(1)) );
						
						} else if ("INTERVAL_TIME".equals(name)) {
							set_start.setINTERVAL_TIME(Integer.parseInt(parser.getAttributeValue(1)) );
						
						} else if ("START_TIME".equals(name)) {
							Logger.d("START_TIME", parser.getAttributeValue(1));
							set_start.setSTART_TIME(Long.parseLong(parser.getAttributeValue(1)) );
						
						} else if ("JiaoZhun".equals(name)) {
							Logger.d("JiaoZhun", parser.getAttributeValue(1));
							set_start.setJiaoZhun(Integer.parseInt(parser.getAttributeValue(1)) );
						}else if("Time".equals(name)){
							set_start.setTime(parser.getText());
							Log.e("time=============>>>","..zzhixingler"+name);
						}

					}
					Logger.e("myLogger_name", name);
					break;

				case XmlPullParser.TEXT:
					Logger.d("myLogger", "TEXT");
					Logger.d("myLogger", "TEXT=" + parser.getText());
					if (null != name) {
						Log.e("time=============>>>","............."+name);
						if ("HOLE_ID".equals(name)) {
							Logger.d("HOLE_ID", parser.getText());
							set_start.setHOLE_ID(parser.getText());
						}else if("Drilling".equals(name)){
							set_start.setDrilling( parser.getText());
						}else if("Ben".equals(name)){
							set_start.setBen(parser.getText());
						}else if("Face".equals(name)){
							set_start.setFace(parser.getText());
						}else if("ChongM".equals(name)){
							set_start.setChongM(parser.getText());
						}else if ("qinjiao".equals(name)) {
							set_start.setQinjiao(parser.getText());
						}else if ("fangwei".equals(name)) {
							set_start.setFangwei(parser.getText());
						}else if ("kongshen".equals(name)) {
							set_start.setKongshen(parser.getText());
						}else if ("shang".equals(name)) {
							set_start.setShang(parser.getText()) ;
						}else if ("zuo".equals(name)) {
							set_start.setZuo(parser.getText()) ;
						}else if ("zong".equals(name)) {
							set_start.setZong(parser.getText()) ;
						}else if("Time".equals(name)){
							set_start.setTime(parser.getText());
							Log.e("time=============>>>","...........下面 的执行了.."+name);
						}


					}
					break;

				case XmlPullParser.END_TAG:
					Logger.d("myLogger", "END_TAG");
					Logger.d("myLogger", "END_TAG=" + parser.getText());
					if ("person".equals(parser.getName())) {
						// personsList.add(person);
						// person = null;
					}
					name = null;
					break;
				}
			
			eventType = parser.next(); // 下一个事件类型
			}
		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();


			Log.e("try catch:",""+e);
		}

		return set_start;
	}
	 /**
     *解析SOFT_CONFIG.xml文件
     * @author zgl
     * 
     */
	public static SOFT_CONFIG parseSOFT_CONFIG(String fileName){
		InputStream is = null;
		SOFT_CONFIG soft_config = new SOFT_CONFIG();
		try {
			is = new FileInputStream(new File(fileName));
			// 使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(is, "utf-8");

			int eventType = parser.getEventType();
			String name = "";
			eventType = parser.nextTag();
			while (XmlPullParser.END_DOCUMENT != eventType) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:

					break;

				case XmlPullParser.START_TAG:
					Logger.d("myLogger", "START_TAG_");
					if (!"map".equals(parser.getName())) {
						name = parser.getAttributeValue(0);
					} 
					Logger.e("myLogger_name", name);
					break;

				case XmlPullParser.TEXT:
					Logger.d("myLogger", "TEXT");
					Logger.d("myLogger", "TEXT=" + parser.getText());
					if (null != name) {
						if ("CODE_TO_HEART".equals(name)) {
							Logger.d("CODE_TO_HEART", parser.getText());
							soft_config.setCODE_TO_HEART(parser.getText());
						
						}else if("MACHINE_NUM".equals(name)){
							Logger.d("MACHINE_NUM", parser.getText());
							soft_config.setMACHINE_NUM(parser.getText());
						
						}

					}
					break;

				case XmlPullParser.END_TAG:
					Logger.d("myLogger", "END_TAG");
					Logger.d("myLogger", "END_TAG=" + parser.getText());
					if ("person".equals(parser.getName())) {
						// personsList.add(person);
						// person = null;
					}
					name = null;
					break;
				}
			
			eventType = parser.next(); // 下一个事件类型
			}
		} catch (XmlPullParserException e) { // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return soft_config;
	}
	public static double BD(double dou){
		java.math.BigDecimal b = new java.math.BigDecimal(dou);
		double myNum3 = b.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
		return myNum3;
	}
}
