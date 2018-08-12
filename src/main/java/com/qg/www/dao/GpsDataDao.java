package com.qg.www.dao;

import com.qg.www.models.GeoHash;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 查询GPS数据的dao接口；
 */
public interface GpsDataDao {
    /**
     *查询某段时间的热力图；
     * @param leftTopLon 左上角经度
     * @param leftTopLat 左上角纬度
     * @param rightBottomLon 右下角经度
     * @param rightBottomLat 右下角纬度
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 该矩阵区域的某段时间内个GeoHash方块中的权值
     */
    List<GeoHash> listGeoHashAndNumByTimeAndLonAndBat(@Param("leftTopLon") Double leftTopLon,
                                                      @Param("leftTopLat") Double leftTopLat,
                                                      @Param("rightBottomLon") Double rightBottomLon,
                                                      @Param("rightBottomLat") Double rightBottomLat,
                                                      @Param("startTime") String startTime,
                                                      @Param("endTime") String endTime);


}
