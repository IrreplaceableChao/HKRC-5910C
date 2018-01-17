package com.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesHelper {
	
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Context context;
	//创建
	public SharedPreferencesHelper(Context c,String name){
		Log.d("chuangjian",name);
	context = c;
	sp = context.getSharedPreferences(name, 0);
	editor = sp.edit();
	
	}
	//写入数据
	public void putValue(String key, String value){
		//Log.d("xieshuju","ok");
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
		} 
	public void putValue(String key, long value){
		//Log.d("xieshuju","ok");
		editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
		} 
	public void putValue(String key, int value){
		//Log.d("xieshuju","ok");
		editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
		} 
//	读取数据
	public String getString(String key){
		//Log.d("dustring","ok");
		return sp.getString(key, " ");
		}
	public Long getLong(String key){
		return sp.getLong(key, 0);
		}
	public int getInt(String key){
		return sp.getInt(key, 0);
		}
}
