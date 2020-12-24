package com.ahuiali.word.mapper;

import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
}
