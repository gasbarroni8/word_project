package com.ahuiali.word.spider;

import com.ahuiali.word.spider.processor.ChinaDailyProcessor;
import us.codecraft.webmagic.Spider;

/**
 * SpiderLaunch
 * 爬虫启动类
 * @author ZhengChaoHui
 * @date 2020/12/11 23:16
 */
public class SpiderLaunch {

    /**
     * 爬取中国日报英文版新闻
     */
    public static void startSpiderChinaDaily() {
        // 异步启动
        Spider.create(new ChinaDailyProcessor())
                .addUrl("")
                .addPipeline(null)
                .thread(5)
                .runAsync();
    }
}
