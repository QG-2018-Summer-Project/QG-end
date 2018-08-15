package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 数据包装类；
 */
@Getter
@Setter
@Service("dataPack")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestData<T> {
    /**
     *  预测的月份
     */
    private Integer month;

    /**
     * 预测的时间段，用0-23中的一个数字；
     */
    private Integer hour;

    /**
     * 预测的天数
     */
    private Integer day;
    /**
     * 数据列表；
     */
    private List<T> list;
}
