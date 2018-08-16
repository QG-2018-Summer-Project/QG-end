package com.qg.www.service.impl;

import com.qg.www.dao.FeatureDao;
import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Url;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Rate;
import com.qg.www.service.ChartService;
import com.qg.www.utils.HttpClientUtil;
import com.qg.www.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author net
 * @version 1.3
 * 预测未来数据图表实现类
 */
@Service("chartService")
public class ChartServiceImpl implements ChartService {
    @Resource
    private TimeUtil timeUtil;
    @Resource
    private FeatureDao featureDao;
    @Resource
    private InteractBigData bigData;
    @Resource
    private ResponseData responseData;
    @Resource
    private List<GeoHash> geoHashList;
    @Resource
    private RequestData requestData;


    /**
     * 得到未来的变化率
     *
     * @param data 经纬度范围和预测时间
     * @return 6段变化率
     */
    @Override
    public ResponseData getChangePercent(InteractionData data) {
        String table = "";
        try {
            // 得到应该查询的数据表
            table = timeUtil.getDemandTable(data.getCurrentTime(), "percent");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 截取预测时间的日期天数和时间
        Integer currentMonth = Integer.parseInt(data.getCurrentTime().substring(5, 7));
        Integer currentDay = Integer.parseInt(data.getCurrentTime().substring(8, 10));
        Integer currentHour = Integer.parseInt(data.getCurrentTime().substring(11, 13));
        // 得到表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table, data, currentHour);
        // 将各参数放入交互model中
        requestData = new RequestData<>();
        requestData.setDay1(currentDay);
        requestData.setHour1(currentHour);
        requestData.setMonth1(currentMonth);
        requestData.setList(featureList);
        // 得到数据挖掘端预测的当前时段与未来一、二小时的流量变化率
        try {
            bigData = HttpClientUtil.demandedCount(Url.CHANGE_PERCENT.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        geoHashList = bigData.getPointSet();
        Double[] percents = new Double[6];

        if (currentHour < 3) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                calendar.setTime(sdf.parse(data.getCurrentTime()));
                // 提前一小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "percent");
                percents[2] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR));
                // 提前两小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "percent");
                percents[1] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR));
                // 提前三小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "percent");
                percents[0] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (currentHour > 21) {
            //TODO 无法查询
        } else {
            // 查询数据库得到前三、二、一小时的出租车数量变化率的平均值
            percents[0] = featureDao.getAvgPercent(data, table, currentHour - 3);
            percents[1] = featureDao.getAvgPercent(data, table, currentHour - 2);
            percents[2] = featureDao.getAvgPercent(data, table, currentHour - 1);
        }
        // 遍历得到数据挖掘端传过来的值的平均值
        int i = 0;
        double currentPercent = 0;
        double oneLatePercent = 0;
        double twoLatePercent = 0;
        for (GeoHash geoHash : geoHashList) {
            currentPercent += geoHash.getWeight1();
            oneLatePercent += geoHash.getWeight2();
            twoLatePercent += geoHash.getWeight3();
            i++;
        }
        percents[3] = currentPercent / i;
        percents[4] = oneLatePercent / i;
        percents[5] = twoLatePercent / i;
        responseData.setPercents(percents);
        return responseData;
    }

    /**
     * 得到过去和未来车辆的利用率
     *
     * @param data 经纬度范围和预测时间
     * @return 6段变化率
     */
    @Override
    public ResponseData getUtilizePercent(InteractionData data) {
        String table = "";
        try {
            // 得到应该查询的数据表
            table = timeUtil.getDemandTable(data.getCurrentTime(), "rate");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 截取预测时间的日期天数和时间
        Integer currentMonth = Integer.parseInt(data.getCurrentTime().substring(5, 7));
        Integer currentDay = Integer.parseInt(data.getCurrentTime().substring(8, 10));
        Integer currentHour = Integer.parseInt(data.getCurrentTime().substring(11, 13));
        // 得到表中的所有信息
        List<Rate> rateList = featureDao.listAllRate(table, data, currentHour);
        // 将各参数放入交互model中
        requestData = new RequestData<>();
        requestData.setDay1(currentDay);
        requestData.setHour1(currentHour);
        requestData.setMonth1(currentMonth);
        requestData.setList(rateList);
        // 得到数据挖掘端预测的当前时段与未来一、二小时的出租车平均利用率
        try {
            bigData = HttpClientUtil.demandedCount(Url.UTILIZE_PERCENT.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        geoHashList = bigData.getPointSet();
        Double[] percents = new Double[6];

        if (currentHour < 3) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                calendar.setTime(sdf.parse(data.getCurrentTime()));
                // 提前一小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "rate");
                percents[2] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR));
                // 提前两小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "rate");
                percents[1] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR));
                // 提前三小时
                calendar.add(Calendar.HOUR, -1);
                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), "rate");
                percents[0] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (currentHour > 21) {
            //TODO 无法查询
        } else {
            // 查询数据库得到前三、二、一小时的出租车数量变化率的平均值
            percents[0] = featureDao.getAvgRate(data, table, currentHour - 3);
            percents[1] = featureDao.getAvgRate(data, table, currentHour - 2);
            percents[2] = featureDao.getAvgRate(data, table, currentHour - 1);
        }
        // 遍历得到数据挖掘端传过来的值的平均值
        int i = 0;
        double currentPercent = 0;
        double oneLatePercent = 0;
        double twoLatePercent = 0;
        for (GeoHash geoHash : geoHashList) {
            currentPercent += geoHash.getWeight1();
            oneLatePercent += geoHash.getWeight2();
            twoLatePercent += geoHash.getWeight3();
            i++;
        }
        percents[3] = currentPercent / i;
        percents[4] = oneLatePercent / i;
        percents[5] = twoLatePercent / i;
        responseData.setPercents(percents);
        return responseData;
    }

}
