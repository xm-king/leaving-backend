package com.tiaoxi;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pingchun@meili-inc.com
 * @since 2019/4/2
 */
public class Utils {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateTimeFormatter FMT = DateTimeFormat.forPattern("MM月dd日");

    public static int getTimestamp(String time){
        try{
            Date date =  SDF.parse(time);
            return (int)(date.getTime()/1000);
        }catch (Exception e){
            return 0;
        }
    }

    public static int getCurrentTime(){
        return (int)(System.currentTimeMillis()/1000);
    }

   public static String getFormatTime(long timestamp){
       return FMT.print(timestamp*1000);
   }

   public static String getStatusDesc(int status){
       String statusDesc = "";
       switch (status){
           case 1:
               statusDesc = "已申请";
               break;
           case 2:
               statusDesc = "已确认";
               break;
           default:
               statusDesc = "";
       }
       return statusDesc;
   }
}
