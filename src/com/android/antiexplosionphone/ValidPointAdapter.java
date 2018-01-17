package com.android.antiexplosionphone;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.bean.FileNameBean;
import com.android.utils.ValidPointInfo;

/**
 * 确定有效点Adapter
 * 
 * @author TY
 * 
 */
public class ValidPointAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ValidPointInfo> mItems;
	private ArrayList<ViewHolder> mHolders;
	private FileNameBean fileNameBean ;
	private String strfangshi="";
	public ValidPointAdapter(Context context, ArrayList<ValidPointInfo> it) {
		mContext = context;
		mItems = it;
		mInflater = LayoutInflater.from(mContext);
		mHolders = new ArrayList<ViewHolder>();
		
		
	}
	
	private Intent getIntent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCount() {
		return mItems.size();
		
		
	}
	
	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

/*	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mHolders.size() <= position) {
			convertView = mInflater.inflate(R.layout.validpoint_adapter, null);
			ViewHolder holder = new ViewHolder();
			holder.mSerNum = (TextView) convertView
					.findViewById(R.id.tv_valid_point_serno);
			holder.mDeep = (TextView) convertView
					.findViewById(R.id.tv_valid_point_deep);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.tv_valid_point_time);
			 holder.mtype = (TextView) convertView
					 .findViewById(R.id.tv_valid_point_type);
			// holder.mId = (TextView) convertView
			// .findViewById(R.id.tv_valid_point_id);
			holder.mConvertView = convertView;
			Log.e("testlog","mHolders.size="+mHolders.size()+"  position="+position);
			mHolders.add(position, holder);
			convertView.setTag(mHolders.get(position));
		} else {
			convertView = mHolders.get(position).mConvertView;
		}
		mHolders.get(position).mSerNum.setText(mItems.get(position).serNum);
		mHolders.get(position).mDeep.setText(mItems.get(position).deep);
		mHolders.get(position).mTime.setText(mItems.get(position).time);
		// mHolders.get(position).mId.setText(mItems.get(position).id);
		mHolders.get(position).mtype.setText(mItems.get(position).type);

		return convertView;
	}*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.validpoint_adapter, null);
			holder = new ViewHolder();
			holder.mSerNum = (TextView) convertView
					.findViewById(R.id.tv_valid_point_serno);
			holder.mDeep = (TextView) convertView
					.findViewById(R.id.tv_valid_point_deep);
			holder.mTime = (TextView) convertView
					.findViewById(R.id.tv_valid_point_time);
			 holder.mfangshi = (TextView) convertView
					 .findViewById(R.id.tv_valid_point_fangshi);
			 holder.mtype = (TextView) convertView
					 .findViewById(R.id.tv_valid_point_type);
			
			// holder.mId = (TextView) convertView
			// .findViewById(R.id.tv_valid_point_id);
			holder.mConvertView = convertView;
			Log.e("testlog","mHolders.size="+mHolders.size()+"  position="+position);
//			mHolders.add(position, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		holder.mSerNum.setText(mItems.get(position).serNum);
		holder.mDeep.setText(String.valueOf(mItems.get(position).deep));
		holder.mTime.setText(mItems.get(position).time);
		holder.mtype.setText(mItems.get(position).type);
		// mHolders.get(position).mId.setText(mItems.get(position).id);
		Log.e("mItems.get(position).type", mItems.get(position).type);
		if(mItems.get(position).type.equals("/")||mItems.get(position).type.equals("校起")){
			strfangshi="起钻";
		}
		if(mItems.get(position).type.equals("煤")||mItems.get(position).type.equals("岩")||mItems.get(position).type.equals("校煤")||mItems.get(position).type.equals("校岩")){
			strfangshi="钻进";
		}
		if(mItems.get(position).type.equals("手煤")||mItems.get(position).type.equals("手岩")){
			strfangshi="钻进";
		}
		if(mItems.get(position).type.equals("手")){
			strfangshi="起钻";
		}
		holder.mfangshi.setText(strfangshi);
		Log.e("strfangshi", strfangshi);
		return convertView;
	}
	public void setData(ArrayList<ValidPointInfo> it) {
		mItems = it;
	}

	/* class ViewHolder */
	private class ViewHolder {
		public TextView mSerNum;
		public TextView mDeep;
		public TextView mTime;
		 public TextView mfangshi;
		public TextView mtype;
		public View mConvertView;
	}

}
