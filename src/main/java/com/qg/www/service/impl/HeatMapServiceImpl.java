/*
package com.qg.www.service.impl;



import com.qg.www.dao.GpsDataDao;
import com.qg.www.models.InteractionData;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import com.qg.www.utils.GeoHashUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

*/
/**
 * @author net
 * @version 1.0
 * 热力图业务实现类
 *//*

@Service("heatMapService")
public class HeatMapServiceImpl implements HeatMapService  {
    @Resource
    GpsDataDao gpsDataDao;
    @Resource
    GeoHashUtil geoHashUtil;
    @Resource
    InteractionData interactionData;
    @Resource
    Point point;

    */
/**
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     *//*

    @Override
    public InteractionData querySomeTimesMap(InteractionData data) {

        */
/*//*
/ 将时间设置为从当前时间到15分钟前的这个时间段
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        calendar.setTime(data.getCurrentTime());
        data.setStartTime(data.getCurrentTime());
        calendar.add(Calendar.HOUR,0-0-15);
        data.setStartTime(calendar.getTime());*//*


        // 得到该矩阵区域的某段时间内个GeoHash方块中的权值
        Map<String,Integer> points = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data.getLeftTopLon(),data.getLeftTopLat(),
                data.getRightBottomLon(),data.getRightBottomLat(),data.getStartTime(),data.getEndTime());

        List<Point> list = new ArrayList<>();
        // 模拟将GeoHash的值转化为经纬度
        Set entries = points.entrySet( );
        if(entries != null) {
            Iterator iterator = entries.iterator( );
            while(iterator.hasNext( )) {
                Map.Entry entry = (Map.Entry) iterator.next();
                // GeoHash解码
                double[] lonAndLat = geoHashUtil.decode((String) entry.getKey());
                // 设置经纬度和权重
                point.setLon(lonAndLat[0]);
                point.setLat(lonAndLat[1]);
                point.setWeight((Integer) entry.getValue());
                // 加入集合
                list.add(point);
            }
        }
        interactionData.setPointSet(list);

        return interactionData;

    }

    @Override
    public InteractionData getLiveMap(InteractionData data) {
        Map<String,Integer> points = gpsDataDao.listGeoHashAndNumByTimeAndLonAndBat(data.getLeftTopLon(),data.getLeftTopLat(),
                data.getRightBottomLon(),data.getRightBottomLat(),data.getStartTime(),data.getEndTime());

        interactionData.setPointSet(geoHashUtil.decodeAll(points));
        return interactionData;
    }
}
*/
