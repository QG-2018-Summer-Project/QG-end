package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author net
 * @version 1.0
 * 推荐路线集合；
 */
@Setter
@Getter
@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Routes {
    /**
     * 标记第几条路线；
     */
    private Integer index;
    /**
     * 步骤；
     */
    private List<Steps> steps;
    /**
     * 路线所花的时间；
     */
    private Long allTime;
    /**
     * 该路线的全长；
     */
    private Long distance;
}
