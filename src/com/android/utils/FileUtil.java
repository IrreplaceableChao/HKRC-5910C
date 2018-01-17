package com.android.utils;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;
import android.util.Log;

public class FileUtil {

	String sdpath = Environment.getExternalStorageDirectory().getPath() + "//";  
	String sdfilepath = Environment.getExternalStorageDirectory().getPath() + "//hekangdata//"; 
	FileOutputStream out = null;
	DataOutputStream binaryout = null;
    public File creatSDDir(String dirName) {  
        File dir = new File(sdpath + dirName);  
        dir.mkdir();  
        return dir;  
    } 
    
    public File creatSDDir(String dirName,int i) {  
        File dir = new File( dirName);  
        dir.mkdirs();  
        return dir;  
    }  
    
    public FileOutputStream fileout(String fileName){
    	try {
			out= new FileOutputStream(sdfilepath +fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return out;
    }
    
    public void writefile(String write_str){
    	byte[] bytes = write_str.getBytes();
    	try {
			out.write(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void closefile(){
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d("fileutil", "关闭失败");
			e.printStackTrace();
		}
	}
	public String readFile(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(sdfilepath +fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public void getBinaryOut(String fileName) {
		try {
			binaryout = new DataOutputStream(new BufferedOutputStream( new FileOutputStream(sdfilepath +fileName)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeBinaryString(String write_str) {
		try {	
			byte[] bytt = write_str.getBytes();
			binaryout.write(bytt);
//			binaryout.writeBytes(write_str);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void writeBinaryFloat(float write_val) {
		try {		
			binaryout.writeFloat(write_val);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void closeBinaryOut(String write_str) {
		try {			
			binaryout.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}  
	//----------------------------------------------------------------------//
	public void writeFileSdcardFile(String fileName, String write_str) {

		try {
			FileOutputStream out = new FileOutputStream(sdfilepath +fileName,true);

			byte[] bytes = write_str.getBytes();
			
			out.write(bytes);
			//out.write(bytes);
		}

		catch (Exception e) {
			Log.d("fileutil", "写入失败");
			e.printStackTrace();

		}

	}
	

	
	public void writeZhuijiaFileSdcardFile(String fileName, String write_str) {

		try {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream( new FileOutputStream(sdfilepath +fileName,true)));

//			byte[] bytes = write_str.getBytes();
			
			out.writeBytes(write_str);
			//out.write(bytes);

			out.close();

		}

		catch (Exception e) {

			e.printStackTrace();

		}

	}

	// 读SD中的文件

	public String[] readFileSdcardFile(String fileName) {

		String res = "";
		try {
			FileInputStream fin = new FileInputStream(sdfilepath +fileName);
			int length = fin.available();
			Log.d("fileutil","lenth="+length);
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		}

		catch (Exception e) {

			e.printStackTrace();

		}
		String[] strs = res.split("\r\n");
//		for(int i =0 ; i<strs.length; i++){
//			Log.d("fileutil",strs[i]);
//		}
		return strs;

	}
	
    public File creatSDFile(String fileName) {  
        File file = new File(sdfilepath + fileName);  
        try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return file;  
    }  
   
    private String StrToBinstr(String str) {
        char[] strChar = str.toCharArray();
        String result = "";
        for (int i = 0; i < strChar.length; i++) {
             result += Integer.toBinaryString(strChar[i]) + " ";          
         }
         return result;
    }

}
