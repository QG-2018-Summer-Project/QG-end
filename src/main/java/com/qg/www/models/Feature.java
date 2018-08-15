package com.qg.www.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service("countTrait")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Feature {
    private Integer id;
    private String geohash;

    private Double lon;

    private Double lat;

    private Integer month1;

    private Integer day1;

    private Integer hour1;

    private Integer dure1;

    private Integer dure2;

    private Integer dure3;

    private Integer dure4;

    private Integer dure5;

    private Integer dure6;

    private Integer dure7;

    private Float one;

    private Float two;

    private Float three;

    private Float four;

    private Float five;

    private Float true1;
}