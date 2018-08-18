package com.qg.www.controllers;

import com.qg.www.dao.GpsDataDao;
import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
import com.qg.www.utils.GeoHashUtil;
import org.apache.http.protocol.ResponseDate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * @author net
 * @version 1.3
 */
@RestController
@CrossOrigin
@RequestMapping("/roadandcar")
public class RoadAndCarController {
    @Resource
    GpsDataDao gpsDataDao;
    @Resource
    Feature feature;
    @Resource
    GeoHashUtil geoHashUtil;
    /**
     * 得到最优路径
     * @param data 由前端提供的多条路径信息
     * @return 最优路径
     */
    @PostMapping("/querybestway")
    public ResponseDate getBestWay(){
        selectAll();
        return null;
    }
    public void selectAll() {
        List<Feature> list= gpsDataDao.listAllFeature();
        Iterator<Feature> iterator=list.iterator();
        while(iterator.hasNext()){
            feature=iterator.next();
            double[] arr=geoHashUtil.decode(feature.getGeohash());
            feature.setLat(arr[0]);
            feature.setLon(arr[1]);
            gpsDataDao.addFeature(feature);
        }

    }

}
