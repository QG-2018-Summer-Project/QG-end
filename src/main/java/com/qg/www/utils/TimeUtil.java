package com.qg.www.utils;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author net
 * @version 1.0
 * 计算是否是同一天的类；
 */
@Service("timeUtil")
public class TimeUtil {
    public boolean isSameDay(String stringDate1, String stringDate2){
        String str=stringDate1.substring(0,10);
        String str1=stringDate2.substring(0,10);
        if (str.equals(str1)){
            return true;
        }else
        {
            return false;
        }

      /*  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            Date date1 = sdf.parse(stringDate1);
            Date date2 = sdf.parse(stringDate2);
            calendar1.setTime(date1);
            calendar2.setTime(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));*/
    }
}
