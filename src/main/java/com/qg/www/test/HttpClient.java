package com.qg.www.test;

import com.google.gson.Gson;
import com.qg.www.models.Point;
import com.qg.www.service.HeatMapService;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class HttpClient {
    @Resource
    HeatMapService heatMapService;

    public void requestToBaiDu() throws IOException {

        String url = "http:localhost:8080/forecast/test";
        Gson gson = new Gson();


        String content = "{\"date\":\"2017-11-06 18:27:10.0\",\"sum\":\"-3400\",\"complainOdrNbr\":\"null\"}";
        //使用帮助类HttpClients创建CloseableHttpClient对象.
        CloseableHttpClient client = HttpClients.createDefault();
        //HTTP请求类型创建HttphttpPost实例
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        //组织数据
        StringEntity se = new StringEntity(content);
        se.setContentType("application/json");

        //对于httpPost请求,把请求体填充进HttphttpPost实体.
        httpPost.setEntity(se);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = client.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {


            }
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }


}
