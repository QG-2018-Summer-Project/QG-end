package com.qg.www.service.impl;


import com.qg.www.dao.FeatureDao;
import com.qg.www.dao.GpsDataDao;
import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Url;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
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

/**
 * @author net
 * @version 1.0
 * 热力图业务实现类
 */
@Service("heatMapService")
public class HeatMapServiceImpl implements HeatMapService {
    @Resource
    GpsDataDao gpsDataDao;
    @Resource
    FeatureDao featureDao;
    @Resource
    GeoHashUtil geoHashUtil;
    @Resource
    TimeUtil timeUtil;
    @Resource
    HttpClientUtil httpClientUtil;
    @Resource
    ResponseData<Point> responseData;
    @Resource
    InteractBigData bigData;

    /**
     * 查询某时间段的热力图
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public ResponseData querySomeTimesMap(InteractionData data) {
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data);
        List<Point> pointList = geoHashUtil.decodeAll(list);
        responseData.setPointSet(pointList);
        return responseData;
    }

    @Override
    public ResponseData getLiveMap(InteractionData data) {
        // 将时间设置为从当前时间到14秒前的这个时间段
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            calendar.setTime(sdf.parse(data.getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.setEndTime(data.getCurrentTime());
        calendar.add(Calendar.SECOND, -14);
        data.setStartTime(sdf.format(calendar.getTime()));
        System.out.println(data.getStartTime() + ":" + data.getEndTime());

        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data);
        List<Point> pointList = geoHashUtil.decodeAll(list);
        responseData.setPointSet(pointList);
        return responseData;
    }

    @Override
    public ResponseData getDemandMap(InteractionData data) {
        String table = "";
        try {
            // 得到应该查询的数据表
            table = timeUtil.getDemandTable(data.getPredictedTime(), "need");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 截取预测时间的日期天数和时间
        Integer month = Integer.parseInt(data.getPredictedTime().substring(5, 7));
        Integer day = Integer.parseInt(data.getPredictedTime().substring(8, 10));
        Integer hour = Integer.parseInt(data.getPredictedTime().substring(11, 13));
        // 得到表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table,data,hour);
        // 将各参数放入交互model中
        RequestData<Feature> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(featureList);
        try {
            bigData = httpClientUtil.demandedCount(Url.DEMAND.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<GeoHash> geoHashList = bigData.getPointSet();
        List<Point> pointList = geoHashUtil.decodeAll(geoHashList);
        responseData.setPointSet(pointList);
        return responseData;
    }

    /**
     * 获取预测汽车数量热力图；
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public ResponseData getPredictCarMap(InteractionData data) {
        String table = "";
        try {
            // 得到应该查询的数据表
            table = timeUtil.getDemandTable(data.getPredictedTime(), "data");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 截取预测时间的日期天数和时间
        Integer month = Integer.parseInt(data.getPredictedTime().substring(5, 7));
        Integer day = Integer.parseInt(data.getPredictedTime().substring(8, 10));
        Integer hour = Integer.parseInt(data.getPredictedTime().substring(11, 13));
        // 得到表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table,data,hour);
        // 将各参数放入交互model中
        RequestData<Feature> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(featureList);

        System.out.println(featureList.get(1).getDay1());
        try {
            bigData = httpClientUtil.demandedCount(Url.COUNT.getUrl(), requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<GeoHash> geoHashList = bigData.getPointSet();
        List<Point> pointList = geoHashUtil.decodeAll(geoHashList);
        responseData.setPointSet(pointList);
        return responseData;
    }
}
