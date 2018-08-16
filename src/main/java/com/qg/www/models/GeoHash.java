package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author net
 * @version 1.0
 * GEOHASH解码类；
 */
@Getter
@Setter
@Service("geoHash")
public class GeoHash {
    private String geohash;
    private Integer weight;
}
