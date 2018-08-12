package com.qg.www.service.impl;


import com.qg.www.dao.GpsDataDao;
import com.qg.www.models.GeoHash;
import com.qg.www.models.InteractionData;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import com.qg.www.utils.GeoHashUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    Point point;
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

        // 得到该矩阵区域的某段时间内个GeoHash方块中的权值
        /*  Map<String,Integer> points = */
        List<GeoHash> list = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data.getLeftTopLon(), data.getLeftTopLat(),
                data.getRightBottomLon(), data.getRightBottomLat(), data.getStartTime(), data.getEndTime());
        Iterator<GeoHash> iterator = list.iterator();
        List<Point> pointList = new LinkedList<>();
        while (iterator.hasNext()) {
            //创建新的对象；
            Point point = new Point();
            geoHash = iterator.next();
            System.out.println(geoHash.getGeohash()+"权重："+geoHash.getWeight());
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
