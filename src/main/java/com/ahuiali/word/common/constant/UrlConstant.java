package com.ahuiali.word.common.constant;

/**
 * UrlConstant
 * 网站url
 * @author ZhengChaoHui
 * @date 2020/12/12 23:03
 */
public class UrlConstant {

    /**
     * cctv网站
     */
    public final static String CCTV_URL = "https://english.cctv.com/news/";
    public final static String CCTV_PAGE = "1";
    public final static String CCTV_PAGE_SIZE = "10";
    public final static String CCTV_ID = "PAGE1394789601117162";
    public final static String CCTV_REQUEST_LIST_URL = "https://api.cntv.cn/NewArticle/getArticleListByPageId?serviceId=pcenglish&id="
            + CCTV_ID + "&p=" + CCTV_PAGE + "&n=" + CCTV_PAGE_SIZE;

    /**
     * 中国日报网站
     */
    public final static String CHINA_DAILY_URL = "";
}
