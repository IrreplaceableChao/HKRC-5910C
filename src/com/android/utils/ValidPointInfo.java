package com.android.utils;

/**
 * 有效点结构类
 * 
 * @author TY
 * 
 */
public class ValidPointInfo {
	public String serNum; // 序号
	public Float deep; // 对应测量孔深
	public String time; // 对应测量时间
	public String id; // 对应采集点编号
	public String data; // 对应测量日期
	public long millisTime; // 对应测量时间
	public String type;//类型
	//public String coulor;//标记颜色
	public ValidPointInfo(String sernum, Float dp, String tm, String nId,
			String dt,long millistm, String type) {
		this.serNum = sernum;
		this.deep = dp;
		this.time = tm;
		this.id = nId;
		this.data = dt;
		this.millisTime = millistm;
		this.type=type;
	}
}
