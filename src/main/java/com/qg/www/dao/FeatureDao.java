package com.qg.www.dao;

import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
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
}
