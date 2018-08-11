package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @author net
 * @version 1.0
 * 带权值的经纬相交点；
 */
@Setter
@Getter
public class Point {
    /**
     * 经度；
     */
    private Double lon;
    /**
     * 纬度；
     */
    private Double lat;
    /**
     * 权值；
     */
    private Integer weight;
}
