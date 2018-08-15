package com.qg.www.service.impl;


import com.qg.www.dao.GpsDataDao;
import com.qg.www.dtos.ResponseData;
import com.qg.www.models.GeoHash;
import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import com.qg.www.utils.GeoHashUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private GeoHashUtil geoHashUtil;
    @Resource
    private ResponseData<Point> responseData;

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
        // 将时间设置为从当前时间到15秒前的这个时间段
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            calendar.setTime(sdf.parse(data.getCurrentTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        data.setEndTime(data.getCurrentTime());
        calendar.add(Calendar.SECOND, 0 - 14);
        data.setStartTime(sdf.format(calendar.getTime()));
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndLat(data);
        List<Point> pointList = geoHashUtil.decodeAll(list);
        responseData.setPointSet(pointList);
        return responseData;
    }

    /**
     * 预测某区域，某时间段的热力图；
     *
     * @param data 数据中包含两个点的经纬度和请求时间点；
     * @return 带权点集；
     */
    @Override
    public ResponseData predictMap(InteractionData data) {
        return null;
    }
}
