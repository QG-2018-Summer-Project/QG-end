package com.qg.www.enums;

/**
 * 存放与数据挖掘端交互的URL\
 *
 * @author net
 * @version 1.3
 */
public enum Url {
    /**
     * 请求未来出租车需求量
     */
    DEMAND("http://192.168.1.101:5000/qgtaxi/predict/count"),

    /**
     * 请求未来出租车数量
     */
    COUNT("http://192.168.1.101:5000/qgtaxi/predict/count"),

    /**
     * 请求地区的未来一、二、三小时流量变化率
     */
    CHANGE_PERCENT("http://127.0.0.1:8080/qgtaxi/predict/liyonglv"),

    /**
     * 请求地区的未来一、二、三小时出租车利用率
     */
    UTILIZE_PERCENT("http://127.0.0.1:8080/qgtaxi/predict/liyonglv"),
    /**
     * 路线推荐
     */
    ROUTE_RECOMMEND("http://192.168.1.101:5000/qgtaxi/route/recommend")
    ;

    private String url;

    Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
