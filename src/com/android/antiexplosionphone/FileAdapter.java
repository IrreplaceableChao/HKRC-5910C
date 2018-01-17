package com.android.antiexplosionphone;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * 文件管理适配器
 * 
 * @author TY
 * 
 */
public class FileAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater = null;
	private ArrayList<HashMap<String, String>> list;// 填充数据的list
	private ArrayList<ViewHolder> mHolders;

	// 构造器
	public FileAdapter(ArrayList<HashMap<String, String>> list, Context context) {
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
		mHolders = new ArrayList<ViewHolder>();
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (mHolders.size() <= position) {
			convertView = inflater.inflate(R.layout.file_item, null);
			ViewHolder holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
			holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
			// holder.img = (ImageView) convertView.findViewById(R.id.item_iv);
			holder.mConvertView = convertView;
			mHolders.add(position, holder);
			convertView.setTag(mHolders.get(position));
		} else {
			convertView = mHolders.get(position).mConvertView;
		}
		// 设置list中TextView的显示
		mHolders.get(position).tv.setText(list.get(position).get("content")
				.toString());
		// 根据flag来设置checkbox的选中状况
		mHolders.get(position).cb.setChecked(list.get(position).get("flag")
				.equals("true"));

		return convertView;
	}

	final class ViewHolder {
		public TextView tv;
		public CheckBox cb;
		// public ImageView img;
		public View mConvertView;
	}
}
