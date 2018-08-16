package com.qg.www.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author OK
 * @version 1.0
 * data的控制类
 */
@Getter
@Setter
@Service("gpsData")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GpsData {
    private Long id;

    private String licenseplateno;

    private Date gpsTime;

    private Double longitude;

    private Double latitude;

    private Integer speed;

    private String carStat1;

    private String geohash;

    private String rowKey;

    private String hourRepre;
}