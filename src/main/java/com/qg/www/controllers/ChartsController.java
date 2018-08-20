package com.qg.www.controllers;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.service.ChartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author net
 * @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/charts")
public class ChartsController {

    @Resource
    private ChartService chartService;

    /**
     * 得到未来某地区的出租车数量
     *
     * @param data 经纬度范围和当前时间
     * @return 当前时间前后共6个时间段的出租车数量变化率
     */
    @PostMapping("/changepercent")
    public ResponseData getChangePercent(@RequestBody InteractionData data) {
        System.out.println("开始");
        return chartService.getChangePercent(data);
    }

    /**
     * 得到未来某地区的出租车利用率
     *
     * @param data 经纬度范围和当前时间
     * @return 当前时间前后共6个时间段的出租车利用率
     */
    @PostMapping("/utilizepercent")
    public ResponseData getUtilizePercent(@RequestBody InteractionData data) {
        return chartService.getUtilizePercent(data);
    }

    /**
     * 获取地区异常情况
     *
     * @param data 当前时间
     * @return 异常情况和原因分析
     */
    @PostMapping("/exception")
    public ResponseData getExceptionCase(@RequestBody InteractionData data) {
        return chartService.getExceptionCase(data);
    }

}
