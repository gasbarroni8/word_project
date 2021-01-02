package com.ahuiali.word.spider.processor;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.common.constant.UrlConstant;
import com.ahuiali.word.common.utils.UrlUtil;
import com.ahuiali.word.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.ahuiali.word.common.constant.SpiderConstant.*;
import static com.ahuiali.word.common.constant.UrlConstant.CHINA_DAILY_URLS;

/**
 * ChinaDailyProcessor
 * 爬取中国日报英文版新闻
 * @author ZhengChaoHui
 * @date 2020/12/11 23:11
 */
@Component
public class ChinaDailyProcessor implements PageProcessor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(50);

    /**
     * 获取站点page，并进行匹配
     *
     * @param page 页面
     */
    @Override
    public void process(Page page) {
        List<String> visitedUrls = redisTemplate.opsForList()
                .range(String.format(RedisKeyConstant.SPIDER_LINK_VISITED, CHINA_DAILY), 0, -1);
        String url = page.getUrl().toString();
        // 列表
        if (UrlUtil.isContain(CHINA_DAILY_URLS, url)) {
            List<String> listUrls = page.getHtml().xpath("//*span[@class=\"tw3_01_2_t\"]/h4/a/@href").all();
            List<String> articles = page.getHtml().xpath("//div[@class=\"mb10 tw3_01_2\"]").all();
            List<String> newUrls = new ArrayList<>(listUrls.size()) ;
            page.putField(ARTICLES, articles);
            page.putField(KEY, LIST);
            listUrls.forEach(e -> {
                if (visitedUrls != null && !visitedUrls.contains(e)) {
                    newUrls.add(HTTPS + e);
                }
            });
            page.putField(UNVISITED_URL, newUrls);
            page.putField(TAG, UrlConstant.getChinaDaily().get(url));
            // 加入新链接
            page.addTargetRequests(newUrls);
        } else {
            // TODO 分页爬虫问题暂不弄
            // 内容
            List<String> contents = page.getHtml().xpath("//div[@id=\"content\"]/p/text()").all();
            page.putField(KEY, CONTENT);
            page.putField(PARAGRAPH, contents);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }

    public static void main(String[] args) {
        String date = "2020-12-12 14:26";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date1 = simpleDateFormat.parse(date);
            System.out.println(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
