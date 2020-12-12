package com.ahuiali.word.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;


/**
 * CCTVProcessor
 * CCTV爬虫
 * @author ZhengChaoHui
 * @date 2020/12/12 22:20
 */
public class CCTVProcessor implements PageProcessor {

    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
