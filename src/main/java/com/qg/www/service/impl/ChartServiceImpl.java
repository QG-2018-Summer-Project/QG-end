package com.qg.www.service.impl;

import com.qg.www.dao.FeatureDao;
import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Url;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Point;
import com.qg.www.models.Rate;
import com.qg.www.service.ChartService;
import com.qg.www.utils.GeoHashUtil;
import com.qg.www.utils.HttpClientUtil;
import com.qg.www.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service("chartService")
public class ChartServiceImpl implements ChartService {
    @Resource
    private TimeUtil timeUtil;
    @Resource
    private FeatureDao featureDao;
    @Resource
    private InteractBigData bigData;
    @Resource
    private HttpClientUtil httpClientUtil;
    @Resource
    private ResponseData responseData;
    @Resource
    private List<Point> pointList;
    @Resource
    private List<GeoHash> geoHashList;


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
        Integer month = Integer.parseInt(data.getCurrentTime().substring(5, 7));
        Integer day = Integer.parseInt(data.getCurrentTime().substring(8, 10));
        Integer hour = Integer.parseInt(data.getCurrentTime().substring(11, 13));
        // 得到表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table, data, hour);
        // 将各参数放入交互model中
        RequestData<Feature> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(featureList);
        // 得到数据挖掘端传来的数据
        try {
            bigData = HttpClientUtil.demandedCount(Url.CHANGE_PERCENT.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        geoHashList = bigData.getPointSet();
        Double[] percents = new Double[6];

        if (hour < 3) {
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
        } else if (hour > 21) {
            //TODO 无法查询
        } else {
            // 得到3小时前，2小时前，1小时前的时间段
            Integer hour1 = hour - 3;
            Integer hour2 = hour - 2;
            Integer hour3 = hour - 1;
            // 查询数据库得到各自变化率的平均值
            percents[0] = featureDao.getAvgPercent(data, table, hour1);
            percents[1] = featureDao.getAvgPercent(data, table, hour2);
            percents[2] = featureDao.getAvgPercent(data, table, hour3);
        }
        // 遍历得到数据挖掘端传过来的值的平均值
        int i = 0;
        double weight1 = 0;
        double weight2 = 0;
        double weight3 = 0;
        for (GeoHash geoHash : geoHashList) {
            weight1 += geoHash.getWeight1();
            weight2 += geoHash.getWeight2();
            weight3 += geoHash.getWeight3();
            i++;
        }
        percents[3] = weight1 / i;
        percents[4] = weight2 / i;
        percents[5] = weight3 / i;
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
        // 截取预测时间的日期天数和时间
        Integer month = Integer.parseInt(data.getCurrentTime().substring(5, 7));
        Integer day = Integer.parseInt(data.getCurrentTime().substring(8, 10));
        Integer hour = Integer.parseInt(data.getCurrentTime().substring(11, 13));
        // 得到表中的所有信息
        List<Rate> rateList = featureDao.listAllRate("rate", data, hour);
        // 将各参数放入交互model中
        RequestData<Rate> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(rateList);
        // 得到数据挖掘端传来的数据
        try {
            bigData = HttpClientUtil.demandedCount(Url.UTILIZE_PERCENT.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        geoHashList = bigData.getPointSet();
        Double[] percents = new Double[6];

        if (hour < 3) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                calendar.setTime(sdf.parse(data.getCurrentTime()));
                // 提前一小时
                calendar.add(Calendar.HOUR, -1);
                percents[2] = featureDao.getAvgRate(data,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR));
                // 提前两小时
                calendar.add(Calendar.HOUR, -1);
                percents[1] = featureDao.getAvgRate(data,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR));
                // 提前三小时
                calendar.add(Calendar.HOUR, -1);
                percents[0] = featureDao.getAvgRate(data,calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (hour > 21) {
            //TODO 无法查询
        } else {
            // 得到3小时前，2小时前，1小时前的时间段
            Integer hour1 = hour - 3;
            Integer hour2 = hour - 2;
            Integer hour3 = hour - 1;
            // 查询数据库得到各自变化率的平均值
            percents[0] = featureDao.getAvgRate(data,month, day, hour1);
            percents[1] = featureDao.getAvgRate(data,month, day, hour2);
            percents[2] = featureDao.getAvgRate(data,month, day, hour3);
        }
        // 遍历得到数据挖掘端传过来的值的平均值
        int i = 0;
        double weight1 = 0;
        double weight2 = 0;
        double weight3 = 0;
        for (GeoHash geoHash : geoHashList) {
            weight1 += geoHash.getWeight1();
            weight2 += geoHash.getWeight2();
            weight3 += geoHash.getWeight3();
            i++;
        }
        percents[3] = weight1 / i;
        percents[4] = weight2 / i;
        percents[5] = weight3 / i;
        responseData.setPercents(percents);
        return responseData;
    }

}
