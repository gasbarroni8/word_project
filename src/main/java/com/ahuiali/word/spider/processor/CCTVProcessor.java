package com.ahuiali.word.spider.processor;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.common.constant.UrlConstant;

import com.ahuiali.word.pojo.Article;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import java.util.ArrayList;
import java.util.List;

import static com.ahuiali.word.common.constant.SpiderConstant.*;


/**
 * CCTVProcessor
 * CCTV爬虫
 * @author ZhengChaoHui
 * @date 2020/12/12 22:20
 */
@Component
public class CCTVProcessor implements PageProcessor {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
     */
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(100);

    @Override
    public void process(Page page) {
        List<String> visitedUrls = redisTemplate.opsForList()
                .range(String.format(RedisKeyConstant.SPIDER_LINK_VISITED, CCTV), 0, -1);
        // 如果是主页
        if (UrlConstant.CCTV_REQUEST_LIST_URL.equals(page.getUrl().toString())) {
            // 找出json中的list
            JsonPathSelector jsonPathSelector = new JsonPathSelector("$.data.list[*]");
            List<String> list = jsonPathSelector.selectList(page.getJson().toString());
            List<Article> articles = new ArrayList<>(list.size());
            List<String> urls = new ArrayList<>(list.size());
            list.forEach(e -> {
                Article article = JSON.parseObject(e, Article.class);
                articles.add(article);
                // 只加入没有访问过的链接
                if (visitedUrls != null && !visitedUrls.contains(article.getUrl())) {
                    urls.add(article.getUrl());
                }
            });
            page.putField(ARTICLES, articles);
            page.putField(KEY, LIST);
            page.putField(UNVISITED_URL, urls);
            page.addTargetRequests(urls);
        } else {
            // 文章内容
            page.putField(KEY, CONTENT);
            List<String> paras = page.getHtml().xpath("//div[@id=\"content_area\"]/p/text()").all();
            page.putField(PARAGRAPH, paras);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
