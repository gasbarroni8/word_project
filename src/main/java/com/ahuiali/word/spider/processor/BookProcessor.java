package com.ahuiali.word.spider.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * BookProcessor
 * 小说爬虫
 * @author ZhengChaoHui
 * @date 2021/1/2 22:37
 */
@Component
public class BookProcessor implements PageProcessor {

    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(50);

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
