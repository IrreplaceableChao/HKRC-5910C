package com.android.utils;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TimerTextView extends TextView implements Runnable{  
      private String strhour;
      private String strhour1;
      private String strhour2;
    public TimerTextView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
    }  
  
    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒  
    private boolean run=false; //是否启动了  
  
    public void setTimes(long[] djstime) {  
       // mday = djstime[0];  
        mhour = djstime[1];  
        mmin = djstime[2];  
        msecond = djstime[3];  
  
    }  
  
    /** 
     * 倒计时计算 
     */  
    private void ComputeTime() {  
        msecond--;  
        if (msecond < 0) {  
            mmin--;  
            msecond = 59;  
            if (mmin < 0) {  
                mmin = 59;  
                mhour--;  
//                if (mhour < 0) {  
//                    // 倒计时结束，一天有24个小时  
//                    mhour = 23;  
//                    mday--;  
//      
//                }  
            }  
      
        }  
      
    }  
  
    public boolean isRun() {  
        return run;  
    }  
  
    public void beginRun() {  
        this.run = true;  
        run();  
    }  
      
    public void stopRun(){  
        this.run = false;  
    }  
      
  
    @Override  
    public void run() {  
        //标示已经启动  
        if(run){  
            ComputeTime();  
  if(mhour<10){
	 strhour="0"+String.valueOf(mhour); 
  }else{
	  strhour=String.valueOf(mhour);
  }
  
  if(mmin<10){
		 strhour1="0"+String.valueOf(mmin); 
	  }else{
		  strhour1=String.valueOf(mmin);
	  }
  if(msecond<10){
		 strhour2="0"+String.valueOf(msecond); 
	  }else{
		  strhour2=String.valueOf(msecond);
	  }
            String strTime=strhour+":"+ strhour1+":"+strhour2;  
            this.setText(strTime);  
  
            postDelayed(this,999);  
        }else {  
            removeCallbacks(this);  
        }  
    }  
  
}  