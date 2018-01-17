package com.android.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;


public class PublicValues {
	public static int step = 1;
	public static int yachishijian = 1;
	public static int xiaxinghour = 0;
	public static int xiaxingminute = 0;
	public static int xiaxingseconds = 10;
	public static int shangfuhour = 0;
	public static int shangfuminute = 1;
	public static int shangfuseconds = 1;
	public static String strxiaxing = "";
	public static String strshangfu = "";
	public static String tanguanname;
	public static long celiangdiantime = 0;
	public static int cexietype = 0;
	public static String teststr="";
	public static Map<String, String> backinfo= new HashMap<String, String>();
	public static String pinkey = "1234";
	public static String tanguanpinkey = "1234";
	public static List<Map<String, String>> daochudata = null;
	
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    public static Date start;
    
    public static BluetoothDevice device = null;
    public static  String info = "";
    
	public static  String minute2hour(double minutes){
		xiaxinghour = (int) (minutes/60);
		String a = String.valueOf(xiaxinghour);
		if(xiaxinghour<10){
			a = "0"+a; 
		}
        return a;
    }
	public static  String minute2minute(double minutes){
		xiaxingminute = (int) ( minutes%60);
		String a = String.valueOf(xiaxingminute);
		if(xiaxingminute<10){
			a = "0"+a;
		}
        return a;
    }
	public static  String minute2seconds(double minutes){
		xiaxingseconds = (int) ( minutes*60%60);
		String a = String.valueOf(xiaxingseconds);
		if(xiaxingseconds<10){
			a = "0"+a;
		}
        return a;
    }
}
