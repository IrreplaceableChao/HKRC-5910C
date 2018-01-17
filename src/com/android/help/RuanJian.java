package com.android.help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.antiexplosionphone.R;

public class RuanJian extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ruanjian);
		ImageView	guiji=(ImageView) findViewById(R.id.guiji);
		ImageView	shujujieshou=(ImageView) findViewById(R.id.shujujieshou);
		ImageView	tgsp=(ImageView) findViewById(R.id.tgsp);
		ImageView	sjcx=(ImageView) findViewById(R.id.sjcx);
		TextView	ruan=(TextView) findViewById(R.id.ruan);
		guiji.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(RuanJian.this, Guiji.class);
				intent.putExtra("status", "1");
				startActivity(intent);
			}
		});
		shujujieshou.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(RuanJian.this, Guiji.class);
				intent.putExtra("status", "2");
				startActivity(intent);
			}
		});
		tgsp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(RuanJian.this, Guiji.class);
				intent.putExtra("status", "3");
				startActivity(intent);
			}
		});
		sjcx.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(RuanJian.this, Guiji.class);
				intent.putExtra("status", "4");
				startActivity(intent);
			}
		});
		ruan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RuanJian.this.finish();
			}
		});
		
	}

}
