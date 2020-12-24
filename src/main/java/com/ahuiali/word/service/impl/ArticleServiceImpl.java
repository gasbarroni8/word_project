package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.dto.ArticleParaDto;
import com.ahuiali.word.mapper.ArticleMapper;
import com.ahuiali.word.pojo.Article;
import com.ahuiali.word.service.ArticleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ArticleServiceImpl
 * 文章业务层实现类
 * @author ZhengChaoHui
 * @date 2020/12/12 21:10
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleDto> getLastestArticle() {
        return articleMapper.getLastestArticle();
    }

    @Override
    public Response<?> getContentByArticleId(String articleId) {
        Response<ArticleParaDto> response = Response.success();
        ArticleParaDto paraDto = articleMapper.getContentByArticleId(articleId);
//        ArticleParaDto articleParaDto = new ArticleParaDto();
//        articleParaDto.setParagraphs(paragraphs);
//        if (paragraphs != null && paragraphs.size() > 0) {
//            response.setData(articleParaDto);
//        }
        response.setData(paraDto);
        return response;
    }
}
