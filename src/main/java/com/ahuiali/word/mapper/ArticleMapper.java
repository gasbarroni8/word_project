package com.ahuiali.word.mapper;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.PageUtil;
import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.dto.ArticleParaDto;
import com.ahuiali.word.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ArticleMapper
 * 文章mapper层
 * @author ZhengChaoHui
 * @date 2020/12/12 21:11
 */
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("SELECT id, title, source, url, description, date, image FROM article ORDER BY date DESC LIMIT 15;")
    List<ArticleDto> getLastestArticle();

    /**
     * 获取文章内容
     * @param articleId 文章id
     * @return ArticleParaDto
     */
    @Select("SELECT id, title, source, url, description, date, image FROM article WHERE id = #{articleId}")
    @Results({
            @Result(id = true, property = "article.id", column = "id"),
            @Result(property = "article.title", column = "title"),
            @Result(property = "article.description", column = "description"),
            @Result(property = "article.url", column = "url"),
            @Result(property = "article.image", column = "image"),
            @Result(property = "article.source", column = "source"),
            @Result(property = "article.date", column = "date"),
            @Result(property = "paragraphs", column = "id", javaType = List.class,
                    many = @Many(select = "com.ahuiali.word.mapper.ArticleMapper.getContentsByArticleId"))
    })
    ArticleParaDto getContentByArticleId(String articleId);

    @Select("SELECT DISTINCT(para), no FROM article_paragraph WHERE article_id = #{articleId} ORDER BY no ASC;")
    List<String> getContentsByArticleId(String articleId);

    @Select("SELECT id, title, source, url, description, date, image FROM article WHERE tag = #{tag} ORDER BY date DESC LIMIT #{pageUtil.offset},#{pageUtil.size};")
    List<ArticleDto> getByTag(String tag, PageUtil pageUtil);
}
