package com.qg.www.service.impl;


import com.qg.www.dao.FeatureDao;
import com.qg.www.dao.GpsDataDao;
import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Status;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import com.qg.www.utils.GeoHashUtil;
import com.qg.www.utils.HttpClient;
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
    private GpsDataDao gpsDataDao;
    @Resource
    private FeatureDao featureDao;
    @Resource
    private GeoHashUtil geoHashUtil;
    @Resource
    private TimeUtil timeUtil;
    @Resource
    private HttpClient httpClient;
    @Resource
    private ResponseData<Point> responseData;
    @Resource
    private InteractBigData bigData;

    /**
     * 查询某时间段的热力图
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public ResponseData querySomeTimesMap(InteractionData data) {
        //定义第一张表名；
        String tableOne ;
        //定义第二张表名；
        String tableTwo ;
        //定义开始时间；
        String startTime;
        //定义结束时间；
        String endTime;
        //定义列表；
        List<GeoHash> list;
        //点集；
        List<Point> pointList;
        startTime = data.getStartTime();
        endTime = data.getEndTime();
        //如果时间不为空，则可以调用dao层进行查询；
        if (null != startTime && null != endTime) {
            String[]tables=new String[2];
            try {
                //接收数据；
                tables=timeUtil.getGpsDateTable(startTime,endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //先判断表名；如果不是跨天查询，执行下列操作；
            if (null!=tables[1]&&"".equals(tables[1])){
                tableOne=tables[0];
                System.out.println(tableOne);
                list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data,tableOne,null);
                //判空；
                if (null!=list) {
                    pointList = geoHashUtil.decodeAll(list);
                    responseData.setPointSet(pointList);
                }else {
                    //查询结果为空；
                    responseData.setPointSet(null);
                }
                responseData.setStatus(Status.NORMAL.getStatus());
            }else{
                //如果是跨天查询，执行分表查询；
                tableOne=tables[0];
                tableTwo=tables[1];
                list=gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data,tableOne,tableTwo);
                //判空；
                if (null!=list) {
                    pointList = geoHashUtil.decodeAll(list);
                    responseData.setPointSet(pointList);
                }else {
                    //查询结果为空；
                    responseData.setPointSet(null);
                }
                responseData.setStatus(Status.NORMAL.getStatus());
            }

            return responseData;
        }
        responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
        return responseData;
    }

    /**
     * 获取实况热力图；
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public ResponseData getLiveMap(InteractionData data) {
        //定义第一张表名；
        String tableOne ;
        //定义第二张表名；
        String tableTwo ;
        //定义当前时间变量；
        String currentTime;
        //定义geohash队列
        List<GeoHash> list;
        //定义点集；
        List<Point> pointList;
        //创建表数组；
        String []tables=new String[2];
        // 将时间设置为从当前时间到15秒前的这个时间段
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime = data.getCurrentTime();
        //判空验证；
        if (null != currentTime && null != sdf) {
            try {
                calendar.setTime(sdf.parse(data.getCurrentTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            //当前时间为空，或者查询出错；
            responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
            return responseData;
        }
        data.setEndTime(currentTime);
        //时间前移14秒，避免因为请求的跨度造成的精度损失；
        calendar.add(Calendar.SECOND, -14);
        data.setStartTime(sdf.format(calendar.getTime()));
        //获取表名数组；
        try {
            tables=timeUtil.getGpsDateTable(data.getStartTime(),data.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //不是跨天；
        if("".equals(tables[1])){
            tableOne=tables[0];
            //获取数据库查询数据队列；
            list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data,tableOne,null);
        }else {
            tableOne=tables[0];
            tableTwo=tables[1];
            //获取数据库查询数据队列；
            list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data,tableOne,tableTwo);
        }

        //查询成功,且数据不为空；
        if (!list.isEmpty()) {
            pointList = geoHashUtil.decodeAll(list);
            responseData.setPointSet(pointList);
            responseData.setStatus(Status.NORMAL.getStatus());
        }else {
            //查询成功，但是数据为空；
            responseData.setStatus(Status.NORMAL.getStatus());
        }
        return responseData;
    }

    @Override
    public ResponseData getDemandMap(InteractionData data) {
        String table = "";
        //定义月份；
        Integer month;
        //定义日变量；
        Integer day;
        //定义小时变量；
        Integer hour;
        try {
            // 得到应该查询的数据表名；
            table = timeUtil.getDemandTable(data.getPredictedTime(), "need");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 截取预测时间的日期天数和时间
        month = Integer.parseInt(data.getPredictedTime().substring(5, 7));
        day = Integer.parseInt(data.getPredictedTime().substring(8, 10));
        hour = Integer.parseInt(data.getPredictedTime().substring(11, 13));
        // 得到表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table, data, hour);
        // 将各参数放入交互model中
        RequestData<Feature> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(featureList);
        try {
            bigData = httpClient.demandedCount("http://192.168.1.110:5000/qgtaxi/predict/xuqiuliang", requestData);
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
        // 得到特征表中的所有信息
        List<Feature> featureList = featureDao.listAllFeature(table, data, hour);
        // 将各参数放入交互model中
        RequestData<Feature> requestData = new RequestData<>();
        requestData.setDay1(day);
        requestData.setHour1(hour);
        requestData.setMonth1(month);
        requestData.setList(featureList);
        try {
            bigData = httpClient.demandedCount("http://192.168.1.110:5000/qgtaxi/predict/count", requestData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<GeoHash> geoHashList = bigData.getPointSet();
        List<Point> pointList = geoHashUtil.decodeAll(geoHashList);
        responseData.setPointSet(pointList);
        return responseData;
    }
}
