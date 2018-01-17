package com.android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.MP.Data;
import com.android.MP.Zhiliangpingjia;
import com.android.antiexplosionphone.R;
import com.android.bean.FileNameBean;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ShowDataTableActivity extends Activity {
	private ListView scroll_list;
	private MyAdpater adpater = null;
	private List<String> list;
	private Button btn_delete;
	private File file = null;
	private List<Integer> itemPosition = new ArrayList<Integer>();
	// private List<Map<String, String>> list1 = new ArrayList<Map<String,
	// String>>();
	private List<Map<String, String>> terms;
	private TextView textView;
	private Button xuanzhongall;
	private boolean isChcekAll = true;
	private TextView textView1;
	private String path;
	private Button fanhuichaxun;
	private Boolean isxian = false;
	private LinearLayout lbufen;
	private LinearLayout lfen;
	private File SDFile;
	private ArrayList<FileNameBean> fileNameList = new ArrayList<FileNameBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shujuchaxuntable);

		terms=(ArrayList<Map<String, String>>) getIntent().getSerializableExtra("terms");

		SDFile = Environment.getExternalStorageDirectory();
		lfen = (LinearLayout) findViewById(R.id.lfen);
		list = new ArrayList<String>();
		textView1 = (TextView) findViewById(R.id.textView1);
		scroll_list = (ListView) findViewById(R.id.scroll_list);
		btn_delete = (Button) findViewById(R.id.delete);
		xuanzhongall = (Button) findViewById(R.id.xuanzhongall);
		fanhuichaxun = (Button) findViewById(R.id.fanhuichaxun);
		// path = getIntent().getStringExtra("path");
		textView1.setVisibility(View.GONE);
		final File SDFile = Environment.getExternalStorageDirectory();
		File sdPath = new File(SDFile.getAbsolutePath() + "/HKEXCLE");

		file = sdPath;
		// FileListBean.list = list1;
		adpater = new MyAdpater();
		getAllFiles(sdPath);

		scroll_list.setAdapter(adpater);
		xuanzhongall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isChcekAll) {
					for (int count = 0; count < fileNameList.size(); count++) {
						((CheckBox) scroll_list.getAdapter()
								.getView(count, null, null)
								.findViewById(R.id.CheckBox)).setChecked(true);
					}
					adpater.notifyDataSetChanged();
					findViewById(R.id.xuanzhongall).setVisibility(View.GONE);
					findViewById(R.id.quxiaoall).setVisibility(View.VISIBLE);

					isChcekAll = false;
				}

			}
		});
		findViewById(R.id.quxiaoall).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				findViewById(R.id.xuanzhongall).setVisibility(View.VISIBLE);
				findViewById(R.id.quxiaoall).setVisibility(View.GONE);

				for (int count = 0; count < fileNameList.size(); count++) {
					((CheckBox) scroll_list.getAdapter()
							.getView(count, null, null)
							.findViewById(R.id.CheckBox)).setChecked(false);
				}

				adpater.notifyDataSetChanged();

				// finish();
				// Intent intent = new Intent(ShowDataTableActivity.this,
				// ShowDataTableActivity.class);
				// intent.putExtra("longendtime",
				// getIntent().getLongExtra("longstarttime", 0));
				// intent.putExtra("longstarttime",
				// getIntent().getLongExtra("longstarttime", 0));
				// intent.putExtra("isCheck",
				// getIntent().getBooleanExtra("isCheck", false));
				// intent.putExtra("isPOrS",
				// getIntent().getStringExtra("isPOrS"));
				// startActivity(intent);

				isChcekAll = true;
			}
		});
		btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ShowDataTableActivity.this);
				builder.setCancelable(false); // 不响应back按钮
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage("        数据删除后不可恢复，确定删除吗？"); // 对话框显示内容
				// 设置按钮
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								// 遍历要删除的文件名集合。之后将其一个个的删除
								for (String filePath : list) {
									File file = new File(filePath);
									// file.delete();
									RecursionDeleteFile(file);
								}

								if (file.length() >= list.get(0).length()) {

									for (String filePath : list) {

										// Toast.makeText(ShowDataTableActivity.this,
										// SDFile.getAbsoluteFile() +
										// "/HKCX-SJ", 0)
										// .show();
										//
										// Toast.makeText(
										// ShowDataTableActivity.this,
										// filePath.substring(filePath.lastIndexOf("/"),
										// filePath.length()), 0).show();
										
										Log.e("filePath", filePath);

										File file = new File(SDFile
												.getAbsoluteFile()
												+ "/HKCX-SJ"
												+ filePath.substring(filePath
														.lastIndexOf("/"),
														filePath.length()));

										RecursionDeleteFile(file);

									}
								}
								
								fileNameList.clear();
								//判断是否点击查看excle，点击查看过，截取成HXEXCLE路劲，未查看过，直接查询。
								if (path.contains("-")) {
									getAllFiles(new File(path.substring(0, path.lastIndexOf("/"))));
								}else{
									getAllFiles(new File(path));
								}
								
								btn_delete.setEnabled(false);
								adpater.notifyDataSetChanged();
							}

						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(ShowDataTableActivity.this,
										"点击了取消按钮", Toast.LENGTH_SHORT).show();
							}
						}).show();
				adpater.notifyDataSetChanged();
			}

		});

		scroll_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String str = fileNameList.get(position).getPath();
				str = str.substring(25,str.length());
				Intent intent = new Intent();
				intent.putExtra("path",str);
				intent.setClass(ShowDataTableActivity.this, Data.class);
				startActivity(intent);
//				isxian = true;
//				file = new File(fileNameList.get(position).getPath());
//				System.out.println(file);
//				if (!file.isFile()) {
//					getAllFiles(file);
//				}

			}
		});
		fanhuichaxun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ShowDataTableActivity.this.finish();

			}
		});

	}

	
	
	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	class MyAdpater extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			// return list1.size();
			return fileNameList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return fileNameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = null;
//			Log.e("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwww","file" + SDFile.getAbsolutePath() + "/HKEXCLE");

			view = LayoutInflater.from(ShowDataTableActivity.this).inflate(
					R.layout.fen, null);
			TextView textView1 = (TextView) view.findViewById(R.id.item_datav1);
			TextView textView2 = (TextView) view.findViewById(R.id.item_datav2);
			TextView textView3 = (TextView) view.findViewById(R.id.item_datav3);
			TextView textView4 = (TextView) view.findViewById(R.id.item_datav4);
			TextView textView5 = (TextView) view.findViewById(R.id.item_datav5);
			TextView textView6 = (TextView) view.findViewById(R.id.item_datav6);
			TextView textView7 = (TextView) view.findViewById(R.id.item_datav7);
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.CheckBox);
			
//			if (fileNameList.get(position).getpOrs().equals("平行孔")) {
				textView1.setText(fileNameList.get(position).getpOrs());

				textView2.setText(fileNameList.get(position).getGzm());
				textView4.setText(fileNameList.get(position).getKh());
				textView3.setText(fileNameList.get(position).getZc());
				String time = "20"+fileNameList.get(position).getTime();
				
				time=time.substring(0,4)+"-"+time.substring(4, 6)+"-"+time.substring(6, time.length());
				
				textView5.setText(time);
				textView6.setText(fileNameList.get(position).getCount());
				textView7.setText(fileNameList.get(position).getZclx());
//			}else if (fileNameList.get(position).getpOrs().equals("扇行孔")) {
//				textView1.setText(fileNameList.get(position).getpOrs());
//
//				textView2.setText(fileNameList.get(position).getGzm());
//				textView4.setText(fileNameList.get(position).getZc());
//				textView3.setText(fileNameList.get(position).getKh());
//				String time = "20"+fileNameList.get(position).getTime();
//				
//				time=time.substring(0,4)+"-"+time.substring(4, 6)+"-"+time.substring(6, time.length());
//				
//				textView5.setText(time);
//				textView6.setText(fileNameList.get(position).getCount());
//				textView7.setText(fileNameList.get(position).getZclx());
//			}
			
			
			
			if (list.contains(fileNameList.get(position).getPath())) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}

			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {

					if (isChecked) {
						list.add(fileNameList.get(position).getPath());
						btn_delete.setEnabled(true);
						itemPosition.add(position);
					} else {
						list.remove(fileNameList.get(position).getPath());

						for (int i = 0; i < itemPosition.size(); i++) {
							if (itemPosition.get(i) == position) {
								itemPosition.remove(i);
							}
						}

						if (list.size() == 0) {
							btn_delete.setEnabled(false);
						}
					}

				}
			});

			return view;
		}

	}

	/**
	 * @param term
	 *            查询条件名称
	 * @return
	 */
	private void getFilesList(List<Map<String, String>> terms) {

		ArrayList<FileNameBean> fileNameList1 = new ArrayList<FileNameBean>();

		for (Map<String, String> term : terms) {

			for (FileNameBean bean : fileNameList) {
				Gson gson = new Gson();
				try {
					String str = (String) term.keySet().toArray()[0];
					if (term.get(str).equals(
							new JSONObject(gson.toJson(bean)).getString(str))) {
						fileNameList1.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			fileNameList.clear();

			for (FileNameBean fileNameBean : fileNameList1) {
				fileNameList.add(fileNameBean);
			}

			fileNameList1.clear();

		}

	}

	private void getAllFiles(File path) {

		// if(!path.getAbsoluteFile().equals(SDFile.getAbsolutePath() +
		// "/HKEXCLE")){
		// list1.clear();
		// }

		this.path = path.getAbsolutePath();
		Log.e("llllllllllllllllllllll", "" + this.path);
		try {

			if (path.listFiles().length > 0) {
				for (File file : path.listFiles()) {
					Log.e("llllllllllllllllllllll", "" + file);
					if (this.path.equals(SDFile.getAbsolutePath() + "/HKEXCLE")) {

						FileNameBean fileNameBean = new FileNameBean();

						String allFileName = file.getName();//F-S-40801-1-考虑-150805-1

						String userName = allFileName.substring(0,
								allFileName.indexOf("-")); // F

						String str_zclx = "";

						if (userName.equals("F")) {
							str_zclx = "防治水孔";

						} else {
							str_zclx = "瓦斯抽采孔";
						}
						fileNameBean.setZclx(str_zclx);

						allFileName = allFileName.substring(
								userName.length() + 1, allFileName.length());// 40807-1-20150504-0

						userName = allFileName.substring(0,
								allFileName.indexOf("-")); // 第一个字节p
						String pOrs = allFileName.substring(0,
								allFileName.indexOf("-")); // 第一个字节p
						String str = "";

						if (userName.equals("P")) {
							str = "平行孔";

						} else {
							str = "扇行孔";
						}
						fileNameBean.setpOrs(str);

						allFileName = allFileName.substring(
								userName.length() + 1, allFileName.length());// 40807-1-20150504-0
						userName = allFileName.substring(0,
								allFileName.indexOf("-")); // 第二个字节
						fileNameBean.setGzm(userName);
						if (pOrs.equals("S")) {
							allFileName = allFileName
									.substring(userName.length() + 1,
											allFileName.length());// 40807-1-20150504-0
							userName = allFileName.substring(0,
									allFileName.indexOf("-")); // 第二个字节
							fileNameBean.setZc(userName);

						}

						allFileName = allFileName.substring(
								userName.length() + 1, allFileName.length());
						userName = allFileName.substring(0,
								allFileName.indexOf("-")); // 第二个字节
						fileNameBean.setKh(userName);
						allFileName = allFileName.substring(
								userName.length() + 1, allFileName.length());
						userName = allFileName.substring(0,
								allFileName.indexOf("-")); // 第二个字节
						fileNameBean.setTime(userName);
						allFileName = allFileName.substring(
								userName.length() + 1, allFileName.length());
						userName = allFileName.substring(0,
								allFileName.length()); // 第二个字节
						fileNameBean.setCount(userName);

						fileNameBean.setPath(file.getAbsolutePath());
						fileNameBean.setName(file.getName());

						fileNameList.add(fileNameBean);

					} else {

						Intent intent = new Intent("android.intent.action.VIEW");

						intent.addCategory("android.intent.category.DEFAULT");

						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						Uri uri = Uri.fromFile(file);
						intent.setDataAndType(uri, "application/msword");
						startActivity(intent);
						// 　　return intent;

						// parseExcel(file);

					}

				}

				getFilesList(terms);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private long timeString2Long(String timeStr) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = format.parse(timeStr);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}
}
