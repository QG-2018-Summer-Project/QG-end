package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author net
 * @version 1.3
 * 路径的坐标集合集合；
 */
@Setter
@Getter
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoutePoint {
    /**
     * 经度；
     */
    private double lon;
    /**
     * 纬度；
     */
    private double lat;
    /**
     * 区域块；
     */
    private String geohash;
}
