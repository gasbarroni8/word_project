package com.ahuiali.word.spider;

import com.ahuiali.word.common.constant.UrlConstant;
import com.ahuiali.word.spider.pipeline.CCTVSqlPipeline;
import com.ahuiali.word.spider.pipeline.ChinaDailySqlPipeline;
import com.ahuiali.word.spider.processor.CCTVProcessor;
import com.ahuiali.word.spider.processor.ChinaDailyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import static com.ahuiali.word.common.constant.UrlConstant.CHINA_DAILY_URLS;

/**
 * SpiderLaunch
 * 爬虫启动类
 * @author ZhengChaoHui
 * @date 2020/12/11 23:16
 */
@Component
public class SpiderLaunch {

    @Autowired
    private CCTVSqlPipeline cctvSqlPipeline;

    @Autowired
    private ChinaDailySqlPipeline chinaDailySqlPipeline;

    @Autowired
    private CCTVProcessor cctvProcessor;

    @Autowired
    private ChinaDailyProcessor chinaDailyProcessor;

    /**
     * 爬取中国日报英文版新闻
     */
    public void startSpiderChinaDaily() {
//        for (String chinaDailyUrl : CHINA_DAILY_URLS) {
//            // 异步启动
//            Spider.create(chinaDailyProcessor)
//                    .addUrl(chinaDailyUrl)
//                    .addPipeline(chinaDailySqlPipeline)
//                    .thread(5)
//                    .run();
//        }

        // 测试
        Spider.create(chinaDailyProcessor)
                .addUrl("https://www.chinadaily.com.cn/china/governmentandpolicy")
                .addPipeline(chinaDailySqlPipeline)
                .thread(5)
                .run();

    }

    /**
     * 爬取CCTV英文版新闻
     */
    public void startSpiderCCTV() {
        // 异步启动
        Spider.create(cctvProcessor)
                .addUrl(UrlConstant.CCTV_REQUEST_LIST_URL)
                .addPipeline(cctvSqlPipeline)
                .thread(5)
                .runAsync();
    }

    public static void main(String[] args) {
//        startSpiderCCTV();
    }
}
