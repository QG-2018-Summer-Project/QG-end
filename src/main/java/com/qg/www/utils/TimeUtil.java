package com.qg.www.utils;

import org.junit.Test;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author net
 * @version 1.0
 * 时间处理工具类；
 */
@Service("timeUtil")
public class TimeUtil {
    /**
     * 得到数据库操作需要的数据表
     *
     * @param stringDate1 起始时间
     * @param stringDate2 截止时间
     */
    public String[] getGpsDateTable(String stringDate1, String stringDate2) throws ParseException {
        //TODO 实现层判断相差是否一小时,是否在有效范围
        String[] table = new String[2];

        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 得到Camemdar单例
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();

        // 得到对应的标准时间
        String strStandardDate = "2017-2-1 00:00:00";
        Date date3 = sdf.parse(strStandardDate);
        calendar3.setTime(date3);

        // 得到对应的起始时间
        Date date1 = sdf.parse(stringDate1);
        calendar1.setTime(date1);

        // 得到对应的结束时间
        Date date2 = sdf.parse(stringDate2);
        calendar2.setTime(date2);

        // 得到各自以一年算的天数
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int day3 = calendar3.get(Calendar.DAY_OF_YEAR);

        // 得到各自所在的年份
        int year1 = calendar1.get(Calendar.YEAR);
        int year3 = calendar3.get(Calendar.YEAR);

        int resultTime;
        // 判断是不是同一年
        if (year1 != year3) {
            int timeDistance = 0;
            for (int i = year3; i < year1; i++) {
                // 判断是不是闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            resultTime = timeDistance + (day1 - day3) + 1;
        } else {
            resultTime =  day1 - day3 + 1;
        }

        // 表名在22时有两种情况
        if (resultTime >= 22) {
            table[0] = "gpsdata_copy" + resultTime;
        } else {
            table[0] = "gpsdata" + resultTime;
        }

        if ((day2 - day1) == 1) {
            // 数据库下张表名不同
            if (resultTime >= 21) {
                table[1] = "gpsdata_copy" + (resultTime + 1);
            } else {
                table[1] = "gpsdata" + (resultTime + 1);
            }
        }
        System.out.println(table[1]);
        return table;
    }

    /**
     * 根据预测所需日期得到相应的数据库表
     * @param strDate 传入的预测所需日期
     * @param firstTable 所需数据表的类型
     * @return 该日期对应的数据库表
     * @throws ParseException
     */
    public String getDemandTable(String strDate, String firstTable) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = sdf.parse(strDate);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);

        String strStandardDate = "2017-2-1 00:00:00";
        Date standardDate = sdf.parse(strStandardDate);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(standardDate);
        // 得到各自以一年算的天数
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        // 得到各自所在的年份
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        // 判断是不是同一年
        if (year1 != year2) {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                // 判断是不是闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return firstTable + (timeDistance + (day2 - day1) + 1);
        } else {
            return firstTable + (day2 - day1 + 1);
        }
    }
}
