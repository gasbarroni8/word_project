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
}
