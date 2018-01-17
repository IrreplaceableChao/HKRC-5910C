package com.android.utils;

public class POINT_COLLECT {
	private long NextMillistime = 0L;
	private String START_DEEP = "";
	private int STEP = 99;
	private boolean DEEP_MANUAL = false	;
	private int SumPoint = 0;
	private String INTERVAL_LEN = "";
	private boolean MeasureWayToUp = true; 
	private int Ilist_bhs=0;
	
	
	public boolean isMeasureWayToUp() {
		return MeasureWayToUp;
	}
	public void setMeasureWayToUp(boolean measureWayToUp) {
		MeasureWayToUp = measureWayToUp;
	}
	public long getNextMillistime() {
		return NextMillistime;
	}
	public void setNextMillistime(long nextMillistime) {
		NextMillistime = nextMillistime;
	}
	public String getSTART_DEEP() {
		return START_DEEP;
	}
	public void setSTART_DEEP(String sTART_DEEP) {
		START_DEEP = sTART_DEEP;
	}
	public int getSTEP() {
		return STEP;
	}
	public void setSTEP(int sTEP) {
		STEP = sTEP;
	}
	public boolean isDEEP_MANUAL() {
		return DEEP_MANUAL;
	}
	public void setDEEP_MANUAL(boolean dEEP_MANUAL) {
		DEEP_MANUAL = dEEP_MANUAL;
	}
	public int getSumPoint() {
		return SumPoint;
	}
	public void setSumPoint(int sumPoint) {		SumPoint = sumPoint;	}
	public String getINTERVAL_LEN() {
		return INTERVAL_LEN;
	}
	public void setINTERVAL_LEN(String iNTERVAL_LEN) {
		INTERVAL_LEN = iNTERVAL_LEN;
	}
	public int getIlist_bhs() {return Ilist_bhs;	}
	
	public void setIlist_bhs(int ilist_bhs) {	Ilist_bhs = ilist_bhs;	}
	
}
