package com.project.sokdak2.api.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
  public static String generateFileName(String fileCategory){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String timestamp = sdf.format(new Date());
    String newFileName = fileCategory+"_"+timestamp;
    return newFileName;
  }

  public static String getFileExt(String fileName){
    int fileExt = fileName.lastIndexOf(".");
    if(fileExt>1){
      return fileName.substring(fileExt+1);
    }
    return "";
  }

  public static String byteToKbytes(long fileSize) {
    double kilobytes = (double) fileSize / 1024;
    DecimalFormat df = new DecimalFormat("#.#");
    String formatter = df.format(kilobytes);
    return formatter;
  }
}
