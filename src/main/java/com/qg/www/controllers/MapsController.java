package com.qg.www.controllers;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.service.HeatMapService;
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
    HeatMapService heatMapService;

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
     * 未来热力图预测；
     *
     * @param data 数据中有两个点的经纬度和请求查看的未来时间；
     * @return 返回热力图数据；
     */
    @PostMapping("/predictmap")
    public ResponseData predictSometimesMap(@RequestBody InteractionData data) {
        return null;
    }

    /**
     * 预测出租车数量；
     *
     * @param data 数据中有两个点的经纬度和请求查看的未来时间；
     * @return 返回热力图数据；
     */
    @PostMapping("/predicttaxi")
    public ResponseData predictCarUsage(@RequestBody InteractionData data) {
        return null;
    }
}
