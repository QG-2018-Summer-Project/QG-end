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
    private List<Point> path;
    /**
     * 起点；
     */
    private Point startLocation;
    /**
     * 终点；
     */
    private Point endLocation;
    /**
     * 该路段花费的时间；
     */
    private Long time;

}
