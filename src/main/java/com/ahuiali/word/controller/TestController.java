package com.ahuiali.word.controller;

import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.mapper.WordEctMapper;
import com.ahuiali.word.pojo.WordEctDetail;
import com.ahuiali.word.service.WordEctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
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
    public @ResponseBody WordEctDetailJson demoVue(@PathVariable("word") String word,
                                                   HttpSession session){
        wordEctDetailJson = wordEctService.findWordDetail(word, (Integer) session.getAttribute("learnerId"));
        return wordEctDetailJson;
    }

    @RequestMapping("/test")
    public String gotoTestView(){
        return "testVue";
    }
}
