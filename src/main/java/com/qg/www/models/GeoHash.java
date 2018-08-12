package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service("geoHash")
public class GeoHash {
    private String geohash;
    private Integer weight;
}
