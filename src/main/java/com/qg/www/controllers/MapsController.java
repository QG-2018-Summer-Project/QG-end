package com.qg.www.controllers;

import com.qg.www.models.InteractionData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author net
 * @version 1.0
 * 地图展示控制器
 */
@Controller
@RequestMapping("/maps")
public class MapsController {
    /**
     *获取实况热力图；
     * @param data 数据中包含两个点的经纬度和当前的请求时间；
     * @return json格式的带权点集；
     */
    @ResponseBody
    @RequestMapping(value = "/liveheatmap",method = RequestMethod.POST)
    public String getLiveMap(@RequestBody InteractionData data){
        return null;
    }

    /**
     * 查询某段时间的热力图；
     * @param data 数据中包含两个点的经纬度和请求的时间段；
     * @return json格式的带权点集；
     */
    @ResponseBody
    @RequestMapping(value = "/querymap",method = RequestMethod.POST)
    public String querySomeTimesMap(@RequestBody InteractionData data){
        return null;
    }
}