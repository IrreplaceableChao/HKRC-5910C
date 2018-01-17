package com.android.utils;
// 生成Excel的类 
 import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import android.os.Environment;
import android.util.Log;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

 public   class  CreateExcel  {
	 String sdfilepath = Environment.getExternalStorageDirectory().getPath() + "//HKExcel//"; 
	 FileUtils f = new FileUtils();
	 
	 public void setSdFilePath(String s){
		 this.sdfilepath = s;
	 }
	 
     public   static   void  putong()  {
    	 String filename = "c:\\test2.xls";
    	 
    	 WritableWorkbook wwb = null;    
         try {    
             //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象    
             wwb = Workbook.createWorkbook(new File(filename));    
         } catch (IOException e) {    
             e.printStackTrace();    
         }    
         if(wwb!=null){    
             //创建一个可写入的工作表    
             //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置    
             WritableSheet ws = wwb.createSheet("sheet1", 0);    
                 
             //下面开始添加单元格    
             for(int i=0;i<10;i++){    
                 for(int j=0;j<5;j++){    
                     //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行    
                     Label labelC = new Label(j, i, "这是第"+(i+1)+"行，第"+(j+1)+"列");    
                     try {    
                         //将生成的单元格添加到工作表中    
                         ws.addCell(labelC);    
                     } catch (RowsExceededException e) {    
                         e.printStackTrace();    
                     } catch (WriteException e) {    
                         e.printStackTrace();    
                     }    
                 }    
             }    
             try {    
                 //从内存中写入文件中    
                 wwb.write();    
                 //关闭资源，释放内存    
                 wwb.close();    
             } catch (IOException e) {    
                 e.printStackTrace();    
             } catch (WriteException e) {    
                 e.printStackTrace();    
             }    
         }
    } 
     
     public void pullArray(String pathname,String filename,String[] head,List<Map<String, String>> listdata){
    	 WritableWorkbook wwb = null;
    	 if(!new File(pathname).exists()){
    		 Log.d("ce",pathname);
    		 f.creatSDDir(pathname);
    	 }
    	 
         try {    
             //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象    
             wwb = Workbook.createWorkbook(new File(filename+".xls"));    
         } catch (IOException e) {    
             e.printStackTrace();    
         }    
         if(wwb!=null){    
             //创建一个可写入的工作表    
             //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置    
             WritableSheet ws = wwb.createSheet("sheet1", 0);  
             //添加列头
             for(int i = 0; i<head.length;i++){
                 Label labelC = new Label(i, 0, head[i]);    
                 try {    
                     //将生成的单元格添加到工作表中    
                     ws.addCell(labelC);    
                 } catch (RowsExceededException e) {    
                     e.printStackTrace();    
                 } catch (WriteException e) {    
                     e.printStackTrace();    
                 } 
             }
             //添加单元格    
             for(int i=0;i<listdata.size();i++){    
                 for(int j=0;j<head.length;j++){    
                     //这里需要注意的是，在Excel中，第一个参数表示列，第二个表示行    
                     Label labelC = new Label(j, i+1, listdata.get(i).get(head[j]));    
                     try {    
                         //将生成的单元格添加到工作表中    
                         ws.addCell(labelC);    
                     } catch (RowsExceededException e) {    
                         e.printStackTrace();    
                     } catch (WriteException e) {    
                         e.printStackTrace();    
                     }    
                 }    
             }    
             try {    
                 //从内存中写入文件中    
                 wwb.write();    
                 //关闭资源，释放内存    
                 wwb.close();    
             } catch (IOException e) {    
                 e.printStackTrace();    
             } catch (WriteException e) {    
                 e.printStackTrace();    
             }    
         }
     }
} 