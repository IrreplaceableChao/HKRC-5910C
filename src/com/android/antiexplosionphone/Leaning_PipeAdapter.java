package com.android.antiexplosionphone;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.bluetooth.BluetoothService;
import com.android.utils.Preferences;
import com.android.utils.PublicValues;
import com.android.utils.SharedPreferencesHelper;
import com.android.utils.SysExitUtil;

public class Leaning_PipeAdapter extends Activity {
	
	TextView timeview;
	String UPDATE = "updateTime";
	Button fanhuizhuye,settanguan,setdayinji,searchnewtg,searchnewprinter;
	TextView sptanguan ;
    private BluetoothAdapter mBtAdapter;
    ProgressBar progressBar1 , progressBar2;
    private ArrayAdapter<String> mNewDevicesArrayAdapter,pringDervicesArrayAdapter;
    private BluetoothDevice bluetoothDevice;
    private String  name;
    Set<BluetoothDevice> pairedDevices ;
    List<String> listDevices = new ArrayList<String>();
    List<BluetoothDevice> Devices = new ArrayList<BluetoothDevice>();
    List<String> listPrintDevices = new ArrayList<String>();
    List<BluetoothDevice> printDevices = new ArrayList<BluetoothDevice>();
    MReceiver mReceiver = null;
    SharedPreferencesHelper sp;
    int set1,set2;
    int status ; //状态设置
    private BluetoothService mService = null;
    int count = 1;
    int type = 0 ;
    Button help ; 
    Dialog dialog;
    ListView listview;
    DuankouAdapter adapter;
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SysExitUtil.getAppManager().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leaning_pipeadapter);
		init();
		 listview = (ListView) findViewById(R.id.duankou_listview);
	     adapter  = new DuankouAdapter();
	     listview.setAdapter(adapter);
	     adapter.notifyDataSetChanged();  
	}
	
	private void init(){
		dialog = new Dialog(Leaning_PipeAdapter.this, R.style.MyDialog);
		sp = new SharedPreferencesHelper(this, "config");
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		fanhuizhuye = (Button) findViewById(R.id.fanhuizhuye);
		searchnewtg = (Button) findViewById(R.id.Button02);
		settanguan = (Button) findViewById(R.id.Button01);
		sptanguan = (TextView) findViewById(R.id.Spinner01);
		fanhuizhuye.setOnClickListener(new monclicklistener());  
		searchnewtg.setOnClickListener(new monclicklistener());     
		settanguan.setOnClickListener(new monclicklistener());

		mService = new BluetoothService(Leaning_PipeAdapter.this,mHandler);
		
		mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name,listDevices);
		pringDervicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name,listPrintDevices);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
            	if(istanguan(device.getName())){
                	Devices.add(device);
                	listDevices.add(device.getName());
                	mNewDevicesArrayAdapter.notifyDataSetChanged();	
            	}else{
            		printDevices.add(device);
            		listPrintDevices.add(device.getName());
            		pringDervicesArrayAdapter.notifyDataSetChanged();
            	}
            }
        } else {
        	
        	
        }
        

        
        if(Devices.size()<=0){
        	settanguan.setEnabled(false);
        }
      //  sptanguan.setAdapter(mNewDevicesArrayAdapter);
        
        if(sp.getString("tanguanname")!=null){
        	for(int i=0 ; i<listDevices.size() ; i++){
        		if(listDevices.get(i).contains(sp.getString("tanguanname"))){
        			//sptanguan.setSelection(i);
        		}
        	}
        }
        
	}
	
	private class monclicklistener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int id = arg0.getId();
			switch (id) {
			case R.id.fanhuizhuye:
				mService.stop();
				Leaning_PipeAdapter.this.finish();
				break;
			case R.id.Button02:
				status = 1;
				mBtAdapter.startDiscovery();//搜索
				mReceiver = new MReceiver();
				try {
				    Method m = Devices.get(set1).getClass()
				        .getMethod("removeBond", (Class[]) null);
				    m.invoke(Devices.get(set1), (Object[]) null);
				} catch (Exception e) {
					  Log.e("", e.getMessage());
				}
		        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		        Leaning_PipeAdapter.this.registerReceiver(mReceiver, filter);
		        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		        Leaning_PipeAdapter.this.registerReceiver(mReceiver, filter);
		        jingshiinfo("正在搜索适配器...",true);
		        adapter.notifyDataSetChanged(); 
				break;
			case R.id.Button01:
				status = 1;
				PublicValues.device = Devices.get(set1);
				Log.e("shipeishebei","size="+Devices.size()+"set1="+set1+"devicename"+Devices.get(set1).getName());
				Preferences.saveBluetooth(Leaning_PipeAdapter.this,sptanguan.getText().toString());
				mService.connect(PublicValues.device);				
				type = R.id.Button01;
				count = 1;
				jingshiinfo("正在配对适配器...",true);
				break;
			}
		}
		
	}
	
	protected void onDestroy() {
		if(mReceiver!= null){
			this.unregisterReceiver(mReceiver);	
		}
		
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
    private class  MReceiver extends  BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	String tempString = "";
				//添加设备				
            	tempString += device.getName();
				//防止重复添加

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

    					
    					switch (status) {
						case 1:
							if(istanguan(device.getName())&&listDevices.indexOf(tempString) == -1){
		    					Devices.add(device);
		    					listDevices.add(tempString);
		    					mNewDevicesArrayAdapter.notifyDataSetChanged();
		    					settanguan.setEnabled(true);
		    					adapter.notifyDataSetChanged();
		    					jingshiinfo("",false);
							}
							break;

						}
    					

    				}
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                jingshiinfo("",false);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                }
            }
        }
    };
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
            		
            		switch (status) {
					case 1:
						Toast.makeText(getApplicationContext(), "适配器设置成功",Toast.LENGTH_SHORT).show();
						jingshiinfo("",false);
						
						break;
					}
                    break;
                case BluetoothService.STATE_CONNECTING:

                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:

                    break;
                }
                break;
//            case 159:
//            case 753:
//                break;
            case 5:
            	if(count<=4){
            		switch (type) {
					case R.id.Button01:
						mService.stop();
						mService.connect(PublicValues.device);
						break;
					}
            		count++;
            	}else{
            		switch (status) {
					case 1:
	            		Toast.makeText(Leaning_PipeAdapter.this,"适配器设置成功！",
	                            Toast.LENGTH_LONG).show();
						break;

					}
            		

            		settanguan.setText("设置适配器");
            		jingshiinfo("",false);
            	}
                    

                break;
            case 6:
            	break;
            }
        }
    };
    
    private boolean istanguan (String name){
    	if(name!=null){
        	if(name.indexOf("HK59-03A")>=0){
        		return true;
        	}else{
        		return false;
        	}
    	}else{
    		return false;
    	}

    }
    
    
	
	private void jingshiinfo(String s,boolean b){
		TextView message;
        //设置它的ContentView
        dialog.setContentView(R.layout.blank_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        message = (TextView) dialog.findViewById(R.id.message);
        message.setText(s);
        if(b){
        	 dialog.show();	
        }else{
        	dialog.dismiss();
        }
       
	}
	
	public class DuankouAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			
		
			return listDevices.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			// 观察convertView随ListView滚动情况
			if (convertView == null) {
				convertView = LayoutInflater.from(Leaning_PipeAdapter.this).inflate(R.layout.duankou_tablerow,null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */
				holder.text1 = (TextView) convertView.findViewById(R.id.text1);
				convertView.setTag(holder);// 绑定ViewHolder对象
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			
			holder.text1.setText(listDevices.get(position));
			
			holder.text1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
				 name=holder.text1.getText().toString();
				sptanguan.setText(name);
    				
				}
			});
			return convertView;
		}
		
		/* 存放控件 */

		public final class ViewHolder {
			public TextView text1;
			public TextView text2;
			public TextView text3;
		}
	}

}
