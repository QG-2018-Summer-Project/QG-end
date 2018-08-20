package com.qg.www.service;

import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.models.Feature;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author net
 * @verison 1.3
 * 预测未来数据图表的接口；
 */
public interface ChartService {
    /**
     * 得到过去和未来的变化率
     *
     * @param data 经纬度范围和预测时间
     * @return 6段变化率
     */
    ResponseData getChangePercent(@RequestBody InteractionData data);

    /**
     * 得到过去和未来车辆的利用率
     *
     * @param data 经纬度范围和当前时间
     * @return 6段变化率
     */
    ResponseData getUtilizePercent(@RequestBody InteractionData data);

    /**
     * 得到过去和未来广州市交通的拥堵率
     *
     * @param data 经纬度范围和当前时间
     * @return 6段拥堵率
     */
    ResponseData<Feature> getCrowdedPercent(InteractionData data);

}
