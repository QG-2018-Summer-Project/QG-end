package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author net
 * @version 1.0
 * 带权值的经纬相交点；
 */
@Setter
@Getter
@Service("point")
@Scope("prototype")
public class Point {
    /**
     * 经度；
     */
    private double lon;
    /**
     * 纬度；
     */
    private double lat;
    /**
     * 权值；
     */
    private int weight;
}
