package com.qg.www.controllers;


import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Point;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/predict")
public class PredictController {

    @ResponseBody
    @RequestMapping(value = "/xuqiuliang", method = RequestMethod.POST)
    public InteractionData xuqiuliang(@RequestBody RequestData<Feature> requestData){
        System.out.println(requestData.getDay());
        GeoHash geoHash = new GeoHash();
        geoHash.setGeohash("as");
        geoHash.setWeight(12);
        List<GeoHash> geoHashes = new ArrayList<>();
        geoHashes.add(geoHash);
        InteractionData interactionData = new InteractionData();
        return interactionData;
    }
}
