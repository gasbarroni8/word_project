package com.ahuiali.word.service.impl;

import com.ahuiali.word.mapper.ArticleMapper;
import com.ahuiali.word.pojo.Article;
import com.ahuiali.word.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ArticleServiceImpl
 * 文章业务层实现类
 * @author ZhengChaoHui
 * @date 2020/12/12 21:10
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
}
