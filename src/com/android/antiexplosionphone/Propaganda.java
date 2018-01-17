package com.android.antiexplosionphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Propaganda extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.propaganda);
		// final Intent it = new Intent(this, Main.class); //你要转向的Activity
		// Timer timer = new Timer();
		// TimerTask task = new TimerTask() {
		// public void run() {
		// startActivity(it); //执行
		// }
		// };
		// timer.schedule(task, 1000 * 3); //10秒后
		Button jinru = (Button) findViewById(R.id.jinru);
		Button tuichu = (Button) findViewById(R.id.tuichu);
		//跳转
		jinru.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Propaganda.this, Main.class);
				startActivity(intent); //执行
			}
		});
		tuichu.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Propaganda.this.finish();
			}
		});

//		Log.e("aaaaaaaaaaa:",":"+Math.sin(0.65 * $F$1));
//
//		Log.e("aaaaaaaaaaa:",":"+(Math.sin(80.51 * $F$1) * Math.sin(-0.66 * $F$1)) / Math.sin(0.65 * $F$1));
//
//		Log.e("aaaaaaaaaaa:",":"+Math.asin(Utils.BD((Math.sin(80.51 * $F$1) * Math.sin(-0.66 * $F$1)) / Math.sin(0.65 * $F$1))));
//            Math.asin((Math.sin(80.51 * $F$1) * Math.sin(-0.66 * $F$1)) / Math.sin(0.65 * $F$1)) / $F$1;
	}

}
