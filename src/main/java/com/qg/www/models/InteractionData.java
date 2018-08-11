package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author net
 * @version 1.0
 * 前端与后台交互数据处理实体类
 */
@Getter
@Setter
public class InteractionData {
    /**
     *左上角经度
     */
    private Double leftTopLon;
    /**
     *左上角纬度
     */
    private Double leftTopLat;
    /**
     *右下角经度；
     */
    private Double rightBottomLon;
    /**
     *右下角纬度；
     */
    private Double rightBottomLat;
    /**
     *当前时间；
     */
    private Date currentTime;
    /**
     * 返回点集；
     */
    private List<Point> pointSet;
    /**
     * 起始时间；
     */
    private Date startTime;
    /**
     * 结束时间；
     */
    private Date endTime;
}
