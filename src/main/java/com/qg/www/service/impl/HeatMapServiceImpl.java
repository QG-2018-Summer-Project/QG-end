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

    /**
     * 查询某时间段的热力图
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    @Override
    public InteractionData querySomeTimesMap(InteractionData data) {

        // 得到该矩阵区域的某段时间内个GeoHash方块中的权值
        /*  Map<String,Integer> points = */
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data);
        List<Point> pointList = geoHashUtil.decodeAll(list);
        interactionData.setPointSet(pointList);
        return interactionData;
    }

    @Override
    public InteractionData getLiveMap(InteractionData data) {
        // 将时间设置为从当前时间到15秒前的这个时间段
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            calendar.setTime(sdf.parse(data.getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.setEndTime(data.getCurrentTime());
        calendar.add(Calendar.SECOND,-15);
        data.setStartTime(sdf.format(calendar.getTime()));
        System.out.println(data.getStartTime() + ":" + data.getEndTime());

        // 得到该矩阵区域的某段时间内个GeoHash方块中的权值
        /*  Map<String,Integer> points = */
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data);
        List<Point> pointList = geoHashUtil.decodeAll(list);
        interactionData.setPointSet(pointList);
        return interactionData;
    }
}
