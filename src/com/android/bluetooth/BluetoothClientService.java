package com.android.bluetooth;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import android.app.Service;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.antiexplosionphone.Point_Collect;
import com.android.utils.ClsUtils;
import com.android.utils.ConstantDef;
import com.android.utils.Preferences;
import com.android.utils.SharedPreferencesHelper;
import com.android.utils.ShowDataTableActivity;

/**
 * 蓝牙通信服务
 * 
 * @author TY
 * 
 */
public class BluetoothClientService extends Service {

	// 搜索到的远程设备集合
	private List<BluetoothDevice> mDevices = new ArrayList<BluetoothDevice>();
	// 蓝牙适配器
	private final  BluetoothAdapter mBtAdapter = BluetoothAdapter
			.getDefaultAdapter();
	// 蓝牙通讯线程
	private BluetoothTransThread mTransThread;
	// 匹配蓝牙Device
	private BluetoothDevice mPairDevice;
	private SharedPreferencesHelper sp;
	private Set<BluetoothDevice> pairedDevices;
	@Override
	public void onCreate() {
		// 注册Receiver
		IntentFilter discoveryFilter = new IntentFilter();
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
		discoveryFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		discoveryFilter
				.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		registerReceiver(discoveryReceiver, discoveryFilter);

		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(BluetoothTools.ACTION_SEND_DATA);
		registerReceiver(controlReceiver, controlFilter);

		super.onCreate();
		
		sp = new SharedPreferencesHelper(this, "config");
		
	}

	@Override
	public void onStart(Intent intent, int startId) {

		mDevices.clear();

		// 打开蓝牙
		// mBtAdapter.enable();
		// 开启蓝牙发现功能（90秒）
		// Intent discoveryIntent = new
		// Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		// discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
		// 90);
		// discoveryIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(discoveryIntent);
		// 开始搜索
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		mBtAdapter.startDiscovery();
		System.out.println("ClientService onStart");
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {

		if (mTransThread != null) {
			mTransThread.isRun = false;
		}
		unregisterReceiver(discoveryReceiver);
		unregisterReceiver(controlReceiver);
		// 关闭蓝牙
		if (mBtAdapter.isDiscovering()) {
			mBtAdapter.cancelDiscovery();
		}
		// mBtAdapter.disable();
		System.out.println("ClientService onDestroy");
		super.onDestroy();
	}

	BroadcastReceiver controlReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			
			if (BluetoothTools.ACTION_SEND_DATA.equals(action)) {
				// 通过蓝牙发送数据
				Object data = intent.getSerializableExtra(BluetoothTools.DATA);
				if (mTransThread != null) {
					mTransThread.writeObject(data);
				}
			}

		}
	};

	/**
	 * 蓝牙搜索BroadcastReceiver
	 */
	BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			pairedDevices=mBtAdapter.getBondedDevices();
			if(Point_Collect.isJie==true){
				
			}else{
			if(pairedDevices.size()>0){
				for(BluetoothDevice device : pairedDevices  ){
					if(device.getAddress().equals(Preferences.getAddress(BluetoothClientService.this))){
						  
						Log.e("device.getAddress()", device.getAddress());
						Log.e("Preferences.getAddress(BluetoothClientService.this)", Preferences.getAddress(BluetoothClientService.this));
						
					mPairDevice=device;
					new BluetoothClientConnectThread(handler,
							device).start();
					break;
					}else{
						ss(context, intent, action);
					}
				}
				
			}else{
				ss(context, intent, action);
			}
			//ss(context, intent, action);
		}
		}
		private void ss(Context context, Intent intent, String action) {
			if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {// 开始搜索

				System.out.println("BlueTooth Discovery Started!");
			} 
			else 
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {// 发现远程蓝牙设备

				// 获取设备
				BluetoothDevice bluetoothDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mDevices.add(bluetoothDevice);
			//	System.out.println(bluetoothDevice.getName());
			//	System.out.println(bluetoothDevice.getAddress());

			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {// 搜索结束

				if (mDevices.isEmpty()) {
					// 若未找到设备，则发动未发现设备广播
					Intent foundIntent = new Intent(
							BluetoothTools.ACTION_NOT_FOUND_DEVICE);
					sendBroadcast(foundIntent);
					System.out
							.println("BlueTooth Discovery Finished, Not Found Devices!");
				} else {
					System.out
							.println("BlueTooth Discovery Finished, Found Devices!");
					for (BluetoothDevice device : mDevices) {
						try {

							if (device.getName().equals(Preferences.getBluetooth(BluetoothClientService.this))) {
								Log.e("device.getName()", device.getName());
								
								Log.e("sp.get",Preferences.getBluetooth(BluetoothClientService.this));
								// if(device.getName().equals("linvor")) {// 测试用
								mPairDevice = device;
								// 实现自动配对
								if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
									System.out.println("蓝牙未配对！");
									// 未配对
									try {
										ClsUtils.createBond(device.getClass(),
												device);
									} catch (Exception e) {
										// TODO: handle exception
										System.out.println("蓝牙未配对异常！");
									}
								}
							else {
									System.out.println("蓝牙已经配对！");
									// 找到匹配的设备，尝试连接
									new BluetoothClientConnectThread(handler,
											device).start();
									Preferences.saveAddress(BluetoothClientService.this, device.getAddress());
									
								}
								// 发送发现设备广播
								Intent deviceListIntent = new Intent(
										BluetoothTools.ACTION_FOUND_DEVICE);
								deviceListIntent.putExtra(
										BluetoothTools.STRING_DEVICE, mPairDevice);
								sendBroadcast(deviceListIntent);
								return;
							}
						} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

					}
					// 机芯配对失败，发送匹配失败广播
					Intent pairfailedIntent = new Intent(
							BluetoothTools.ACTION_PAIR_DEVICE_FAILED);
					sendBroadcast(pairfailedIntent);
				}
			} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getName().equals(Preferences.getBluetooth(BluetoothClientService.this))) {
					Log.e("13333333333333222", device.getName());
					Log.e("3333333333333222", Preferences.getBluetooth(BluetoothClientService.this));
					// if(device.getName().equals("linvor")) {// 测试用
					switch (device.getBondState()) {
					case BluetoothDevice.BOND_BONDING:
						System.out.println("正在配对:" + device.getName());
						
						break;
					case BluetoothDevice.BOND_BONDED:
try {
	System.out.println("完成配对:" + mPairDevice.getName());
						} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
						// 发送配对成功消息
						handler.obtainMessage(
								BluetoothTools.MESSAGE_PAIR_SUCCSES)
								.sendToTarget();
						break;
					case BluetoothDevice.BOND_NONE:
						System.out.println("取消配对");
						break;
					default:
						break;
					}
				}
			} else if (action
					.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
				System.out.println("PAIRING_REQUEST onReceive!");
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getName().equals(Preferences.getBluetooth(BluetoothClientService.this))) {
					Log.e("114444444444444444",device.getName());
					Log.e("4444444444444444", Preferences.getBluetooth(BluetoothClientService.this));
					// if(device.getName().equals("linvor")) {// 测试用
					try {
						// 手机和蓝牙采集器配对
						ClsUtils.setPin(device.getClass(), device,
								Preferences.getCodeToHeart(context));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	};

	/**
	 * 接收其他线程消息的Handler
	 */
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 处理消息
			switch (msg.what) {
			case BluetoothTools.MESSAGE_PAIR_SUCCSES:
				// 找到匹配的设备，尝试连接
				new BluetoothClientConnectThread(handler, mPairDevice).start();
				break;
			case BluetoothTools.MESSAGE_CONNECT_ERROR:
				// 连接错误
				// 发送连接错误广播
				Intent errorIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_ERROR);
				sendBroadcast(errorIntent);
				System.out
						.println("BluetoothClientService MESSAGE_CONNECT_ERROR");
				break;
			case BluetoothTools.MESSAGE_CONNECT_SUCCESS:
				// 连接成功
				System.out
						.println("BluetoothClientService MESSAGE_CONNECT_SUCCESS!");
				// 开启通讯线程
				mTransThread = new BluetoothTransThread(handler,
						(BluetoothSocket) msg.obj);
				mTransThread.start();

				// 发送连接成功广播
				Intent succIntent = new Intent(
						BluetoothTools.ACTION_CONNECT_SUCCESS);
				sendBroadcast(succIntent);
				System.out
						.println("BluetoothClientService MESSAGE_CONNECT_SUCCESS");
				break;
			case BluetoothTools.MESSAGE_READ_DATA:
				// 读取到对象
				// 发送数据广播（包含数据对象）
				Intent dataIntent = new Intent(BluetoothTools.ACTION_READ_DATA);
				dataIntent
						.putExtra(BluetoothTools.DATA, (Serializable) msg.obj);
				sendBroadcast(dataIntent);
				System.out
						.println("BluetoothClientService MESSAGE_READ_OBJECT");
				break;
			}
			super.handleMessage(msg);
		}
	};
	
       

//	public static void close() {
//		// TODO Auto-generated method stub
//		//BluetoothClientService.close();
//		BluetoothAdapter.getDefaultAdapter().disable();
//	}

}
