package com.ahuiali.word.common.utils;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.ahuiali.word.common.constant.SpiderConstant.DOT;
import static com.ahuiali.word.common.constant.SpiderConstant.URL_SPLIT;

/**
 * UrlUtil
 * 链接工具类
 * @author ZhengChaoHui
 * @date 2020/12/13 11:18
 */
@Component
public class UrlUtil {

    /**
     * 判断链接是否在数组里面
     * @param urls 链接数组
     * @param url 链接
     * @return boolean
     */
    public static boolean isContain(String[] urls, String url) {
        if ("".equals(url) || urls == null || urls.length == 0) {
            return false;
        }
        for (String s : urls) {
            if(s.equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断链接是否在数组里面
     * @param urls 链接数组
     * @param url 链接
     * @return boolean
     */
    public static boolean isContain(List<String> urls, String url) {
        if ( urls == null || urls.size() == 0 || "".equals(url)) {
            return false;
        }
        for (String s : urls) {
            if(s.equals(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * url转id
     * @param url 链接
     * @return id
     */
    public static String urlToId(String url) {
        // 从url中截取id
        String[] split = url.trim().split(URL_SPLIT);
        String id = split[split.length - 1].substring(0, split[split.length - 1].indexOf(DOT));
        return id;
    }

}
