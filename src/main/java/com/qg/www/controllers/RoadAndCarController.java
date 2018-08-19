package com.qg.www.controllers;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.service.RecommendService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author net
 * @version 1.3
 * 车辆和路线控制；
 */
@RestController
@CrossOrigin
@RequestMapping("/roadandcar")
public class RoadAndCarController {
    @Resource
    private RecommendService recommendService;
    /**
     * 得到最优路径
     *
     * @param data 由前端提供的多条路径信息
     * @return 最优路径
     */
    @PostMapping("/querybestway")
    public ResponseData getBestWay(@RequestBody InteractionData data) {
        return recommendService.getBestWay(data);
    }

}
