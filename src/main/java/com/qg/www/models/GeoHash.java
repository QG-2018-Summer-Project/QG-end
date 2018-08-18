package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeoHash {
    private String geohash;
    private Integer weight;
    private Float weight1;
    private Float weight2;
    private Float weight3;
}
