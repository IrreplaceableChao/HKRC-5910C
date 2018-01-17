package com.android.utils;

import android.app.Application;

import org.xutils.x;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化
		x.Ext.init(this);
		// 设置是否输出debug
		x.Ext.setDebug(true);
	}

}
