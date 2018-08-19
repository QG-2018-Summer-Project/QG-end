package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 向数据挖掘组发送请求时的数据；
 */
@Getter
@Setter
@Service("requestData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestData<T> {

    private Integer month1;

    private Integer hour1;

    private Integer day1;
    /**
     * 数据列表；
     */
    private List<T> list;

    private List<T> oneHourAgo;

    private List<T> twoHourAgo;

    private List<T> threeHourAgo;

}
