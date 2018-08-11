package com.qg.www.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author net
 * @version 1.0
 * 查询GPS数据的dao接口；
 */
public interface GpsDataDao {
    /**
     *
     * @param leftTopLon 左上角经度
     * @param leftTopLat 左上角纬度
     * @param rightBottomLon 右下角经度
     * @param rightBottomLat 右下角纬度
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 该矩阵区域的某段时间内个GeoHash方块中的权值
     */
    Map<String,Integer> listGeoHashAndNumByTimeAndLonAndBat(@Param("leftTopLon") Double leftTopLon,
                                                    @Param("leftTopBat") Double leftTopLat,
                                                    @Param("rightBottomLon") Double rightBottomLon,
                                                    @Param("rightBottomBat") Double rightBottomLat,
                                                    @Param("beginTime") Date beginTime,
                                                    @Param("endTime") Date endTime);


}
