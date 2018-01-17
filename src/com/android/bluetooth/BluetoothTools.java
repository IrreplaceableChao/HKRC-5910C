package com.android.bluetooth;

import java.util.UUID;

import android.content.Context;
import android.content.Intent;

import com.android.utils.TransProtocol;

/**
 * 蓝牙工具类
 * 
 * @author TY
 * 
 */
public class BluetoothTools {

	/**
	 * 本程序所使用的UUID
	 */
	public static UUID BLUETOOTH_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");

	/**
	 * String:存放在Intent中的设备对象
	 */
	public static final String STRING_DEVICE = "DEVICE";

	/**
	 * String:Intent中的蓝牙收发数据标识
	 */
	public static final String DATA = "DATA";

	/**
	 * Action:读取到蓝牙数据
	 */
	public static final String ACTION_READ_DATA = "ACTION_READ_DATA";

	/**
	 * Action:发送到蓝牙的数据
	 */
	public static final String ACTION_SEND_DATA = "ACTION_SEND_DATA";

	/**
	 * Action:设备列表
	 */
	public static final String ACTION_FOUND_DEVICE = "ACTION_FOUND_DEVICE";

	/**
	 * Action:未发现设备
	 */
	public static final String ACTION_NOT_FOUND_DEVICE = "ACTION_NOT_FOUND_DEVICE";

	/**
	 * Action:开始配对
	 */
	public static final String ACTION_PAIR_DEVICE_START = "ACTION_PAIR_DEVICE_START";

	/**
	 * Action:配对失败
	 */
	public static final String ACTION_PAIR_DEVICE_FAILED = "ACTION_PAIR_DEVICE_FAILED";

	/**
	 * Action:连接成功
	 */
	public static final String ACTION_CONNECT_SUCCESS = "ACTION_CONNECT_SUCCESS";
	/**
	 * Action:连接错误
	 */
	public static final String ACTION_CONNECT_ERROR = "ACTION_CONNECT_ERROR";

	/**
	 * Message:配对成功
	 */
	public static final int MESSAGE_PAIR_SUCCSES = 0x00000001;
	/**
	 * Message:连接成功
	 */
	public static final int MESSAGE_CONNECT_SUCCESS = 0x00000002;

	/**
	 * Message:连接失败
	 */
	public static final int MESSAGE_CONNECT_ERROR = 0x00000003;

	/**
	 * Message:蓝牙通信线程读取到数据
	 */
	public static final int MESSAGE_READ_DATA = 0x00000004;

	/**
	 * 通过广播发送数据到蓝牙通信线程
	 * 
	 * @param bytes
	 *            --发送数据字节数组
	 */
	public static void sendData(Context context, byte[] bytes, int cmdType) {

		BluetoothTransData data = new BluetoothTransData();
		data.setData(bytes);
		Intent sendDataIntent = new Intent(BluetoothTools.ACTION_SEND_DATA);
		sendDataIntent.putExtra(BluetoothTools.DATA, data);
		context.sendBroadcast(sendDataIntent);
		TransProtocol.setCurCMDType(cmdType);
	}

	/**
	 * 接收广播接收蓝牙通信线程读取的数据
	 * 
	 * @param intent
	 *            --接收广播Intent
	 * @return
	 */
	public static byte[] readData(Intent intent) {
		BluetoothTransData data = (BluetoothTransData) intent.getExtras()
				.getSerializable(BluetoothTools.DATA);
		return data.getData();
	}

}
