package com.qg.www.utils;

import org.springframework.stereotype.Service;

import java.util.BitSet;
import java.util.HashMap;

/**
 * geohash解码工具类；
 * @author net
 * @version 1.0
 */
@Service("geoHashUtil")
public class GeoHashUtil {
    /**
     * 经纬度单独编码长度
     */
    private static int numbits = 6 * 5;
    /**
     * 32位编码对应字符
     */
    final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
            'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    /**
     * 定义编码映射关系
     */
    final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();

    //初始化编码映射内容
    static {
        int i = 0;
        for (char c : digits) {
            lookup.put(c, i++);
        }
    }

    /**
     * 对编码后的字符串解码
     *
     * @param geohash 编码后的字符串
     * @return 解码后的经纬度
     */
    public double[] decode(String geohash) {
        StringBuilder buffer = new StringBuilder();
        for (char c : geohash.toCharArray()) {

            int i = lookup.get(c) + 32;
            buffer.append(Integer.toString(i, 2).substring(1));
        }

        BitSet lonset = new BitSet();
        BitSet latset = new BitSet();

        //偶数位，经度
        int j = 0;
        for (int i = 0; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            lonset.set(j++, isSet);
        }

        //奇数位，纬度
        j = 0;
        for (int i = 1; i < numbits * 2; i += 2) {
            boolean isSet = false;
            if (i < buffer.length()) {
                isSet = buffer.charAt(i) == '1';
            }
            latset.set(j++, isSet);
        }

        double lon = decode(lonset, -180, 180);
        double lat = decode(latset, -90, 90);

        return new double[]{lat, lon};
    }

    /**
     * 解码
     * @param bs
     * @param floor
     * @param ceiling
     * @return
     */
    private double decode(BitSet bs, double floor, double ceiling) {
        double mid = 0;
        for (int i = 0; i < bs.length(); i++) {
            mid = (floor + ceiling) / 2;
            if (bs.get(i)) {
                floor = mid;
            } else {
                ceiling = mid;
            }
        }
        return mid;
    }





}
