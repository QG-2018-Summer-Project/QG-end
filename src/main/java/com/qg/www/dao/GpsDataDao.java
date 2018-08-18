package com.qg.www.dao;

import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.dtos.InteractionData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 查询GPS数据的dao接口；
 */
public interface GpsDataDao {
    /**
     * 根据空间与时间范围得到GeoHash的权重
     * @param tableOne 表名一
     * @param tableTwo 表名二
     * @param data 左上角与右下角的经纬度以及时间范围
     * @return 在以上范围内每个GeoHash块中出现的车的数量
     */
    List<GeoHash> listGeoHashAndNumByTimeAndLonAndLat(@Param("data") InteractionData data, @Param("tableOne") String tableOne, @Param("tableTwo")String tableTwo);

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Select("SELECT * FROM tezheng_percent WHERE day1 = 8")
    List<Feature> listAllFeature();

    @Insert("insert into percent8 (lon,lat,geohash,month1,day1,hour1,dure1,dure2,dure3,dure4,dure5,dure6,dure7,one,two,three,four,five,true1)Values(" +
            "#{lon},#{lat},#{geohash},#{month1},#{day1},#{hour1},#{dure1},#{dure2},#{dure3},#{dure4},#{dure5},#{dure6},#{dure7},#{one},#{two},#{three},#{four},#{five},#{true1})")
    int addFeature(Feature feature);
}
