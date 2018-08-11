package com.qg.www.service;

import com.qg.www.models.InteractionData;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author net
 * @version 1.0
 * 热力图业务接口
 */
public interface HeatMapService {
    /**
     *
     * @param data 数据中包含两个点的经纬度和当前的请求时间
     * @return 带权点集
     */
    InteractionData querySomeTimesMap(InteractionData data);
}
