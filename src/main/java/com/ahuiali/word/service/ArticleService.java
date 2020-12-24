package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ArticleService
 * 文章业务接口
 * @author ZhengChaoHui
 * @date 2020/12/12 21:10
 */
@Service
public interface ArticleService extends IService<Article> {

    /**
     * 获取最新文章
     * @return
     */
    List<ArticleDto> getLastestArticle();


}
