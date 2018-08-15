package com.qg.www.utils;


import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.RequestData;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Point;
import org.apache.http.HttpEntity;
import com.google.gson.Gson;
import com.qg.www.dtos.InteractionData;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class HttpClient {

    /**
     * 与数据挖掘端交互数据
     * @param url 发送的URL
     * @param requestData 传给数据
     * @return
     * @throws IOException
     */
    public InteractBigData demandedCount(String url, RequestData<Feature> requestData) throws IOException {
        // 将Json对象转换为字符串
        Gson gson = new Gson();
        String strJson = gson.toJson(requestData);
        //使用帮助类HttpClients创建CloseableHttpClient对象.
        CloseableHttpClient client = HttpClients.createDefault();
        //HTTP请求类型创建HttphttpPost实例
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        //组织数据
        StringEntity se = new StringEntity(strJson);
        se.setContentType("application/json");

        //对于httpPost请求,把请求体填充进HttphttpPost实体.
        httpPost.setEntity(se);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                strJson = EntityUtils.toString(entity,"UTF-8").trim();
                // TODO 暂时使用前端交互的点集
                InteractBigData data = gson.fromJson(strJson,InteractBigData.class);
                return data;
            }
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }


}
