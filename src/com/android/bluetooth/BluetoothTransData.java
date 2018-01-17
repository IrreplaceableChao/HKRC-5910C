package com.android.bluetooth;

import java.io.Serializable;

import com.android.utils.TransProtocol;

/**
 * 蓝牙数据包类
 * 
 * @author TY
 * 
 */
public class BluetoothTransData implements Serializable {

	private static final long serialVersionUID = 1L;
	private byte[] data = new byte[TransProtocol.PROTOCOL_LEN];

	public BluetoothTransData() {
		for (int i = 0; i < data.length; i++)
			data[i] = 0;
	}

	public void setData(byte[] bytes) {
		System.arraycopy(bytes, 0, data, 0, data.length);
	}

	public byte[] getData() {
		return data;
	}
}
