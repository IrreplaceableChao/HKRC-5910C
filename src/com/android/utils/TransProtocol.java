package com.android.utils;

import java.text.DecimalFormat;

import android.util.Log;

import static android.R.attr.gravity;

/**
 * 通信协议类
 * 
 * @author TY
 * 
 */
public class TransProtocol {

	public static final int PROTOCOL_LEN = 26;

	public static final int CMD_TYPE_END = -1; // 命令等同于STOP命令,仅仅用来标记通信结束
	public static final int CMD_TYPE_NONE = 0;
	public static final int CMD_TYPE_STOP = 1;
	public static final int CMD_TYPE_SELFCHK = 2;
	public static final int CMD_TYPE_SEND_DELAY_INTERVAL = 3;
	public static final int CMD_TYPE_SEND_HOLE_ID0 = 4;
	public static final int CMD_TYPE_SEND_HOLE_ID1 = 5;
	public static final int CMD_TYPE_START = 6;
	public static final int CMD_TYPE_READ_COLLECTCNT_ETC = 7;
	public static final int CMD_TYPE_READ_STORAGE_DATA0 = 8;
	public static final int CMD_TYPE_READ_STORAGE_DATA1 = 9;

	// zgl
	// public static final int CMD_TYPE_BY = 10;
	// public static final int CMD_TYPE_FACE = 11;
	// public static final int CMD_TYPE_DRILLING = 12;

	private static int mCurCMDType = CMD_TYPE_NONE;

	public static void setCurCMDType(int type) {
		mCurCMDType = type;
	}

	public static int getCurCMDType() {
		return mCurCMDType;
	}

	/**
	 * 停止指令
	 */
	public static final byte[] CMD_STOP = { (byte) 0x2A, (byte) 0xB1,
			(byte) 0xB1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, (byte) 0x8C };
	/**
	 * 自检指令
	 */
	public static final byte[] CMD_SELFCHK = { (byte) 0x2A, (byte) 0xA7,
			(byte) 0xA7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, (byte) 0x78 };

	/**
	 * 启动指令
	 */
	public static final byte[] CMD_START = { (byte) 0x2A, (byte) 0xA5,
			(byte) 0xA5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, (byte) 0x74 };

	/**
	 * 读取采集点数、延时时间、间隔时间指令
	 */
	public static final byte[] CMD_READ_COLLECTCNT_ETC = { (byte) 0x2A,
			(byte) 0x81, (byte) 0x03, (byte) 0xFF, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0xAD };

	/**
	 * 写延时时间、间隔时间指令
	 */
	public static class CMD_Send_Delay_Interval {

		private static byte[] cmdData = { (byte) 0x2A, (byte) 0x82,
				(byte) 0x09, (byte) 0x03, (byte) 0xFF, 0, 0, 0, 0, (byte) 0xA1,
				(byte) 0xAA, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		/**
		 * @param delay
		 *            -----延时时间
		 * @param interval
		 *            --间隔时间
		 */
		public static byte[] getSendCmd(char delay, char interval) {

			cmdData[11] = (byte) ((delay >> 8) & 0xFF);
			cmdData[12] = (byte) (delay & 0xFF);
			cmdData[13] = (byte) ((interval >> 8) & 0xFF);
			cmdData[14] = (byte) (interval & 0xFF);

			// 计算校验值
			int chksum = 0;
			for (int i = 0; i < PROTOCOL_LEN - 1; i++) {
				chksum += cmdData[i] & 0xFF;
			}
			cmdData[25] = (byte) (chksum & 0xFF);

			return cmdData;
		}
	}

	/**
	 * 写孔号指令
	 */
	public static class CMD_Send_HoleID {

		private static byte[] cmdData = { (byte) 0x2A, (byte) 0x82,
				(byte) 0x0A, (byte) 0x03, (byte) 0xFF, (byte) 0x1D, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		/**
		 * @param strHoleId
		 *            --孔号字串(占20字节)
		 * @param bFirst
		 *            -----是否为孔号的前10个字节
		 */
		public static byte[] getSendCmd(String strHoleId, String strby,
				String face, String drilling, boolean bFirst) {

			byte[] bytesHoleid = { 0x20, 0x20, 0x20, 0x20 };
			// System.arraycopy(Utils.getBytes(strHoleId), 0, bytesHoleid, 0,
			// bytesHoleid.length);
			if (Utils.getBytes(strHoleId).length < 2) {

				bytesHoleid[0] = Utils.getBytes(strHoleId)[0];

			} else if (Utils.getBytes(strHoleId).length < 3) {

				bytesHoleid[0] = Utils.getBytes(strHoleId)[0];
				bytesHoleid[1] = Utils.getBytes(strHoleId)[1];

			} else if (Utils.getBytes(strHoleId).length < 4) {
				bytesHoleid[0] = Utils.getBytes(strHoleId)[0];
				bytesHoleid[1] = Utils.getBytes(strHoleId)[1];
				bytesHoleid[2] = Utils.getBytes(strHoleId)[2];
			} else {
				bytesHoleid[0] = Utils.getBytes(strHoleId)[0];
				bytesHoleid[1] = Utils.getBytes(strHoleId)[1];
				bytesHoleid[2] = Utils.getBytes(strHoleId)[2];
				bytesHoleid[3] = Utils.getBytes(strHoleId)[3];
			}
			byte[] bytesstrby = { 0x20, 0x20 };
			// System.arraycopy(Utils.getBytes(strby), 0, bytesstrby, 0,
			// bytesstrby.length);
			if (Utils.getBytes(strby).length < 2) {
				bytesstrby[0] = Utils.getBytes(strby)[0];
			} else {
				bytesstrby[0] = Utils.getBytes(strby)[0];
				bytesstrby[1] = Utils.getBytes(strby)[1];
			}

			byte[] bytesface = { 0x20, 0x20, 0x20, 0x20, 0x20, 0x20 };
			// System.arraycopy(Utils.getBytes(face), 0, bytesface, 0,
			// bytesface.length);
			if (Utils.getBytes(face).length < 2) {
				bytesface[0] = Utils.getBytes(face)[0];

			} else if (Utils.getBytes(face).length < 3) {
				bytesface[0] = Utils.getBytes(face)[0];
				bytesface[1] = Utils.getBytes(face)[1];

			} else if (Utils.getBytes(face).length < 4) {

				bytesface[0] = Utils.getBytes(face)[0];
				bytesface[1] = Utils.getBytes(face)[1];
				bytesface[2] = Utils.getBytes(face)[2];
			} else if ((Utils.getBytes(face).length < 5)) {

				bytesface[0] = Utils.getBytes(face)[0];
				bytesface[1] = Utils.getBytes(face)[1];
				bytesface[2] = Utils.getBytes(face)[2];
				bytesface[3] = Utils.getBytes(face)[3];
			} else if ((Utils.getBytes(face).length < 6)) {
				bytesface[0] = Utils.getBytes(face)[0];
				bytesface[1] = Utils.getBytes(face)[1];
				bytesface[2] = Utils.getBytes(face)[2];
				bytesface[3] = Utils.getBytes(face)[3];
				bytesface[4] = Utils.getBytes(face)[4];
			} else {
				bytesface[0] = Utils.getBytes(face)[0];
				bytesface[1] = Utils.getBytes(face)[1];
				bytesface[2] = Utils.getBytes(face)[2];
				bytesface[3] = Utils.getBytes(face)[3];
				bytesface[4] = Utils.getBytes(face)[4];
				bytesface[5] = Utils.getBytes(face)[5];
			}

			byte[] bytesdrilling = { 0x20, 0x20 };
			//System.arraycopy(Utils.getBytes(drilling), 0, bytesdrilling, 0,
			//		bytesdrilling.length);
			if (Utils.getBytes(drilling).length < 1) {
				bytesdrilling[1] = 0x20;
			} else if (Utils.getBytes(drilling).length < 2) {

				bytesdrilling[1] = Utils.getBytes(drilling)[0];

			} else {

				bytesdrilling[0] = Utils.getBytes(drilling)[0];
				bytesdrilling[1] = Utils.getBytes(drilling)[1];
			}
			// zgl
			Log.e("mylog", "bytesHoleid=" + bytesHoleid.length
					+ "  bytesstrby=" + bytesstrby.length + "  bytesface="
					+ bytesface.length + "  bytesdrilling="
					+ bytesdrilling.length);

			if (bytesHoleid.length > 20)
				return null;
			byte[] holdIDDefault = {// 0x20 为空格
			0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20,
					0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20 };
			//System.arraycopy(bytesHoleid, 0, holdIDDefault, 0,
					//bytesHoleid.length);
			holdIDDefault[0] = bytesstrby[0];
			holdIDDefault[1] = bytesstrby[1];
			holdIDDefault[2] = bytesface[0];
			holdIDDefault[3] = bytesface[1];
			holdIDDefault[4] = bytesface[2];
			holdIDDefault[5] = bytesface[3];
			holdIDDefault[6] = bytesface[4];
			holdIDDefault[7] = bytesface[5];
			holdIDDefault[8] = bytesdrilling[0];
			holdIDDefault[9] = bytesdrilling[1];
			holdIDDefault[10] = bytesHoleid[0];
			holdIDDefault[11] = bytesHoleid[1];
			holdIDDefault[12] = bytesHoleid[2];
			holdIDDefault[13] = bytesHoleid[3];

			if (bFirst) {
				cmdData[5] = (byte) 0x1D;
				for (int i = 0; i < 10; i++) {
					cmdData[i + 6] = holdIDDefault[i];
				}
			} else {
				cmdData[5] = (byte) 0x27;
				for (int i = 0; i < 10; i++) {
					cmdData[i + 6] = holdIDDefault[i + 10];
				}
			}

			// 计算校验值
			int chksum = 0;
			for (int i = 0; i < PROTOCOL_LEN - 1; i++) {
				chksum += cmdData[i] & 0xFF;
			}
			cmdData[25] = (byte) (chksum & 0xFF);
			// zgl
			String a = "";
			for (int i = 0; i < cmdData.length; i++) {
				a = a + ",";
				a = a + cmdData[i];
			}
			Log.e("mylog", "cmdData="+a);
			return cmdData;
		}
	}

	/**
	 * 读取采集数据指令
	 */
	public static class CMD_Send_ReadStorageData {

		private static byte[] cmdData = { (byte) 0x2A, (byte) 0x81, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		/**
		 * @param startAddr
		 *            --数据起始地址
		 * @param bFirst
		 *            -----是否为第一片存储器
		 */
		public static byte[] getSendCmd(int addr, boolean bFirst) {

			if (bFirst) {
				cmdData[1] = (byte) 0x81;
			} else {
				cmdData[1] = (byte) 0x91;
			}
			cmdData[2] = (byte) (addr >> 16 & 0xFF);
			cmdData[3] = (byte) (addr >> 8 & 0xFF);
			cmdData[4] = (byte) (addr & 0xFF);

			// 计算校验值
			int chksum = 0;
			for (int i = 0; i < PROTOCOL_LEN - 1; i++) {
				chksum += cmdData[i] & 0xFF;
			}
			cmdData[25] = (byte) (chksum & 0xFF);
			return cmdData;
		}
	}

	/**
	 * 自检命令应答
	 */
	public static class ANS_Rev_SelfChk {

		/**
		 * 自检应答结构
		 */
		public static class ANS_SELFCHECK {
			public float angle; // 倾角
			public float position; // 方位
			public float checksum; // 校验和
			// public short magnetic; // 磁场强度
			public float tempera; // 温度
			public float voltage; // 电池电压
			public float gravity; //重力高边
		}

		/**
		 * @param data
		 *            --自检应答字节数组
		 */
		public static ANS_SELFCHECK getAns(byte[] data) {

			ANS_SELFCHECK ans = new ANS_SELFCHECK();
			ans.angle =  (float) ((data[1] << 8) | (data[2] & 0xff)) / 100.00f - 90.00f;
			ans.position = (float) ((data[3] << 8) | (data[4] & 0xff)) / 100.00f;
			if(ans.position<0)ans.position+=655.36;
			ans.checksum = (float) ((data[5] << 8) | (data[6] & 0xff)) / 1000.000f;

			ans.tempera = (float) ((data[8])) -40.00f;
			
			ans.voltage = (float) ((data[11] << 8) | (data[12] & 0xff)) / 1000.00f;
/**
 * Author: Achao
 * Time: 2017-10-24 11:03:13
 * */
//			ans.angle = 0- ans.angle;
//			ans.position = 180+ ans.position;
//			if (ans.position>360)ans.position = ans.position-360;
//			ans.gravity = 360-ans.gravity;
			return ans;
		}
	}

	/**
	 * 读取采集点数、延时时间、间隔时间指令应答
	 */
	public static class ANS_Rev_Read_CollectCnt_etc {

		/**
		 * 读取采集点数、延时时间、间隔时间指令应答结构
		 */
		public static class ANS_READ_COLLECTCNT_ETC {
			public short collectCnt; // 已采集点数
			public short delayTime; // 延时时间
			public short intervalTime; // 间隔时间
		}

		/**
		 * @param data
		 *            --应答字节数组
		 */
		public static ANS_READ_COLLECTCNT_ETC getAns(byte[] data) {

			ANS_READ_COLLECTCNT_ETC ans = new ANS_READ_COLLECTCNT_ETC();
			ans.collectCnt = (short) ((data[1] << 8) | (data[2] & 0xff));
			/**手机增加处理如果超过21000点数据 只接受21000数据  AChao 2017-9-18 12:12:34*/
			if (ans.collectCnt >= 21000) ans.collectCnt = 21000;
			ans.delayTime = (short) ((data[6] << 8) | (data[7] & 0xff));
			ans.intervalTime = (short) ((data[8] << 8) | (data[9] & 0xff));

			return ans;
		}

		/**
		 * 提取延时时间、间隔时间数组 （小端）高位高地址 由于协议为大端模式，故需要字节颠倒
		 * 
		 * @param data
		 *            --应答字节数组
		 * @return
		 */
		public static byte[] getAnsDelayIntervalBytes(byte[] data) {

			byte[] bytes = new byte[4];
			bytes[0] = data[7];
			bytes[1] = data[6];
			bytes[2] = data[9];
			bytes[3] = data[8];

			return bytes;
		}
	}

	/**
	 * 读取存储区数据应答,文件名：孔号.YS
	 */
	public static class ANS_Rev_Read_Storage_data {

		/*
		 * 芯片采集点结构,用于界面显示
		 */
		public static class Struct_ReadStorage_Data {
			public String angle; // 倾角
			public String position; // 方位
			public String checksum; // 校验和
			public String magnetic; // 磁场强度
			public String tempera; // 温度
			public String voltage; // 电池电压
			public String gravity;//重力高边

		}

		/**
		 * 分别获取两组数据
		 * 
		 * @param data
		 *            ---------应答字节数组
		 * @param bFirst
		 *            --是否只读取前一组数据
		 * @return
		 */
		public static byte[] getAnsBytes(byte[] data, boolean bFirst) {
			byte[] bytes = new byte[ConstantDef.DATA_REV_POINT_DATA_BYTES];
			if (bFirst) {
				bytes = new byte[ConstantDef.DATA_REV_POINT_DATA_BYTES];
				for (int i = 0; i < ConstantDef.DATA_REV_POINT_DATA_BYTES; i++) {
					bytes[i] = data[i + 1];
				}
			} else {
				for (int i = 0; i < ConstantDef.DATA_REV_POINT_DATA_BYTES; i++) {
					bytes[i] = data[i + 1
							+ ConstantDef.DATA_REV_POINT_DATA_BYTES];
				}
			}
			return bytes;
		}

		/**
		 * 解析芯片有效点数据,获取显示结构
		 * 
		 * @param bytes
		 *            --单个采集点数组12byte
		 * @return
		 */
		public static Struct_ReadStorage_Data getReadStorageData_Struct(
				byte[] data) {
			
			Struct_ReadStorage_Data point = new Struct_ReadStorage_Data();
			float angle = (float) ((data[0] << 8) | (data[1] & 0xff)) / 100.0f - 90.0f;
			float position = (float) ((data[2] << 8) | (data[3] & 0xff)) / 100.0f;
			if(position<0)position+=655.36;
			float gravity=(float) ((data[8] <<8 | (data[9] & 0xff))) / 100.00f;
			if(gravity<0)gravity+=655.36;
			float checksum = (float) ((data[4] <<8 | (data[5] & 0xff))) / 1000.000f;
			float magnetic = (float) ((data[6] ) ) ;
			float tempera = (float) ((data[7] )) -40;
			
			float voltage = (float) ((data[10] << 8) | (data[11] & 0xff)) / 1000.00f;


			/**
			 * Author: Achao
			 * Time: 2017-10-24 11:03:13
			 * */
//			angle = 0- angle;
//			position = 180+ position;
//			if (position>360)position = position-360;
//			gravity = 360-gravity;


			point.angle = new DecimalFormat("###,###,###.###").format(angle);// Float.toString(angle);
			point.position = new DecimalFormat("###,###,###.###")
					.format(position);// Float.toString(position);
			point.checksum = new DecimalFormat("###,###,###.###")
					.format(checksum);// Float.toString(checksum);
			point.magnetic = new DecimalFormat("###,###,###")
					.format(magnetic);// Float.toString(magnetic);
			point.gravity = new DecimalFormat("###,###,###.###")
			.format(gravity);// Float.toString(gravity);
			point.tempera = new DecimalFormat("###,###,###").format(tempera);// Float.toString(tempera);
			point.voltage = new DecimalFormat("###,###,###.###").format(voltage);// Float.toString(voltage);

			return point;
		}
	}

	/**
	 * 单个有效点数据结构，文件名：孔号.YX
	 */
	public static class Efficient_Point {

		/**
		 * 确定有效点结构,用于界面显示
		 */
		public static class Struct_Ensure_Point {
			public int id;
			public String deep;
			public String date;
			public String time;
			public String type;
			public String sign;
		}

		/**
		 * @param id
		 *            ----对应采集点编号0~65535，无符号整形 高位高地址
		 * @param deep
		 *            --对应测量孔深
		 * @param date
		 *            --日期exp:131216
		 * @param time
		 *            --时间exp:231106
		 */
		public static byte[] getFileData(int id, float deep, String date,
				String time, String type) {

			byte[] pointData = new byte[ConstantDef.FILE_VALID_POINT_SINGLE_BYTES];

			pointData[0] = (byte) (id & 0xFF);
			pointData[1] = (byte) ((id >> 8) & 0xFF);
			for (int i = 0; i < 4; i++) {
				pointData[i + 2] = (byte) (Float.floatToIntBits(deep) >> (24 - i * 8));
			}
			byte[] dat = Utils.hexStringToBytes(date);
			for (int i = 0; i < 3; i++) {
				pointData[i + 6] = dat[i];
			}
			///
			byte[] tim = Utils.hexStringToBytes(time);
			for (int i = 0; i < 3; i++) {
				pointData[i + 9] = tim[i];
			}
			// 增加TYPE
			byte[] tpe = Utils.getBytes(type);
			// System.out.println(tpe);
			for (int i = 0; i < tpe.length; i++) {
				pointData[i + 12] = tpe[i];
			}
			//byte[] sin = Utils.getBytes(sign);
			// System.out.println(tpe);
			//for (int i = 0; i < sin.length; i++) {
			//	pointData[i + 16] = sin[i];
			//}
//			for(int i=0;i<15;i++){
//			Log.e("wwwwwwwwwwwwwwwwwwwwww", "wwwwwwwwwwwwwwwwwwwwww");	
//			System.out.println(pointData[i]);
//			}
			return pointData;
		}

		/**
		 * 解析确定有效点单点结构,获取显示结构
		 * 
		 * @param bytes
		 * @return
		 */
		public static Struct_Ensure_Point getEnsurePointStruct(byte[] bytes) {

			Struct_Ensure_Point point = new Struct_Ensure_Point();

			point.id = ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
			point.deep = Float.toString(Utils.getFloat(bytes, 2));
			byte[] data = new byte[4];
			for (int i = 0; i < 3; i++) {
				data[i] = bytes[i + 6];
			}
			point.date = Utils.bytesToHexString(data);
			for (int i = 0; i < 3; i++) {
				data[i] = bytes[i + 9];
			}
			point.time = Utils.bytesToHexString(data);
			for (int i = 0; i < 2; i++) {
				data[i] = bytes[i + 11];
			}
			data = new byte[4];
			 for(int i=0;i<4;i++){
				 data[i] = bytes[i + 12];
			 }
			 point.type=Utils.getString(data);
			 data = new byte[1];
			 for(int i=0;i<1;i++){
				 data[i] = bytes[i + 15];
			 }
			 point.sign=Utils.getString(data);
			return point;
		}
	}
}
