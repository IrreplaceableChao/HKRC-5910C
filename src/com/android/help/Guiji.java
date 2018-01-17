package com.android.help;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.antiexplosionphone.R;

public class Guiji extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guiji);
		TextView gui = (TextView) findViewById(R.id.gui);
			gui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Guiji.this.finish();
			}
		});
		final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);

		scroll.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String status = getIntent().getStringExtra("status");
				if (status.equals("1")) {
					scroll.scrollTo(0, 0);
				}else if (status.equals("2")) {
					scroll.scrollTo(0, 6990);
				}else if (status.equals("3")) {
					scroll.scrollTo(0, 9930);
				}else if (status.equals("4")) {
					scroll.scrollTo(0, 18000);
				}
			}
		});




	}

}
