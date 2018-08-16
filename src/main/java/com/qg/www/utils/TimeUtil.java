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
     * @param startTime 起始时间
     * @param endTime 截止时间
     */
    public String[] getGpsDateTable(String startTime, String endTime) throws ParseException {
        //TODO 实现层判断相差是否一小时,是否在有效范围
        String[] table = new String[2];

        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 得到Camemdar单例
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar standardCal = Calendar.getInstance();

        // 得到对应的标准时间
        String strStandardDate = "2017-2-1 00:00:00";
        Date standardDate = sdf.parse(strStandardDate);
        standardCal.setTime(standardDate);

        // 得到对应的起始时间
        Date startDate = sdf.parse(startTime);
        startCal.setTime(startDate);

        // 得到对应的结束时间
        Date date2 = sdf.parse(endTime);
        endCal.setTime(date2);

        // 得到各自以一年算的天数
        int startDay = startCal.get(Calendar.DAY_OF_YEAR);
        int endDay = endCal.get(Calendar.DAY_OF_YEAR);
        int standardDay = standardCal.get(Calendar.DAY_OF_YEAR);

        // 得到各自所在的年份
        int startYear = startCal.get(Calendar.YEAR);
        int standardYear = standardCal.get(Calendar.YEAR);

        int resultTime;
        // 判断是不是同一年
        if (startYear != standardYear) {
            int timeDistance = 0;
            for (int i = standardYear; i < startYear; i++) {
                // 判断是不是闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            resultTime = timeDistance + (startDay - standardDay) + 1;
        } else {
            resultTime = startDay - standardDay + 1;
        }

        // 表名在22时有两种情况
        if (resultTime >= 22) {
            table[0] = "gpsdata_copy" + resultTime;
        } else {
            table[0] = "gpsdata" + resultTime;
        }

        // 判断两天的差是否等于1
        if ((endDay - startDay) == 1) {
            // 数据库下张表名不同
            if (resultTime >= 21) {
                table[1] = "gpsdata_copy" + (resultTime + 1);
            } else {
                table[1] = "gpsdata" + (resultTime + 1);
            }
        }
        return table;
    }

    /**
     * 根据预测所需日期得到相应的数据库表
     *
     * @param strDate    传入的预测所需日期
     * @param firstTable 所需数据表的类型
     * @return 该日期对应的数据库表
     * @throws ParseException
     */
    public String getDemandTable(String strDate, String firstTable) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = sdf.parse(strDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(date);

        String strStandardDate = "2017-2-1 00:00:00";
        Date standardDate = sdf.parse(strStandardDate);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(standardDate);
        // 得到各自以一年算的天数
        int startDay = startCal.get(Calendar.DAY_OF_YEAR);
        int endDay = endCal.get(Calendar.DAY_OF_YEAR);

        // 得到各自所在的年份
        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);
        // 判断是不是同一年
        if (startYear != endYear) {
            int timeDistance = 0;
            for (int i = startYear; i < endYear; i++) {
                // 判断是不是闰年
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    timeDistance += 366;
                } else {
                    timeDistance += 365;
                }
            }
            return firstTable + (timeDistance + (endDay - startDay) + 1);
        } else {
            return firstTable + (endDay - startDay + 1);
        }
    }
}
