package com.qg.www.controllers;


import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@CrossOrigin
@RequestMapping("/predict")
public class PredictController {

    @ResponseBody
    @RequestMapping(value = "/liyonglv", method = RequestMethod.POST)
    public InteractBigData xuqiuliang(@RequestBody RequestData<Feature> requestData){
        Iterator <Feature>it=requestData.getList().iterator();
        while(it.hasNext()){
            Feature feature=it.next();
            System.out.println(feature.getGeohash()+feature.getLat()+feature.getHour1());
        }
        GeoHash geoHash = new GeoHash();
        geoHash.setGeohash("as");
        geoHash.setWeight(12);
        geoHash.setWeight1(123);
        geoHash.setWeight2(111);
        geoHash.setWeight3(222);
        List<GeoHash> geoHashes = new ArrayList<>();
        geoHashes.add(geoHash);
        InteractBigData interactBigData = new InteractBigData();
        interactBigData.setPointSet(geoHashes);
        return interactBigData;
    }
}
