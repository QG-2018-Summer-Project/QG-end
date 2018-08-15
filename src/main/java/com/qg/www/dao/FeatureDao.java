package com.qg.www.dao;

import com.qg.www.models.Feature;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author net
 * @version 1.1
 */
public interface FeatureDao {

    List<Feature> listAllFeature(@Param("table") String table);
}
