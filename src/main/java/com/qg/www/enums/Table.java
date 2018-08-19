package com.qg.www.enums;

import javax.ws.rs.POST;

/**
 * @author net
 * @version 1.3
 * 对应数据库的表名
 */
public enum  Table {
    /**
     * 出租车利用率
     */
    RATE("rate"),

    /**
     * 出租车变化率
     */
    PERCENT("percent"),

    /**
     * 出租车数量
     */
    COUNT("count"),

    /**
     * 出租车需求量
     */
    NEED("need"),

    /**
     * 出租车数据
     */
    GPS_DATA("gpsdata"),
    /**
     * 拥挤率表
     */
    CROWDED("as");


    private String table;

    Table(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }
}
