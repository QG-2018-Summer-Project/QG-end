package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.www.models.ExceptionCase;
import com.qg.www.models.GeoHash;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 用于与数据挖掘组交互的类；
 */
@Setter
@Getter
@Service("interactBigData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteractBigData {
    /**
     * 用于接收数据挖掘组返回的带权geohash列表；
     */
    private List<GeoHash> pointSet;
    /**
     * 用于接收数据挖掘组返回的最优路线号；
     */
    private Integer index;
    /**
     * 用于接收数据挖掘组返回的路线；
     */
    private Integer[] steps;
    /**
     * 获取异常情况；
     */
    private List<ExceptionCase>alist;
}
