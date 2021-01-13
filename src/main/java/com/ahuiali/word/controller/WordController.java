package com.ahuiali.word.controller;


import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.service.WordService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 单词控制器
 * 注意： 背词模块的很多功能都放到WordBook模块了
 * Created by zhengchaohui on 2019/9/18
 */
@Controller
@RequestMapping("/words")
public class WordController {

    @Autowired
    WordService wordService;

    @RequestMapping(value = "/getWords/", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getWords(@RequestBody PageUtil pageUtil) {
        return wordService.getWords(1, pageUtil);
    }

    @RequestMapping(value = {"/goto"})
    public String gotoMemorize() {
        return "/word/memorize";
    }

    /**
     * 返回复习词汇
     *
     * @param wordbookId
     * @param pageUtil
     * @param session
     * @return
     */
    @RequestMapping(value = "/getReviewWords/{wordbookId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getReviewWords(@PathVariable("wordbookId") Integer wordbookId,
                               @RequestBody PageUtil pageUtil,
                               HttpSession session) {
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        //获取所有需复习单词(分页)
        pageUtil.renew();
        return wordService.getReviewWords(learnerId, wordbookId, pageUtil);
    }

}
