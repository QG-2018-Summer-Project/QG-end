package com.qg.www.controllers;

import com.qg.www.dao.GpsDataDao;
import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
import com.qg.www.models.Rate;
import com.qg.www.service.impl.HeatMapServiceImpl;
import com.qg.www.utils.GeoHashUtil;
import org.apache.http.protocol.ResponseDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;


/**
 * @author net
 * @version 1.0
 * 地图展示控制器
 */
@Controller
@CrossOrigin
@RequestMapping("/maps")
public class MapsController {

    @Resource
    HeatMapServiceImpl heatMapService;
    @Resource
    GpsDataDao gpsDataDao;
    @Resource
    Rate rate;
    @Resource
    GeoHashUtil geoHashUtil;
    /**
     *获取实况热力图；
     * @param interactionData 数据中包含两个点的经纬度和当前的请求时间；
     * @return json格式的带权点集；
     */
    @ResponseBody
    @RequestMapping(value = "/liveheatmap",method = RequestMethod.POST )
    public InteractionData getLiveMap(@RequestBody  InteractionData interactionData){
        return heatMapService.getLiveMap(interactionData);
    }

    /**
     * 查询某段时间的热力图；
     * @param data 数据中包含两个点的经纬度和请求的时间段；
     * @return json格式的带权点集；
     */
    @ResponseBody
    @RequestMapping(value = "/querymap",method = RequestMethod.POST)
    public InteractionData querySomeTimesMap(@RequestBody InteractionData data){
        return heatMapService.querySomeTimesMap(data);
    }



    @ResponseBody
    @RequestMapping(value = "/demanded",method = RequestMethod.POST)
    public ResponseDate getdemandedMap(@RequestBody InteractionData data){
        return heatMapService.getDemandMap(data);
    }
}
