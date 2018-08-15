package com.qg.www.dao;

import com.qg.www.dtos.InteractionData;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
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

    @Select("select * from tezheng_xuqiuliang")
    List<Feature> query();

    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    @Insert("insert into demand7 (lon,lat,geohash,month1,day1,hour1,dure1,dure2,dure3,dure4,dure5,dure6,dure7,one,two,three,four,five,true1)Values(" +
            "#{lon},#{lat},#{geohash},#{month1},#{day1},#{hour1},#{dure1},#{dure2},#{dure3},#{dure4},#{dure5},#{dure6},#{dure7},#{one},#{two},#{three},#{four},#{five},#{true1})")
    void add(Feature bigData);

}
