package com.qg.www.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
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

}
