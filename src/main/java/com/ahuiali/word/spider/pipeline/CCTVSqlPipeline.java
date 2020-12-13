package com.ahuiali.word.spider.pipeline;

import com.ahuiali.word.common.constant.RedisKeyConstant;
import com.ahuiali.word.mapper.ArticleMapper;
import com.ahuiali.word.pojo.Article;
import com.ahuiali.word.pojo.ArticleParagraph;
import com.ahuiali.word.service.ArticleParagraphService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;


import static com.ahuiali.word.common.constant.SpiderConstant.*;

/**
 * CCTVSqlPipeline
 * cctv持久化管道
 *
 * @author ZhengChaoHui
 * @date 2020/12/12 23:06
 */
@Component
@Slf4j
public class CCTVSqlPipeline implements Pipeline {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleParagraphService articleParagraphService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public void process(ResultItems resultItems, Task task) {
        if (LIST.equals(resultItems.get(KEY))) {
            List<Article> articles = resultItems.get(ARTICLES);
            List<String> unVisitedUrls = resultItems.get(UNVISITED_URL);
            articles.forEach(e -> {
                if (unVisitedUrls.contains(e.getUrl())) {
                    articleMapper.insert(e);
                    redisTemplate.opsForList()
                            .leftPush(String.format(RedisKeyConstant.SPIDER_LINK_VISITED, CCTV), e.getUrl());
                }
            });
        } else if (CONTENT.equals(resultItems.get(KEY))) {
            // 从url中截取id
            String[] split = resultItems.getRequest().getUrl().trim().split(URL_SPLIT);
            String id = split[split.length - 1].substring(0, split[split.length - 1].indexOf(DOT));
            // 文章内容
            List<String> paragraphs = resultItems.get(PARAGRAPH);
            List<ArticleParagraph> articleParagraphs = new ArrayList<>(paragraphs.size());
            int no = 0;
            for (String paragraph : paragraphs) {
                if ("".equals(paragraph)) {
                    continue;
                }
                no++;
                ArticleParagraph articleParagraph = new ArticleParagraph();
                articleParagraph.setNo(no);
                articleParagraph.setPara(paragraph);
                articleParagraph.setArticleId(id);
                articleParagraphs.add(articleParagraph);
            }
            articleParagraphService.saveOrUpdateBatch(articleParagraphs);
        }

    }
}
