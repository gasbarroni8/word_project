package com.ahuiali.word.spider.pipeline;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.common.utils.UrlUtil;
import com.ahuiali.word.pojo.Article;
import com.ahuiali.word.service.ArticleService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.selector.Html;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.ahuiali.word.common.constant.SpiderConstant.*;

/**
 * ChinaDailySqlPipeline
 * ChinaDaily持久化管道
 *
 * @author ZhengChaoHui
 * @date 2020/12/13 11:11
 */
@Component
@Slf4j
public class ChinaDailySqlPipeline implements Pipeline {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void process(ResultItems resultItems, Task task) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 列表
        if (LIST.equals(resultItems.get(KEY))) {
            List<String> articles = resultItems.get(ARTICLES);
            List<Article> articlesList = new ArrayList<>(20);
            List<String> unVisitedUrls = resultItems.get(UNVISITED_URL);

            articles.forEach(e -> {
                Article article = new Article();
                Html html = new Html(e);
                String url = HTTPS + html.xpath("//span[@class=\"tw3_01_2_t\"]/h4/a/@href").get();
                if (UrlUtil.isContain(unVisitedUrls, url)) {
                    String date = html.xpath("//span[@class=\"tw3_01_2_t\"]/b/text()").get();
                    article.setUrl(url);
                    article.setTitle(html.xpath("//span[@class=\"tw3_01_2_t\"]/h4/a/text()").get());
                    article.setId(UrlUtil.urlToId(url));
                    article.setSource(CHINA_DAILY);
                    article.setDescription(article.getTitle());
                    try {
                        article.setDate(simpleDateFormat.parse(date));
                    } catch (ParseException ex) {
                        log.error("文章日期转换失败！error:{}", ex.toString());
                    }
                    article.setImage(HTTPS + html.xpath("//span[@class=\"tw3_01_2_p\"]/a/img/@src").get());
                    articlesList.add(article);
                }
            });
//            articleService.saveOrUpdateBatch(articlesList);
//            redisTemplate.opsForList().leftPushAll(String.format(RedisKeyConstant.SPIDER_LINK_VISITED, CHINA_DAILY), unVisitedUrls);

        } else if (CONTENT.equals(resultItems.get(KEY))) {

        }
    }
}
