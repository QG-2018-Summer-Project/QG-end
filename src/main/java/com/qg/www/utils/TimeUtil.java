package com.qg.www.utils;

import org.junit.Test;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service("timeUtil")
public class TimeUtil {
    public boolean isSameDay(String stringDate1, String stringDate2){
        String Date1 = stringDate1.substring(0,10);
        String Date2 = stringDate2.substring(0,10);
        if (Date1.equals(Date1)){

        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        try {
            Date date1 = sdf.parse(stringDate1);
            Date date2 = sdf.parse(stringDate2);
            calendar1.setTime(date1);
            calendar2.setTime(date2);
        } catch (ParseException e) {
            e.printStackTrace();
            calendar1.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        }
        return (calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA)
                && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 得到数据库操作需要的数据表
     * @param stringDate1 起始时间
     * @param stringDate2 截止时间
     */
    public void getTable(String stringDate1, String stringDate2){
        //TODO 实现层判断相差是否一小时,是否在有效范围

        // 取得各自的年月日
        String date1 = stringDate1.substring(0,10);
        String date2 = stringDate2.substring(0,10);

        // 截取第一个日期的月份
        String monthOfDate1 = stringDate1.substring(5,7);
        // 截取第一个日期的天数
        String dayOfDate1 = stringDate1.substring(8,10);
        // 得到该日期与2017年
        int number = (Integer.parseInt(monthOfDate1) - 2) * 28 + Integer.parseInt(dayOfDate1);
        String[] table = new String[2];
        // 表名不同
        if(number >= 22){
            table[0] = "gpsdata_copy" + number;
        }else {
            table[0] = "gpsdata" + number;
        }
        if(!date1.equals(date2)){
            // 数据库下张表名不同
            if(number >= 21){
                table[1] = "gpsdata_copy" + (number + 1);
            }else {
                table[1] = "gpsdata" + (number + 1);
            }
        }
        System.out.println(table[0] + ":" + table[1]);
    }
}
