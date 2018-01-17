package com.android.antiexplosionphone;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.bluetooth.BluetoothTools;
import com.android.utils.ConstantDef;
import com.android.utils.Preferences;
import com.android.utils.Utils;

/**
 * 软件设置界面
 * 
 * @author TY
 * 
 */
public class Soft_Config extends Activity {

	private TextView mtvDate; // 手机出厂日期
	private EditText mettCode; // 机芯配对码
	private EditText metNum; // 机芯仪器编号
	private Button btn_codeToHeart;

	private final BluetoothAdapter mBtAdapter = BluetoothAdapter
			.getDefaultAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.soft_config);

		initActivity();
	}

	private void initActivity() {

		// 自动配对按钮
		 btn_codeToHeart = (Button) findViewById(R.id.btn_codetoheart);
		
		btn_codeToHeart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (findViewById(R.id.ll_showtransetting).getVisibility() == View.VISIBLE) {
//					if (mettCode.getText().toString().equals("")) {
//						Utils.toast(Soft_Config.this, "请输入机芯配对码！");
//						return;
//					}else  if((mettCode.getText().toString().equals(Preferences.getCodeToHeart(Soft_Config.this)))){
//						 Preferences.saveCodeToHeart(Soft_Config.this, mettCode
//									.getText().toString());
//							
//					 }else {
//							Utils.toast(Soft_Config.this, "机芯配对码输入错误！");
//							return;
//						}
//					if (metNum.getText().toString().equals("")) {
//						Utils.toast(Soft_Config.this, "请输入机芯仪器编号！");
//						return;
//					}else if(metNum.getText().toString().equals(Preferences.getMachineNum(Soft_Config.this))){
//						 Preferences.saveCodeToHeart(Soft_Config.this, metNum
//									.getText().toString());
//					}else{
//						Utils.toast(Soft_Config.this, "机芯仪器编号输入错误！");
//						return;
//					}
				}
				mBtAdapter.enable();
				if (mBtAdapter.isDiscovering()) {
					mBtAdapter.cancelDiscovery();
				}
				mBtAdapter.startDiscovery();
				Soft_Config.this.finish();
				// 发送开始配对广播
				Intent foundIntent = new Intent(
						BluetoothTools.ACTION_PAIR_DEVICE_START);
				sendBroadcast(foundIntent);
			}
		});

		Button btn_password = (Button) findViewById(R.id.btn_password);
		btn_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText password = (EditText) findViewById(R.id.et_password);
				if (password.getText().toString()
						.equals(ConstantDef.SETTING_PASSWORD)) {
					findViewById(R.id.ll_password).setVisibility(View.GONE);
					findViewById(R.id.ll_showtransetting).setVisibility(
							View.VISIBLE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				} else {
					Utils.toast(Soft_Config.this, "密码输入错误");
				}

			}
		});

		mtvDate = (TextView) findViewById(R.id.tv_phonedate);
		mtvDate.setText(Preferences.getProduceDate(Soft_Config.this));
		mtvDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});

		mettCode = (EditText) findViewById(R.id.et_codetoheart);
		mettCode.setText(Preferences.getCodeToHeart(Soft_Config.this));
		metNum = (EditText) findViewById(R.id.et_machinenum);
		metNum.setText(Preferences.getMachineNum(Soft_Config.this));
	}

	private void showDateDialog() {
		// 初始化日期弹出框
		String date = Preferences.getProduceDate(Soft_Config.this);
		int year = 2014;
		int month = 01;
		int day = 01;
		if (!date.equals("")) {
			String[] sub = date.split("-");
			if (sub.length == 3) {
				year = Integer.parseInt(sub[0]);
				month = Integer.parseInt(sub[1]) - 1;
				day = Integer.parseInt(sub[2]);
			}
		}

		new DatePickerDialog(Soft_Config.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						int month = monthOfYear + 1;
						int day = dayOfMonth;
						String monStr = ((month < 10) ? "0" : "") + month;
						String dayStr = ((day < 10) ? "0" : "") + day;
						String formatdate = year + "-" + monStr + "-" + dayStr;
						mtvDate.setText(formatdate);
						Preferences.saveProduceDate(Soft_Config.this,
								formatdate);
					}
				}, year, month, day).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mettCode.getText().toString().equals("")) {
				Utils.toast(Soft_Config.this, "请输入机芯配对码！");
				return false;
			}
			if((mettCode.getText().toString().equals(Preferences.getCodeToHeart(Soft_Config.this)))){
				 Preferences.saveCodeToHeart(Soft_Config.this, mettCode
							.getText().toString());
			 }else  {
					Utils.toast(Soft_Config.this, "机芯配对码输入错误！");
					return false;
					
				}  
				 
				 
		 if (metNum.getText().toString().equals("")) {
					Utils.toast(Soft_Config.this, "请输入机芯仪器编号！");
				}else if(metNum.getText().toString().equals(Preferences.getMachineNum(Soft_Config.this))){
					 Preferences.saveCodeToHeart(Soft_Config.this, metNum
								.getText().toString());
				}else {
					Utils.toast(Soft_Config.this, "机芯仪器编号输入错误！");
					return false;
				}
		
					
		
			mBtAdapter.enable();
			if (mBtAdapter.isDiscovering()) {
				mBtAdapter.cancelDiscovery();
			}
			mBtAdapter.startDiscovery();
			Soft_Config.this.finish();
			// 发送开始配对广播
			Intent foundIntent = new Intent(
					BluetoothTools.ACTION_PAIR_DEVICE_START);
			sendBroadcast(foundIntent);
			 return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
		//return true;
	}

	@Override
	protected void onDestroy() {
		Preferences.saveCodeToHeart(Soft_Config.this, mettCode.getText()
				.toString());
		Preferences.saveMachineNum(Soft_Config.this, metNum.getText()
				.toString());
//		Preferences.saveProduceDate(Soft_Config.this,mtvDate.getText()
//				.toString());
		
		super.onDestroy();
	}
}
