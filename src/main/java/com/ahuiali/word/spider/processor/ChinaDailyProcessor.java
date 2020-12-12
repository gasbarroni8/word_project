package com.ahuiali.word.spider.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.UUID;

/**
 * ChinaDailyProcessor
 * 爬取中国日报英文版新闻
 * @author ZhengChaoHui
 * @date 2020/12/11 23:11
 */
public class ChinaDailyProcessor implements PageProcessor {

    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(100);

    /**
     * 获取站点page，并进行匹配
     *
     * @param page 页面
     */
    @Override
    public void process(Page page) {
        // 查询已爬取过的网页
        // 如果不是已经爬取的网页，则加入
        // 保存数据
        // 加入新的页面
    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
