package com.ahuiali.word.controller;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.service.WordbookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/")
public class BaseController {


    @Autowired
    WordbookService wordbookService;

    /**
     * 主页
     * @return
     */
    @RequestMapping("/")
    public String gotoIndex(){
        return "/index";
    }

    /**
     * 主页初始化
     * 查询我当前记忆的词书
     * @param session
     * @return
     */
    @RequestMapping(value = "/index", produces = "application/json;charset=utf-8;" )
    public @ResponseBody Response<?> indexInit(HttpSession session){
        //获取学习者id
       Integer learnerId = (Integer) session.getAttribute("learnerId");
       log.info("用户登入，跳转至index页面，learnerId：{}", learnerId);
       return wordbookService.getMemorizingWordbookAndReviewCount(learnerId);
    }
}
