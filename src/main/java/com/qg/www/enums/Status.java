package com.qg.www.enums;

/**
 * 响应状态码；
 *
 * @author net
 * @version 1.0
 */
public enum Status {
    /**
     * 一切正常；
     */
    NORMAL("2000"),
    /**
     * 服务器发生未知错误；
     */
    SERVER_HAPPEN_ERROR("5000"),
    /**
     * 预测数据发生缺失；
     */
    PREDICTDATA_LACK("5001"),
    /**
     * 前端发送的数据出现错误；
     */
    DATAFROM_WEB_ERROR("5003");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
