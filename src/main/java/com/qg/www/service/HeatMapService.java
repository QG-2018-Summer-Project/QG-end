package com.qg.www.service;

import com.qg.www.models.InteractionData;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author net
 * @version 1.0
 * 热力图业务接口
 */
public interface HeatMapService {
    InteractionData querySomeTimesMap(InteractionData data);
}
