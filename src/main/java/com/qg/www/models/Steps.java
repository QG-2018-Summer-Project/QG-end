package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 路径点集类；
 */
@Setter
@Getter
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Steps {
    /**
     * 路径集合；
     */
    private List<RoutePoint> path;
    /**
     * 起点；
     */
    private RoutePoint startLocation;
    /**
     * 终点；
     */
    private RoutePoint endLocation;
    /**
     * 该路段花费的时间；
     */
    private Integer time;

}
