package com.ahuiali.word.controller;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.PageUtil;
import com.ahuiali.word.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ArticleController
 * 文章控制器
 * @author ZhengChaoHui
 * @date 2020/12/24 13:10
 */
@Slf4j
@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 根据文章id获取文章内容
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/read/{id}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> read(@PathVariable("id") String articleId) {
        return articleService.getContentByArticleId(articleId);
    }

    @RequestMapping(value = "/tag/{tag}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getByTag(@PathVariable("tag") String tag, @RequestBody PageUtil pageUtil) {
        pageUtil.renew();
        return articleService.getByTag(tag, pageUtil);
    }
}
