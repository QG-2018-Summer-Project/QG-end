package com.qg.www.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
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