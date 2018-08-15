package com.qg.www.dtos;

import org.springframework.stereotype.Service;

import java.util.List;

@Service("responseDate")
public class ResponseData<T> {
    /**
     * 数据列表；
     */
    private List<T> list;
}
