package com.qg.www.service;


import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;

/**
 * @author net
 * @version 1.0
 * 热力图业务接口
 */
public interface HeatMapService {
    /**
     * 查询某时间段的热力图
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    ResponseData querySomeTimesMap(InteractionData data);

    /**
     * 获取实况热力图；
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    ResponseData getLiveMap(InteractionData data);

    /**
     * 获取汽车需求量热力图；
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    ResponseData getDemandMap(InteractionData data);

    /**
     * 获取预测汽车数量热力图；
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    ResponseData getPredictCarMap(InteractionData data);
}
