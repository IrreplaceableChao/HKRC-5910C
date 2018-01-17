package com.android.help;

import com.android.antiexplosionphone.R;
import com.android.antiexplosionphone.R.id;
import com.android.antiexplosionphone.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Help extends Activity {
	private ImageView ruanjian;
	private ImageView celiangyi;
	private ImageView wenti;
	private ImageView lianxi;
	private TextView help;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		ruanjian = (ImageView) findViewById(R.id.ruanjian01);
		celiangyi = (ImageView) findViewById(R.id.celiangyi);
		wenti = (ImageView) findViewById(R.id.wenti);
		lianxi = (ImageView) findViewById(R.id.lianxi);
		help = (TextView) findViewById(R.id.help);
		ruanjian.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Help.this, RuanJian.class);
				startActivity(intent);
			}
		});
		celiangyi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Help.this, Cly.class);
				startActivity(intent);
			}
		});
		wenti.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Help.this, Wt.class);
				startActivity(intent);
			}
		});
		lianxi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Help.this, Lx.class);
				startActivity(intent);
			}
		});
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Help.this.finish();
			}
		});

	}

}
