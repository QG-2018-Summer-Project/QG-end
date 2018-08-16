package com.qg.www.controllers;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.service.impl.HeatMapServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author net
 * @version 1.0
 * 地图展示控制器
 */
@RestController
@CrossOrigin
@RequestMapping("/maps")
public class MapsController {

    @Resource
    HeatMapServiceImpl heatMapService;

    /**
     * 获取实况热力图；
     *
     * @param interactionData 数据中包含两个点的经纬度和当前的请求时间；
     * @return json格式的带权点集；
     */
    @PostMapping("/liveheatmap")
    public ResponseData getLiveMap(@RequestBody InteractionData interactionData) {
        return heatMapService.getLiveMap(interactionData);
    }

    /**
     * 查询某段时间的热力图；
     *
     * @param data 数据中包含两个点的经纬度和请求的时间段；
     * @return json格式的带权点集；
     */
    @PostMapping("/querymap")
    public ResponseData querySomeTimesMap(@RequestBody InteractionData data) {
        return heatMapService.querySomeTimesMap(data);
    }

    /**
     * 获取汽车需求量的热力图；
     *
     * @param data 数据中包含两个点的经纬度和请求的时间段；
     * @return json格式的带权点集；
     */
    @PostMapping("/demanded")
    public ResponseData getDemandedMap(@RequestBody InteractionData data) {
        return heatMapService.getDemandMap(data);
    }

    /**
     * 预测汽车数量热力图；
     *
     * @param data 数据中包含两个点的经纬度和请求的时间段；
     * @return json格式的带权点集；
     */
    @PostMapping("/count")
    public ResponseData getPredictCarMap(@RequestBody InteractionData data) {
        return heatMapService.getPredictCarMap(data);
    }
}
