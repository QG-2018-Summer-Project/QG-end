package com.qg.www.service;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;

/**
 * @author net
 * @version 1.3
 * 推荐业务接口；
 */
public interface RecommendService {
    /**
     * 获取最优路径；
     *
     * @param data 数据中包含列举的几道路线选择；
     * @return 最优路线索引；
     */
    ResponseData getBestWay(InteractionData data);
}
