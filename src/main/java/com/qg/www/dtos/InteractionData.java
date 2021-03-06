package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.www.models.Point;
import com.qg.www.models.Routes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 前端与后台交互数据处理实体类
 */
@Getter
@Setter
@Service("interactionData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractionData {
    /**
     * 路线集合；
     */
    private List<Routes> routes;
    /**
     * 左上角经度
     */
    private Double leftTopLon;
    /**
     * 左上角纬度
     */
    private Double leftTopLat;
    /**
     * 右下角经度；
     */
    private Double rightBottomLon;
    /**
     * 右下角纬度；
     */
    private Double rightBottomLat;
    /**
     * 当前时间；
     */
    private String currentTime;
    /**
     * 返回点集；
     */
    private List<Point> pointSet;
    /**
     * 起始时间；
     */
    private String startTime;
    /**
     * 结束时间；
     */
    private String endTime;
    /**
     * 预测时间
     */
    private String predictedTime;
}
