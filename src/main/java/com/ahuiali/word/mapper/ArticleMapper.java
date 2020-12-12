package com.ahuiali.word.mapper;

import com.ahuiali.word.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * ArticleMapper
 * 文章mapper层
 * @author ZhengChaoHui
 * @date 2020/12/12 21:11
 */
@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
}
