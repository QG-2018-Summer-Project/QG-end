package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author net
 * @version 1.0
 * 异常情况类；
 */
@Setter
@Getter
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionCase {
    private String time;
    private Double lon;
    private Double lat;
    private Integer type;
    private String reason;
}
