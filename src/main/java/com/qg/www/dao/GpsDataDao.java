package com.qg.www.dao;

import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Rate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from taxi_rate")
    List<Rate> query();

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Insert("insert into rate (lon,lat,geohash,month,day,hour,feature1,feature2,feature3,feature4,feature5,feature6,feature7,feature8,feature9,rate)Values(" +
            "#{lon},#{lat},#{geohash},#{month},#{day},#{hour},#{feature1},#{feature2},#{feature3},#{feature4},#{feature5},#{feature6},#{feature7},#{feature8},#{feature9},#{rate})")
    void add(Rate bigData);

}
