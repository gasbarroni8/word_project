package com.ahuiali.word.spider.processor;

import com.ahuiali.word.common.constant.UrlConstant;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ahuiali.word.common.constant.SpiderConstant.*;

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
    private final static Site SITE = Site.me().setRetryTimes(3).setSleepTime(100)
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:74.0) Gecko/20100101 Firefox/74.0");

    private Map<String, Integer> bookChapter = new HashMap<>(300);
    private Map<String, Integer> chapterPara = new HashMap<>(3000);


    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();
        System.out.println(url);
        if (UrlConstant.BOOK_URL.equals(url)) {
            // 处理书籍
            List<String> urls = page.getHtml().xpath("//a[@class=\"book\"]/@href").all();
            List<String> titles = page.getHtml().xpath("//h2[@class=\"book_title\"]/text()").all();
            List<String> authors = page.getHtml().xpath("//div[@class=\"book_prop\"]/span[2]/text()").all();
//            List<String> books = page.getHtml().xpath("//a[@class=\"book\"]").all();
            List<String> tags = page.getHtml().xpath("//div[@class=\"book_tags\"]/text()").all();
            List<String> summaries = page.getHtml().xpath("//div[@class=\"book_summary\"]/text()").all();
            List<String> src = page.getHtml().xpath("//img[@class=\"book_img\"]/@src").all();
            List<String> tsrc = page.getHtml().xpath("//img[@class=\"book_img\"]/@tsrc").all();
            List<String> images = new ArrayList<>(urls.size());
            // 因为它的小说图片是懒加载，所以前五个图片是src里面，后面的都是tsrc
            for (int i = 0; i < urls.size(); i++) {
                if (i < 5) {
                    images.add(src.get(i));
                } else {
                    images.add(tsrc.get(i));
                }
            }
            page.putField(AUTHOR, authors);
            page.putField(TITLES, titles);
            page.putField(TAG, tags);
            page.putField(KEY, BOOK);
            page.putField(IMAGES, images);
            page.putField(SUMMARY, summaries);
//            分类：社会,        难度：大学,        长度：短篇
            // 1. 去除空格
            // 2. ','分隔
            // 3. '：'分隔，获取第二个
            // 最终获取社会 大学 短篇 分别加入到不同字段
            List<String> newUrls = new ArrayList<>(urls.size());
            for (int i = 0; i < titles.size(); i++) {
                String temp = UrlConstant.BOOK_URL + urls.get(i);
                newUrls.add(temp);
                bookChapter.put(temp, i + 1);
            }
            page.putField(URLS, newUrls);
            page.addTargetRequests(newUrls);
        } else if (url.startsWith(UrlConstant.CHAPTER_URL_PREFIX)) {
            // 处理章节
            // 获取该书籍的所有章节url
            List<String> urls = page.getHtml().xpath("//a[@class=\"chapter_item\"]/@href").all();
            // 获取该书籍所有章节名称
            List<String> chapterName = page.getHtml().xpath("//a[@class=\"chapter_item\"]/text()").all();
            page.putField("bookIndex", bookChapter.get(url));
            page.putField("chapterName", chapterName);
            page.putField(KEY, CHAPTER);
            List<String> newUrls = new ArrayList<>(urls.size());
            for (int i = 0; i < chapterName.size(); i++) {
                String temp = UrlConstant.BOOK_URL + "/story" + urls.get(i).substring(2);
                newUrls.add(temp);
                chapterPara.put(temp, bookChapter.get(url) * 10000 + (i + 1));
            }
            page.addTargetRequests(newUrls);

        } else if (url.startsWith(UrlConstant.PARAGRAPH_URL_PREFIX)) {
            // 处理段落
            List<String> ens = page.getHtml().xpath("//div[@class=\"line_en\"]/text()").all();
            List<String> cns = page.getHtml().xpath("//div[@class=\"line_cn\"]/@title").all();
            page.putField(ENS, ens);
            page.putField(CNS, cns);
            page.putField(CHAPTER_INDEX, chapterPara.get(url));
            page.putField(KEY, PARAGRAPH);
        }
    }

    @Override
    public Site getSite() {
        return SITE;
    }
}
