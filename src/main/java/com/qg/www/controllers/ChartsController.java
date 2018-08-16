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
    ChartService chartService;

    @PostMapping("/changepercent")
    public ResponseData getChangePercent(@RequestBody InteractionData data){
        return chartService.getChangePercent(data);
    }

    @PostMapping("/utilizepercent")
    public ResponseData getUtilizePercent(@RequestBody InteractionData data){
        return chartService.getUtilizePercent(data);
    }

}
