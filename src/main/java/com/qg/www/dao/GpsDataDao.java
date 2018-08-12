package com.qg.www.dao;

import com.qg.www.models.GeoHash;
import com.qg.www.models.InteractionData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 查询GPS数据的dao接口；
 */
public interface GpsDataDao {
    /**
     *
     * 根据空间与时间范围得到GeoHash的权重
     * @param data 左上角与右下角的经纬度以及时间范围
     * @return 在以上范围内每个GeoHash块中出现的车的数量
     */
    List<GeoHash> listGeoHashAndNumByTimeAndLonAndBat(InteractionData data);


}
