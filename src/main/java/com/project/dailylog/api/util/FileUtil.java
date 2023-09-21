package com.project.dailylog.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
  public static String generateFileName(String fileCategory){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String timestamp = sdf.format(new Date());
    String newFileName = fileCategory+"_"+timestamp;
    return newFileName;
  }
}
