package com.SearchEngine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	private static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"; 
	public static int getTimeDelta(Date date1,Date date2){  
        long timeDelta=(date1.getTime()-date2.getTime())/1000;//µ¥Î»ÊÇÃë  
        int secondsDelta=timeDelta>0?(int)timeDelta:(int)Math.abs(timeDelta);  
        return secondsDelta;  
    }
	public static int getTimeDelta(String dateStr1,String dateStr2){  
        Date date1=parseDateByPattern(dateStr1, yyyyMMddHHmmss);  
        Date date2=parseDateByPattern(dateStr2, yyyyMMddHHmmss);  
        return getTimeDelta(date1, date2);  
    }
	public static Date parseDateByPattern(String dateStr,String dateFormat){  
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);  
        try {  
            return sdf.parse(dateStr);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
}
