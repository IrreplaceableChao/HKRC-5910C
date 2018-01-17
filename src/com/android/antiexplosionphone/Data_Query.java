package com.android.antiexplosionphone;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.android.utils.DateTimePickerDialog;
import com.android.utils.ShowDataTableActivity;

public class Data_Query extends Activity {
	private static final String TAG = "MainActivity";
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private Button chaxun;
	private Button fanhui;
	private RadioButton radioping;
	private Boolean isping = false;
	private RadioGroup radiops;
	private RadioGroup radiops1;
	private String isPOrS = "P";
	private String isFOrW = "-";
	private Button fanhuizhuye;
	private EditText starttime;
	private EditText endtime;
	private EditText konghao;
	private EditText jinghao;
	private CheckBox checkshijian;
	private String stkonghao;
	private String stjinghao;
	private String stzuanchang;
	private EditText zuanchang;
	private LinearLayout zuan_layout;
	private EditText jinghao1;
	private boolean isCheck = false;
	private boolean isCheckyongtu = false;
	private boolean isCheckleixing = false;
	private boolean isCheckkonghao = false;
	private boolean isCheckjinghao = false;
	private boolean isCheckzuanchang = false;
	private ArrayList<Map<String, String>> terms = new ArrayList<Map<String, String>>();;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_query);
		chaxun = (Button) findViewById(R.id.chaxun); 
		fanhui = (Button) findViewById(R.id.fanhuizhuye);
		radioping = (RadioButton) findViewById(R.id.radioping);
		radiops = (RadioGroup) findViewById(R.id.radiops);
		radiops1 = (RadioGroup) findViewById(R.id.radiops1);
		zuan_layout = (LinearLayout) findViewById(R.id.zuan_layout);

		// starttime = (EditText) findViewById(R.id.starttime);
		// endtime = (EditText) findViewById(R.id.endtime);
		// starttime.setInputType(InputType.TYPE_NULL);
		// endtime.setInputType(InputType.TYPE_NULL);

		// checkshijian = (CheckBox) findViewById(R.id.checkshjian);
		// checkyongtu = (CheckBox) findViewById(R.id.yongtu);
		// checkleixing = (CheckBox) findViewById(R.id.leixing);
		// checkkonghao = (CheckBox) findViewById(R.id.checkkonghao);
		// checkjinghao = (CheckBox) findViewById(R.id.checkjinghao);
		konghao = (EditText) findViewById(R.id.konghao);
		zuanchang = (EditText) findViewById(R.id.zuanchang);
		jinghao = (EditText) findViewById(R.id.jinghao);
		stzuanchang = zuanchang.getText().toString().trim();
		// starttime.setOnClickListener(new DateTimeOnClick());
		// endtime.setOnClickListener(new DateOnClick());
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
		// starttime.setText(dateFormat.format(date));
		// endtime.setText(dateFormat.format(date));

		// Intent intent = new Intent(Data_Query.this,
		// ShowDataTableActivity.class);
		// //intent.putExtra("path", SDFile.getAbsolutePath() + "/HKEXCLE");
		// intent.putExtra("isPOrS", isPOrS);
		// startActivity(intent);
		jinghao.addTextChangedListener(new TextWatcher() {
			String tmp = "";
			String digits = "/---_\"\n\t";

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				jinghao.setSelection(s.length());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				tmp = s.toString();
			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = s.toString();
				if (str.equals(tmp)) {
					return;
				}
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < str.length(); i++) {
					if (digits.indexOf(str.charAt(i)) < 0) {
						sb.append(str.charAt(i));
					}
				}
				tmp = sb.toString();
				jinghao.setText(tmp);
			}
		});

		radiops.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.radioping) {
					isPOrS = "P";
					zuan_layout.setVisibility(View.GONE);
					
				} else if (checkedId == R.id.radioshan) {
					
					isPOrS = "S";
					zuan_layout.setVisibility(View.VISIBLE);
				}

				Log.e("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww", isPOrS);
			}

		});
		radiops1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == R.id.radioquan) {
					isCheckleixing=false;
					konghao.setEnabled(false);
					zuanchang.setEnabled(false);
					jinghao.setEnabled(false);
					
				} else if (checkedId == R.id.radiotiao) {
					
					isCheckleixing = true;
					konghao.setEnabled(true);
					zuanchang.setEnabled(true);
					jinghao.setEnabled(true);
//					if (isPOrS.equals("P")) {
//						Map<String, String> term = new HashMap<String, String>();
//						term.put("pOrs", "平行孔");
//						terms.add(term);
//					} else if (isPOrS.equals("S")) {
//						Map<String, String> term = new HashMap<String, String>();
//						term.put("pOrs", "扇行孔");
//						terms.add(term);
//					} else {
//						isCheckleixing = false;
//					}

				}

				Log.e("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww", isPOrS);
			}

		});
		radioping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isping = true;
			}
		});
		chaxun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// if(checkshijian.isChecked()){
				// isCheck=true;
				// }else{
				// isCheck=false;
				// }

				// if(checkyongtu.isChecked()){
				// isCheckyongtu=true;
				// }else{
				// isCheckyongtu=false;
				// }

				// if(checkleixing.isChecked()){
				// isCheckleixing=true;
				//
				// if (isPOrS.equals("P")) {
				// Map<String, String> term = new HashMap<String, String>();
				// term.put("pOrs", "平行孔");
				// terms.add(term);
				// }else if (isPOrS.equals("S")){
				// Map<String, String> term = new HashMap<String, String>();
				// term.put("pOrs", "扇行孔");
				// terms.add(term);
				// }
				//
				// }else{
				// isCheckleixing=false;
				// }

				if (!konghao.getText().toString().trim().equals("")) {
					isCheckkonghao = true;
					stkonghao = konghao.getText().toString().trim();

					Map<String, String> term = new HashMap<String, String>();
					term.put("kh", stkonghao);
					terms.add(term);

				} else {
					isCheckkonghao = false;
				}
				
				if (isCheckleixing) {
					if (isPOrS.equals("P")) {
						Map<String, String> term = new HashMap<String, String>();
						term.put("pOrs", "平行孔");
						terms.add(term);
					} else if (isPOrS.equals("S")) {
						Map<String, String> term = new HashMap<String, String>();
						term.put("pOrs", "扇行孔");
						terms.add(term);
					}
				}
				

				if (!jinghao.getText().toString().trim().equals("")) {
					isCheckjinghao = true;
					stjinghao = jinghao.getText().toString().trim();

					Map<String, String> term = new HashMap<String, String>();
					term.put("gzm", stjinghao);
					terms.add(term);

				} else {
					isCheckjinghao = false;
				}
				if (!zuanchang.getText().toString().trim().equals("")) {
					isCheckzuanchang = true;
					stzuanchang = zuanchang.getText().toString().trim();

					Map<String, String> term = new HashMap<String, String>();
					term.put("zc", stzuanchang);
					terms.add(term);

				} else {
					isCheckzuanchang = false;
				}

				// File SDFile = Environment.getExternalStorageDirectory();
				// File sdPath = new File(SDFile.getAbsolutePath() +
				// "/HKEXCLE");
				// getAllFiles(sdPath);
				// textView1.setVisibility(View.GONE);
				// FileListBean.list = list;
				// long longendtime =
				// timeString2Long("20"+endtime.getText().toString());
				// long longstarttime =
				// timeString2Long("20"+starttime.getText().toString());
				Intent intent = new Intent(Data_Query.this,
						ShowDataTableActivity.class);

				intent.putExtra("terms", terms);

				// intent.putExtra("longendtime", longendtime);
				// intent.putExtra("longstarttime", longstarttime);
				// intent.putExtra("isCheckyongtu", isCheckyongtu);
				// intent.putExtra("isCheckleixing", isCheckleixing);
				// intent.putExtra("isCheckkonghao", isCheckkonghao);
				// intent.putExtra("isCheckjinghao", isCheckjinghao);
				// intent.putExtra("isCheck", isCheck);
				// intent.putExtra("stjinghao", stjinghao);
				// intent.putExtra("stzuanchang", stzuanchang);
				// intent.putExtra("stkonghao", stkonghao);
				// intent.putExtra("path", SDFile.getAbsolutePath() +
				// "/HKEXCLE");
				// intent.putExtra("isPOrS", isPOrS);
				// intent.putExtra("isFOrW", isFOrW);
				startActivity(intent);
				terms.clear();

				// checkleixing.setChecked(false);
				// checkkonghao.setChecked(false);
				// checkjinghao.setChecked(false);

			}
		});
		final long LIMIT_AVAILABLESIZE = 10* 1024 * 1024;
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (getSDAvailableSize() < LIMIT_AVAILABLESIZE) {
					//showFileBrowser();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Data_Query.this);
					builder.setTitle("内存空间仍不足请清理！")
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											
										}
									}).setCancelable(false).show();
					
				}else{
					Data_Query.this.finish();
				}
				
			}
		});

	}
	/**
	 * 获得sd卡剩余容量，即可用大小
	 * 
	 * @return
	 */
	private long getSDAvailableSize() {

		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return blockSize * availableBlocks;
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (isCheckleixing == true) {
			if (checkedId == R.id.radioping) {
				isPOrS = "P";
				//findViewById(R.id.checkzuanchang).setVisibility(View.GONE);
				//findViewById(R.id.zuan_query).setVisibility(View.GONE);
				findViewById(R.id.zuanchang).setVisibility(View.GONE);
			} else if (checkedId == R.id.radioshan) {
				isPOrS = "S";
				//findViewById(R.id.checkzuanchang).setVisibility(View.VISIBLE);
				//findViewById(R.id.zuan_query).setVisibility(View.VISIBLE);
				findViewById(R.id.zuanchang).setVisibility(View.VISIBLE);
			}
		} else {
			if (checkedId == R.id.radioping) {
				isPOrS = "P";
			} else if (checkedId == R.id.radioshan) {
				isPOrS = "S";
			}
		}
	}

	// public void onCheckedChanged1(RadioGroup group, int checkedId) {
	//
	// if (checkedId == R.id.radiofang) {
	// isFOrW = "F";
	// } else if (checkedId == R.id.radiowa) {
	// isFOrW = "W";
	// }
	//
	// }
	private final class DateTimeOnClick implements OnClickListener {
		public void onClick(View v) {
			hideIM(v);
			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
					Data_Query.this);
			dateTimePicKDialog.dateTimePicKDialog(starttime, 1);
		}
	}

	private final class DateOnClick implements OnClickListener {
		public void onClick(View v) {
			hideIM(v);
			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
					Data_Query.this);
			dateTimePicKDialog.dateTimePicKDialog(endtime, 1);
		}
	}

	// 隐藏手机键盘
	private void hideIM(View edt) {
		try {
			InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = edt.getWindowToken();

			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {

		}
	}

	private long timeString2Long(String timeStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
}