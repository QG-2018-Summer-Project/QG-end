package com.qg.www.service.impl;


import com.qg.www.dao.GpsDataDao;
import com.qg.www.models.GeoHash;
import com.qg.www.models.InteractionData;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import com.qg.www.utils.GeoHashUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
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
    GeoHashUtil geoHashUtil;
    @Resource
    InteractionData interactionData;
    @Resource
    GeoHash geoHash;


    /**
     * 查询某时间段的热力图
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public InteractionData querySomeTimesMap(InteractionData data) {
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data.getLeftTopLon(), data.getLeftTopLat(),
                data.getRightBottomLon(), data.getRightBottomLat(), data.getStartTime(), data.getEndTime());
        Iterator<GeoHash> iterator = list.iterator();
        List<Point> pointList = new LinkedList<>();
        while (iterator.hasNext()) {
            geoHash = iterator.next();
            //创建新的对象；
            Point point = new Point();
            System.out.println(geoHash.getGeohash() + "权重：" + geoHash.getWeight());
            //获取经纬度；
            double[] lonAndLat = geoHashUtil.decode(geoHash.getGeohash());
            point.setLon(lonAndLat[1]);
            point.setLat(lonAndLat[0]);
            point.setWeight(geoHash.getWeight());
            //加入列表；
            pointList.add(point);
        }
        interactionData.setPointSet(pointList);
        return interactionData;
    }

    /**
     * 查询实况热力图；
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public InteractionData queryCurrentMap(InteractionData data) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(sdf.parse(data.getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.setEndTime(data.getCurrentTime());
        calendar.add(Calendar.SECOND,0-14);
        data.setStartTime(sdf.format(calendar.getTime()));
        System.out.println(data.getStartTime()+"::"+data.getEndTime());
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data.getLeftTopLon(), data.getLeftTopLat(),
                data.getRightBottomLon(), data.getRightBottomLat(), data.getStartTime(),data.getEndTime());
        Iterator<GeoHash> iterator = list.iterator();
        List<Point> pointList = new LinkedList<>();
        while (iterator.hasNext()) {
            //创建新的对象；
            Point point = new Point();
            geoHash = iterator.next();
            //获取经纬度；
            double[] lonAndLat = geoHashUtil.decode(geoHash.getGeohash());
            point.setLon(lonAndLat[1]);
            point.setLat(lonAndLat[0]);
            point.setWeight(geoHash.getWeight());
            //加入列表；
            pointList.add(point);
        }
        interactionData.setPointSet(pointList);
        return interactionData;
    }
}
