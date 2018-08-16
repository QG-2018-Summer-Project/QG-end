package com.qg.www.controllers;

import com.qg.www.dtos.InteractionData;
import org.apache.http.protocol.ResponseDate;
import org.springframework.web.bind.annotation.*;

/**
 * @author net
 * @version 1.3
 */
@RestController
@CrossOrigin
@RequestMapping("/roadandcar")
public class RoadAndCarController {

    /**
     * 得到最优路径
     * @param data 由前端提供的多条路径信息
     * @return 最优路径
     */
    @PostMapping("/querybestway")
    public ResponseDate getBestWay(@RequestBody InteractionData data){
        System.out.println(data.getRoutes().get(0).getSteps().get(0).getPath().get(0).getLat());
        return null;
    }

}
