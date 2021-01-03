package com.ahuiali.word.common.constant;

import java.util.HashMap;
import java.util.Map;

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
     * 点学英语网站（爬取小说）
     *
     */
    public final static String BOOK_URL = "http://www.dian3x.com";
    public final static String CHAPTER_URL_PREFIX = "http://www.dian3x.com/story/book/";
    public final static String PARAGRAPH_URL_PREFIX = "http://www.dian3x.com/story/chapter/";

    /**
     * 中国日报网站
     */
    public final static String[] CHINA_DAILY_URLS =
            {
                    "https://www.chinadaily.com.cn/china/governmentandpolicy",
                    "https://www.chinadaily.com.cn/china/59b8d010a3108c54ed7dfc25",
                    "https://www.chinadaily.com.cn/china/society",
                    "https://www.chinadaily.com.cn/business/tech",
                    "https://www.chinadaily.com.cn/culture/books",
                    "https://www.chinadaily.com.cn/culture/art",
                    "https://www.chinadaily.com.cn/sports/soccer",
                    "https://www.chinadaily.com.cn/world/europe",
                    "https://www.chinadaily.com.cn/world/america",
                    "https://www.chinadaily.com.cn/travel/aroundworld"
            };

    /**
     * 中国日报网站
     */
    public static Map<String, String> getChinaDaily() {
        Map<String, String> CHINA_DAILY_URLS = new HashMap<>(16);
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/china/governmentandpolicy","国家");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/china/59b8d010a3108c54ed7dfc25","军事");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/china/society","社会");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/business/tech","科技");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/culture/books", "书籍");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/culture/art","艺术");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/sports/soccer","运动");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/world/europe","欧洲");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/world/america","美国");
        CHINA_DAILY_URLS.put("https://www.chinadaily.com.cn/travel/aroundworld","旅游");
        return CHINA_DAILY_URLS;
    }
}
