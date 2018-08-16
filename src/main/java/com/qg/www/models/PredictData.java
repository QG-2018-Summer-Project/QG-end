package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author net
 * @version 1.0
 * 预测数据类；
 */
@Setter
@Getter
@Service("predictData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PredictData {
    /**
     * geohash值
     */
    private String geohash;
    /**
     * 7个独热值
     */
    private Integer dure1;
    private Integer dure2;
    private Integer dure3;
    private Integer dure4;
    private Integer dure5;
    private Integer dure6;
    private Integer dure7;
    /**
     * 5个时间段的特征值；
     */
    private Double one;
    private Double two;
    private Double three;
    private Double four;
    private Double five;

}
