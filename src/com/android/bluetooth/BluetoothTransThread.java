package com.android.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.android.utils.TransProtocol;

/**
 * 蓝牙通信线程
 * 
 * @author TY
 * 
 */
public class BluetoothTransThread extends Thread {

	private Handler serviceHandler; // 与Service通信的Handler
	private BluetoothSocket socket;
	private InputStream inStream; // 对象输入流
	private OutputStream outStream; // 对象输出流
	public volatile boolean isRun = true; // 运行标志位

	/**
	 * 构造函数
	 * 
	 * @param handler
	 *            用于接收消息
	 * @param socket
	 */
	public BluetoothTransThread(Handler handler, BluetoothSocket socket) {
		this.serviceHandler = handler;
		this.socket = socket;
		try {
			this.outStream = socket.getOutputStream();
			this.inStream = socket.getInputStream();
		} catch (Exception e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			// 发送连接失败消息
			serviceHandler.obtainMessage(BluetoothTools.MESSAGE_CONNECT_ERROR)
					.sendToTarget();
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			if (!isRun) {
				break;
			}
			byte[] bytes = new byte[TransProtocol.PROTOCOL_LEN];
			int readCnt = 0;
			try {
				while (readCnt < TransProtocol.PROTOCOL_LEN) {
					readCnt += inStream.read(bytes, readCnt,
							TransProtocol.PROTOCOL_LEN - readCnt);
					// System.out.println("readCnt = "+readCnt);
				}
				// System.out.println("TotalCnt = "+readCnt);
				readCnt = 0;
				BluetoothTransData data = new BluetoothTransData();
				data.setData(bytes);
				// 发送成功读取到对象的消息，消息的obj参数为读取到的对象
				Message msg = serviceHandler.obtainMessage();
				msg.what = BluetoothTools.MESSAGE_READ_DATA;
				msg.obj = data;
				msg.sendToTarget();
			} catch (Exception ex) {
				// 发送连接失败消息
				serviceHandler.obtainMessage(
						BluetoothTools.MESSAGE_CONNECT_ERROR).sendToTarget();
				ex.printStackTrace();
				return;
			}
		}

		// 关闭流
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 写入一个可序列化的对象
	 * 
	 * @param obj
	 */
	public void writeObject(Object obj) {
		if (socket == null) {
			return;
		}
		BluetoothTransData data = (BluetoothTransData) obj;
		try {
			outStream.write(data.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
