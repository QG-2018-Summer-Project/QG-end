package com.qg.www.service.impl;

import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Status;
import com.qg.www.enums.Url;
import com.qg.www.models.RoutePoint;
import com.qg.www.models.Routes;
import com.qg.www.models.Steps;
import com.qg.www.service.RecommendService;
import com.qg.www.utils.GeoHashUtil;
import com.qg.www.utils.HttpClientUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author net
 * @version 1.3
 * 推荐业务的实现；
 */
@Service
public class RecommendServiceImpl implements RecommendService {
    @Resource
    private GeoHashUtil geoHashUtil;
    @Resource
    private RequestData<Routes> requestData;
    @Resource
    private InteractBigData bigData;

    /**
     * 获取最优路径；
     *
     * @param data 数据中包含列举的几道路线选择；
     * @return 最优路线索引；
     */
    @Override
    public ResponseData getBestWay(InteractionData data) {
        System.out.println("路径推荐请求！");
        //定义返回model
        ResponseData<RoutePoint> responseData = new ResponseData<>();
        //定义路线列表；
        List<Routes> routesList = data.getRoutes();
        //定义步骤列表；
        List<Steps> stepsList;
        //定义局部坐标集合；
        List<RoutePoint> pathList;
        //如果路线列表非空，则创建循环；
        if (!routesList.isEmpty()) {
            //创建迭代器进行迭代；
            Iterator<Routes> routesIterator = routesList.iterator();
            while (routesIterator.hasNext()) {
                //获取列表；
                stepsList = routesIterator.next().getSteps();
                //判空,非空则创建循环，进行下一层迭代；
                if (!stepsList.isEmpty()) {
                    Iterator<Steps> stepsIterator = stepsList.iterator();
                    while (stepsIterator.hasNext()) {
                        //获取步骤；
                        Steps steps = stepsIterator.next();
                        //获取端点；
                        RoutePoint start = steps.getStartLocation();
                        RoutePoint end = steps.getEndLocation();
                        //转化成为geohash;
                        start.setGeohash(geoHashUtil.encode(start.getLat(), start.getLon()));
                        end.setGeohash(geoHashUtil.encode(end.getLat(), end.getLon()));
                        pathList = steps.getPath();
                        //如果点集不为空，进行迭代，同时将所有的经纬点转换为7位GEOHASH；
                        if (!pathList.isEmpty()) {
                            Iterator<RoutePoint> pointIterator = pathList.iterator();
                            //将经纬度转换成为geohash;
                            while (pointIterator.hasNext()) {
                                RoutePoint routePoint = pointIterator.next();
                                routePoint.setGeohash(geoHashUtil.encode(routePoint.getLat(), routePoint.getLon()));
                            }
                        }
                    }

                }
            }
            requestData.setList(routesList);
            //向数据挖掘组发送请求获取数据；
            try {
                bigData = HttpClientUtil.demandedCount(Url.ROUTE_RECOMMEND.getUrl(), requestData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //如果数据挖掘给的返回的路线索引不为0，则正常响应给前端；
            if (null!=bigData&&bigData.getIndex()!=0){
                responseData.setIndex(bigData.getIndex());
                responseData.setStatus(Status.NORMAL.getStatus());
            }else {
                //否则，数据挖掘预测失败；
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
            }
        } else {
            responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
        }
        return responseData;
    }
}
