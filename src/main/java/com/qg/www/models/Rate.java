package com.qg.www.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service("rate")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rate {

    private Integer id;

    private String geohash;

    private Double lon;

    private Double lat;

    private Integer month;

    private Integer day;

    private Integer hour;

    private Integer feature1;

    private Integer feature2;

    private Float feature3;

    private Float feature4;

    private Float feature5;

    private Float feature6;

    private Float feature7;

    private Float feature8;

    private Float feature9;

    private Float rate;
}