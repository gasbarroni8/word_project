package com.ahuiali.word.controller;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.service.WordEctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author ZhengChaoHui
 * @Date 2020/6/3 10:35
 */
@Controller
@RequestMapping("/vue")
public class TestController {

    @Autowired
    WordEctDetailJson wordEctDetailJson;

    @Autowired
    WordEctService wordEctService;


    @RequestMapping(value = "/getWord/{word}",produces = "application/json;charset=utf-8;" )
    public @ResponseBody
    Response<?> demoVue(@PathVariable("word") String word,
                     HttpSession session){
        return wordEctService.findWordDetail(word, (Integer) session.getAttribute("learnerId"));
    }

    @RequestMapping("/test")
    public String gotoTestView(){
        return "testVue";
    }
}
