package com.android.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

/**
 * 蓝牙连接线程
 * 
 * @author TY
 * 
 */
public class BluetoothClientConnectThread extends Thread {
	private Handler serviceHandler; // 用于向客户端Service回传消息的handler
	private BluetoothDevice serverDevice; // 服务器设备
	private BluetoothSocket socket; // 通信Socket

	/**
	 * 构造函数
	 * 
	 * @param handler
	 * @param serverDevice
	 */
	public BluetoothClientConnectThread(Handler handler,
			BluetoothDevice serverDevice) {
		this.serviceHandler = handler;
		this.serverDevice = serverDevice;
	}

	@Override
	public void run() {
		try {
			socket = serverDevice
					.createRfcommSocketToServiceRecord(BluetoothTools.BLUETOOTH_UUID);
			BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
			socket.connect();
			// 发送连接成功消息，消息的obj参数为连接的socket
			Message msg = serviceHandler.obtainMessage();
			msg.what = BluetoothTools.MESSAGE_CONNECT_SUCCESS;
			msg.obj = socket;
			msg.sendToTarget();
			System.out.println("BluetoothClientConnectThread Success!");
		} catch (Exception ex) {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 发送连接失败消息
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR)
					.sendToTarget();
			System.out.println("BluetoothClientConnectThread Error!");
		}

	}
}
