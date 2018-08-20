package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qg.www.models.ExceptionCase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 响应给前端的数据处理类；
 */
@Getter
@Setter
@Service("responseData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    /**
     * 状态码
     */
    private String status;
    /**
     * 点集；
     */
    private List<T> pointSet;

    /**
     * 百分比数组 0.99形式
     */
    private Float[] percents;
    /**
     * 最优路径索引；
     */
    private Integer index;
    /**
     * 返回最优路线
     */
    private Integer[] steps;
}
