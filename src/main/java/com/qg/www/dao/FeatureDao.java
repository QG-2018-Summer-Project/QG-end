package com.qg.www.dao;

import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
import com.qg.www.models.Rate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author net
 * @version 1.1
 */
public interface FeatureDao {
    /**
     * 查询所有特征值；
     *
     * @param table 动态表名
     * @return 特征列表
     */
    List<Feature> listAllFeature(@Param("table") String table, @Param("data") InteractionData data,@Param("hour")Integer hour);

    /**
     * 查询过去出租车利用率平均值
     * @param data 经纬度范围
     * @param table 使用的表
     * @param hour 查询的时间段
     * @return 一个出租车利用率平均值
     */
    Double getAvgPercent(@Param("data") InteractionData data,@Param("table") String table, @Param("hour") Integer hour);

    List<Rate> listAllRate(@Param("table") String table, @Param("data") InteractionData data,@Param("hour")Integer hour);

    Double getAvgRate(@Param("data") InteractionData data,@Param("month") Integer month, @Param("day") Integer day, @Param("hour") Integer hour);
}
