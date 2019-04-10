package com.myproject.core.utils;

import java.io.*;

public class FileOperation {
  /**
   *  生成文件
   *  @param fileName
   */
  public static boolean createFile(File fileName){
    if(!fileName.exists()){  
      try {
        fileName.createNewFile();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return true;
  }
  
  /**
   * @description 读文件
   * @throws IOException 
   */
  public static String readTxtFile(File fileName) throws IOException{
    String result = "";
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    fileReader = new FileReader(fileName);
    bufferedReader = new BufferedReader(fileReader);
    
    String line = "";
    while((line = bufferedReader.readLine()) != null){
      result = result + line + "\r\n";
    }
    
    if(bufferedReader != null){
      bufferedReader.close();
    }
    
    if(fileReader != null){
        fileReader.close();
    }
    return result;
  }
  
  /**
   * @description 写文件
   * @param args
   * @throws UnsupportedEncodingException 
   * @throws IOException
   */
  public static boolean writeTxtFile(String content,File fileName) throws UnsupportedEncodingException, IOException{
    FileOutputStream o = null;
    o = new FileOutputStream(fileName);
    o.write(content.getBytes("UTF-8"));
    o.close();
    return true; 
  }
}