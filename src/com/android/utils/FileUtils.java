package com.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 文件操作类
 * 
 * @author TY
 * 
 */
public class FileUtils {

	/*
	 * 创建已存在路径的文件,如文件已存在，返回，不删除已有文件
	 */
	public static File FileCreate(String filePath) {

		if (filePath == null)
			return null;

		File file = new File(filePath);
		if (file.exists()) {
			return file;
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/*
	 * 创建已存在路径的文件,如存在则删除,创建新的
	 */
	public static File FileNewCreate(String filePath) {

		if (filePath == null)
			return null;

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/*
	 * 创建文件或目录-name为空创建目录
	 */
	public static File FileCreate(String dirPath, String name) {

		if (dirPath == null)
			return null;
		File fpath = new File(dirPath);
		if (!fpath.exists()) {
			fpath.mkdirs();
		}
		if (name == null)
			return null;
		File file = new File(dirPath, name);
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	/*
	 * 设置文件长度
	 */
	public static void FileSetlength(File file, long filesize) {
		try {
			RandomAccessFile rafile = new RandomAccessFile(file, "rwd");
			rafile.setLength(filesize);
			rafile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 从输入流中获取数据
	 */
	public static byte[] InputStreamRead(InputStream inStream) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			inStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outStream.toByteArray();
	}

	/*
	 * 通过文件路径读取数据
	 */
	public static byte[] FileRead(String filepath) {

		byte[] buffer = null;
		try {
			FileInputStream mInputStream = new FileInputStream(filepath);
			int length = mInputStream.available();
			buffer = new byte[length];
			mInputStream.read(buffer);
			mInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}

	/*
	 * 通过文件路径写数据
	 */
	public static void FileWrite(String filepath, byte[] buf) {

		try {
			FileOutputStream mOutputStream = new FileOutputStream(filepath);
			mOutputStream.write(buf);
			mOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 文件删除
	 */
	public static void FileDelete(String dirPath, String name) {

		File file = new File(dirPath, name);
		if (file.exists()) {
			file.delete();
		}
	}

	/*
	 * 文件删除
	 */
	public static void FileDelete(String filePath) {

		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/*
	 * 文件是否存在
	 */
	public static boolean FileIsExist(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}

	/**
	 * 指定偏移文件读
	 * 
	 * @param filepath
	 *            --文件路径
	 * @param offset
	 *            ----文件偏移
	 * @param len
	 *            -------读取长度
	 * @return 返回null读取失败,否则返回实际读取数组
	 */
	public static byte[] RandomReadFile(String filepath, long offset, int len) {
		byte[] bytes = new byte[len];
		try {
			RandomAccessFile raf = new RandomAccessFile(filepath, "r");
			raf.seek(offset);
			if (raf.read(bytes, 0, len) != len) {
				bytes = null;
			}
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * 指定偏移文件写
	 * 
	 * @param filepath
	 *            --文件路径
	 * @param offset
	 *            ----文件内偏移
	 * @param bytes
	 *            -----写入字节数组
	 */
	public static void RandomWriteFile(String filepath, long offset,
			byte[] bytes) {

		try {
			RandomAccessFile raf = new RandomAccessFile(filepath, "rw");
			raf.seek(offset);
			raf.write(bytes, 0, bytes.length);
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 文件末尾追加写文件
	 * 
	 * @param fileName
	 *            --文件路径
	 * @param bytes
	 *            -----写入字节数组
	 */
	public static void appendWriteFile(String fileName, byte[] bytes) {

		if(bytes==null){
			try {
				RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
				long fileLength = raf.length();
				raf.seek(fileLength);
				raf.write(10);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
				long fileLength = raf.length();
				raf.seek(fileLength);
				
				raf.write(bytes);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 删除文件夹内全部文件 
	 * @author zgl
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFiles(File file) {

			File[] childFile = file.listFiles();
			for (File f : childFile) {
				f.delete();
			
		}
	}
	
	/**  
     * 复制单个文件  zgl2014-11-13 09:29:59添加
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径 
     * @return boolean  
     */   
	public static void copyFile(File sourceFile,File targetFile)   
			throws IOException{  
					if(!targetFile.exists()){
						targetFile.createNewFile();
					}
			        // 新建文件输入流并对它进行缓冲   
			        FileInputStream input = new FileInputStream(sourceFile);  
			        BufferedInputStream inBuff=new BufferedInputStream(input);  
			  
			        // 新建文件输出流并对它进行缓冲   
			        FileOutputStream output = new FileOutputStream(targetFile);  
			        BufferedOutputStream outBuff=new BufferedOutputStream(output);  
			          
			        // 缓冲数组   
			        byte[] b = new byte[1024 * 5];  
			        int len;  
			        while ((len =inBuff.read(b)) != -1) {  
			            outBuff.write(b, 0, len);  
			        }  
			        // 刷新此缓冲的输出流   
			        outBuff.flush();  
			          
			        //关闭流   
			        inBuff.close();  
			        outBuff.close();  
			        output.close();  
			        input.close();  
			    }  
	
	/***
	 * @author zgl
	 * 2014年12月31日14:31:44
	 */
    public File creatSDDir(String dirName) {  
        File dir = new File( dirName);  
        dir.mkdirs();  
        return dir;  
    } 
}
