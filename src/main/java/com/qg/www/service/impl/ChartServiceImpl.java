package com.qg.www.service.impl;

import com.qg.www.dao.FeatureDao;
import com.qg.www.dtos.InteractBigData;
import com.qg.www.dtos.InteractionData;
import com.qg.www.dtos.RequestData;
import com.qg.www.dtos.ResponseData;
import com.qg.www.enums.Status;
import com.qg.www.enums.Table;
import com.qg.www.enums.Url;
import com.qg.www.models.Feature;
import com.qg.www.models.GeoHash;
import com.qg.www.models.Rate;
import com.qg.www.service.ChartService;
import com.qg.www.utils.HttpClientUtil;
import com.qg.www.utils.TimeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * @author net
 * @version 1.3
 * 预测未来数据图表实现类
 */
@Service("chartService")
public class ChartServiceImpl implements ChartService {
    @Resource
    private TimeUtil timeUtil;
    @Resource
    private FeatureDao featureDao;
    @Resource
    private InteractBigData bigData;
    @Resource
    private ResponseData responseData;
    @Resource
    private List<GeoHash> geoHashList;
    @Resource
    private RequestData requestData;


    /**
     * 得到未来的变化率
     *
     * @param data 经纬度范围和预测时间
     * @return 6段变化率
     */
    @Override
    public ResponseData getChangePercent(InteractionData data) {
        // 查询使用的表名
        String table = "";
        // 当前时间的月份
        Integer currentMonth;
        // 当前时间的天数
        Integer currentDay;
        // 当前时间的小时段
        Integer currentHour;
        // 当前时间
        String currentTime = data.getCurrentTime();
        RequestData<Feature> requestData = new RequestData<>();
        if (null != currentTime && currentTime.length() >= 14) {
            // 截取预测时间的日期天数和时间
            currentMonth = Integer.parseInt(currentTime.substring(5, 7));
            currentDay = Integer.parseInt(currentTime.substring(8, 10));
            currentHour = Integer.parseInt(currentTime.substring(11, 13));

            requestData.setDay1(currentDay);
            requestData.setHour1(currentHour);
            requestData.setMonth1(currentMonth);

            try {
                // 得到应该查询的数据表
                table = timeUtil.getDemandTable(currentTime, Table.PERCENT.getTable());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Feature> oneHourAgo = null;
            List<Feature> twoHourAge = null;
            List<Feature> threeHourAge = null;
            if (currentHour > 3) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    calendar.setTime(sdf.parse(data.getCurrentTime()));
                    // 提前一小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                    oneHourAgo = featureDao.listAllFeature(table, data, currentHour);


                    // 提前两小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                    twoHourAge = featureDao.listAllFeature(table, data, currentHour);

                    // 提前三小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                    threeHourAge = featureDao.listAllFeature(table, data, currentHour);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (currentHour > 21) {
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                responseData.setPercents(new Float[6]);
                return responseData;
            } else {
                // 得到表中的所有信息
                oneHourAgo = featureDao.listAllFeature(table, data, currentHour);
                twoHourAge = featureDao.listAllFeature(table, data, currentHour);
                threeHourAge = featureDao.listAllFeature(table, data, currentHour);
            }
            if (oneHourAgo != null && twoHourAge != null && threeHourAge != null) {
                requestData.setOneHourAgo(oneHourAgo);
                requestData.setTwoHourAgo(twoHourAge);
                requestData.setThreeHourAgo(threeHourAge);
            } else {
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                responseData.setPercents(new Float[6]);
                return responseData;
            }

            // 得到数据挖掘端预测的当前时段与未来一、二小时的流量变化率
            try {
                bigData = HttpClientUtil.demandedCount(Url.UTILIZE_PERCENT.getUrl(), requestData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Float[] percents = new Float[6];

            // 判断返回值是否为空
            if (null != bigData) {
                geoHashList = bigData.getPointSet();
                // 判断预测值是否为空
                if (null != geoHashList) {
                    // 当时段小于3时，需要跨表查询
                    if (currentHour < 3) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            calendar.setTime(sdf.parse(data.getCurrentTime()));
                            // 提前一小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                            // 得到平均值
                            percents[2] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                            // 提前两小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                            // 得到平均值
                            percents[1] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                            // 提前三小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.PERCENT.getTable());
                            // 得到平均值
                            percents[0] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (currentHour > 21) {
                        responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                        responseData.setPercents(new Float[6]);
                        return responseData;
                    } else {
                        // 查询数据库得到前三、二、一小时的出租车数量变化率的平均值
                        percents[0] = featureDao.getAvgPercent(data, table, currentHour - 3);
                        percents[1] = featureDao.getAvgPercent(data, table, currentHour - 2);
                        percents[2] = featureDao.getAvgPercent(data, table, currentHour - 1);
                    }
                    // 判断数据库中是否存在数据
                    if (null != percents[0]) {
                        // 遍历得到数据挖掘端传过来的值的平均值
                        responseData.setPercents(getAvgWeight(geoHashList, percents, Table.PERCENT.getTable()));
                        responseData.setStatus(Status.NORMAL.getStatus());
                        return responseData;
                    }
                }
            }
        } else {
            //前端传递的信息错误时，返回状态码；
            responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
            responseData.setPercents(new Float[6]);
            return responseData;
        }

        //查询成功，但是数据为空；
        responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
        responseData.setPercents(new Float[6]);
        return responseData;
    }

    /**
     * 得到过去和未来车辆的利用率
     *
     * @param data 经纬度范围和预测时间
     * @return 6段变化率
     */
    @Override
    public ResponseData getUtilizePercent(InteractionData data) {
        // 查询使用的表名
        String table = "";
        // 当前时间的月份
        Integer currentMonth;
        // 当前时间的天数
        Integer currentDay;
        // 当前时间的小时段
        Integer currentHour;
        // 当前时间
        String currentTime = data.getCurrentTime();
        RequestData<Rate> requestData = new RequestData<>();
        ResponseData<Rate> responseData = new ResponseData<>();
        if (null != currentTime && currentTime.length() >= 14) {
            // 截取预测时间的日期天数和时间
            currentMonth = Integer.parseInt(currentTime.substring(5, 7));
            currentDay = Integer.parseInt(currentTime.substring(8, 10));
            currentHour = Integer.parseInt(currentTime.substring(11, 13));

            requestData.setDay1(currentDay);
            requestData.setHour1(currentHour);
            requestData.setMonth1(currentMonth);

            try {
                // 得到应该查询的数据表
                table = timeUtil.getDemandTable(currentTime, Table.RATE.getTable());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Rate> oneHourAgo = null;
            List<Rate> twoHourAge = null;
            List<Rate> threeHourAge = null;
            if (currentHour > 3) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    calendar.setTime(sdf.parse(data.getCurrentTime()));
                    // 提前一小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                    oneHourAgo = featureDao.listAllRate(table, data, currentHour);


                    // 提前两小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                    twoHourAge = featureDao.listAllRate(table, data, currentHour);

                    // 提前三小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                    threeHourAge = featureDao.listAllRate(table, data, currentHour);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (currentHour > 21) {
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                responseData.setPercents(new Float[6]);
                return responseData;
            } else {
                // 得到表中的所有信息
                oneHourAgo = featureDao.listAllRate(table, data, currentHour);
                twoHourAge = featureDao.listAllRate(table, data, currentHour);
                threeHourAge = featureDao.listAllRate(table, data, currentHour);
            }
            // 如果查到的数据为空
            if (null != oneHourAgo && null != twoHourAge && null != threeHourAge) {
                // 将参数放入交互model中
                requestData.setOneHourAgo(oneHourAgo);
                requestData.setTwoHourAgo(twoHourAge);
                requestData.setThreeHourAgo(threeHourAge);

                // 得到数据挖掘端预测的当前时段与未来一、二小时的流量变化率
                try {
                    bigData = HttpClientUtil.demandedCount(Url.UTILIZE_PERCENT.getUrl(), requestData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Float[] percents = new Float[6];

                // 判断返回值是否为空
                if (null != bigData) {
                    geoHashList = bigData.getPointSet();
                    // 判断预测值是否为空
                    if (null != geoHashList) {
                        // 当时段小于3时，需要跨表查询
                        if (currentHour < 3) {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                calendar.setTime(sdf.parse(data.getCurrentTime()));
                                // 提前一小时
                                calendar.add(Calendar.HOUR_OF_DAY, -1);
                                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                                percents[2] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                                // 提前两小时
                                calendar.add(Calendar.HOUR_OF_DAY, -1);
                                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                                percents[1] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                                // 提前三小时
                                calendar.add(Calendar.HOUR_OF_DAY, -1);
                                table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.RATE.getTable());
                                percents[0] = featureDao.getAvgRate(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (currentHour > 21) {
                            responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                            responseData.setPercents(new Float[6]);
                            return responseData;
                        } else {
                            // 查询数据库得到前三、二、一小时的出租车数量变化率的平均值
                            percents[0] = featureDao.getAvgRate(data, table, currentHour - 3);
                            percents[1] = featureDao.getAvgRate(data, table, currentHour - 2);
                            percents[2] = featureDao.getAvgRate(data, table, currentHour - 1);
                        }
                        // 判断数据库中是否存在数据
                        if (null != percents[0]) {
                            // 遍历得到数据挖掘端传过来的值的平均值
                            percents = getAvgWeight(geoHashList, percents, Table.RATE.getTable());
                            // 响应回页面
                            responseData.setPercents(percents);
                            responseData.setStatus(Status.NORMAL.getStatus());
                            return responseData;
                        }
                    }
                }
            }
        } else {
            //前端传递的信息错误时，返回状态码；
            responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
            responseData.setPercents(new Float[6]);
            return responseData;
        }

        //查询成功，但是数据为空；
        responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
        responseData.setPercents(new Float[6]);
        return responseData;
    }

    public ResponseData<Feature> getCrowdedPercent(InteractionData data) {
        // 查询使用的表名
        String table = "";
        // 当前时间的月份
        Integer currentMonth;
        // 当前时间的天数
        Integer currentDay;
        // 当前时间的小时段
        Integer currentHour;
        // 当前时间
        String currentTime = data.getCurrentTime();
        RequestData<Feature> requestData = new RequestData<>();
        ResponseData<Feature> responseData = new ResponseData<>();

        if (null != currentTime && currentTime.length() >= 14) {
            // 截取预测时间的日期天数和时间
            currentMonth = Integer.parseInt(currentTime.substring(5, 7));
            currentDay = Integer.parseInt(currentTime.substring(8, 10));
            currentHour = Integer.parseInt(currentTime.substring(11, 13));

            requestData.setDay1(currentDay);
            requestData.setHour1(currentHour);
            requestData.setMonth1(currentMonth);

            try {
                // 得到应该查询的数据表
                table = timeUtil.getDemandTable(currentTime, Table.CROWDED.getTable());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Feature> oneHourAgo = null;
            List<Feature> twoHourAge = null;
            List<Feature> threeHourAge = null;
            if (currentHour > 3) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    calendar.setTime(sdf.parse(data.getCurrentTime()));
                    // 提前一小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                    oneHourAgo = featureDao.listAllFeature(table, data, currentHour);


                    // 提前两小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                    twoHourAge = featureDao.listAllFeature(table, data, currentHour);

                    // 提前三小时
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                    threeHourAge = featureDao.listAllFeature(table, data, currentHour);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else if (currentHour > 21) {
                responseData.setPercents(new Float[6]);
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                return responseData;
            } else {
                // 得到表中的所有信息
                threeHourAge = featureDao.listAllFeature(table, data, currentHour);
                twoHourAge = featureDao.listAllFeature(table, data, currentHour);
                oneHourAgo = featureDao.listAllFeature(table, data, currentHour);
            }

            if (oneHourAgo != null && twoHourAge != null && threeHourAge != null) {
                requestData.setThreeHourAgo(threeHourAge);
                requestData.setTwoHourAgo(twoHourAge);
                requestData.setOneHourAgo(oneHourAgo);
            } else {
                responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                responseData.setPercents(new Float[6]);
                return responseData;
            }

            // 得到数据挖掘端预测的当前时段与未来一、二小时的流量变化率
            try {
                bigData = HttpClientUtil.demandedCount(Url.UTILIZE_PERCENT.getUrl(), requestData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Float[] percents = new Float[6];

            // 判断返回值是否为空
            if (null != bigData) {
                geoHashList = bigData.getPointSet();
                // 判断预测值是否为空
                if (null != geoHashList) {
                    // 当时段小于3时，需要跨表查询
                    if (currentHour < 3) {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            calendar.setTime(sdf.parse(data.getCurrentTime()));
                            // 提前一小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                            // 得到平均值
                            percents[2] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                            // 提前两小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                            // 得到平均值
                            percents[1] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                            // 提前三小时
                            calendar.add(Calendar.HOUR_OF_DAY, -1);
                            // 得到对应的表名
                            table = timeUtil.getDemandTable(sdf.format(calendar.getTime()), Table.CROWDED.getTable());
                            // 得到平均值
                            percents[0] = featureDao.getAvgPercent(data, table, calendar.get(Calendar.HOUR_OF_DAY));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (currentHour > 21) {
                        responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
                        responseData.setPercents(new Float[6]);
                        return responseData;
                    } else {
                        // 查询数据库得到前三、二、一小时的出租车数量拥堵率的平均值
                        percents[2] = featureDao.getAvgPercent(data, table, currentHour - 1);
                        percents[1] = featureDao.getAvgPercent(data, table, currentHour - 2);
                        percents[0] = featureDao.getAvgPercent(data, table, currentHour - 3);

                    }
                    // 判断数据库中是否存在数据
                    if (null != percents[0]) {
                        // 遍历得到数据挖掘端传过来的值的平均值
                        responseData.setPercents(getAvgWeight(geoHashList, percents, Table.CROWDED.getTable()));
                        responseData.setStatus(Status.NORMAL.getStatus());
                        return responseData;
                    }
                }
            }
        } else {
            //前端传递的信息错误时，返回状态码；
            responseData.setStatus(Status.DATAFROM_WEB_ERROR.getStatus());
            responseData.setPercents(new Float[6]);
            return responseData;
        }

        //查询成功，但是数据为空；
        responseData.setStatus(Status.PREDICTDATA_LACK.getStatus());
        responseData.setPercents(new Float[6]);
        return responseData;
    }


    private Float[] getAvgWeight(List<GeoHash> geoHashList, Float[] percents, String table) {
        // 遍历得到数据挖掘端传过来的值的平均值
        int i = 0;
        float currentPercent = 0;
        float oneLatePercent = 0;
        float twoLatePercent = 0;
        for (GeoHash geoHash : geoHashList) {
            currentPercent += geoHash.getWeight1();
            oneLatePercent += geoHash.getWeight2();
            twoLatePercent += geoHash.getWeight3();
            i++;
        }
        percents[3] = currentPercent / i;
        percents[4] = oneLatePercent / i;
        percents[5] = twoLatePercent / i;

        if (Table.RATE.getTable().equals(table)) {
            for (int j = 0; j < 6; j++) {
                percents[j] *= 100;
            }
        }

        // 将Float数组转化为小数位一位
        DecimalFormat df = new DecimalFormat("0.0");
        for (i = 0; i < 6; i++) {
            percents[i] = Float.valueOf(df.format(percents[i]));
        }
        return percents;
    }

}
