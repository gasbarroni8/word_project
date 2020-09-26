package com.ahuiali.word.controller;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.service.WordEctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 单词总表
 */
@Controller
@RequestMapping("/wordect")
public class WordEctController {

    @Autowired
    WordEctService wordEctService;

    @RequestMapping("")
    public String gotoDetail() {
        return "/word/wordDetail";
    }

    @RequestMapping("/gotoSearch")
    public String gotoSearch() {
        return "/word/search";
    }

    /**
     * 通过单词前缀来模糊查询单词，自动提示效果
     *
     * @param wordRre
     * @return
     */
    @RequestMapping(value = "/input/{word}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> input(@PathVariable("word") String wordRre) {
        return wordEctService.getWordsByPre(wordRre);
    }

    /**
     * 查询单词详细信息JSON
     *
     * @param word
     * @param session
     * @return
     */
    @RequestMapping(value = "/findWordDetailJson/{word}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> findWordDetailJSON(@PathVariable("word") String word, HttpSession session) {

        Integer learnerId = (Integer) session.getAttribute("learnerId");
        //这个是使用了redis，不过，我服务器老是被攻击，redis老是不行，暂时不用它了
        //但至少说明，这个是能用的，而且速度还不错，要怪就怪服务器不行
//        wordEctDetailJson = wordEctService.findWordDetail(word,learnerId);
        return wordEctService.findWordDetailNoRedis(word, learnerId);
    }


    /**
     * 查询单词详细信息页面
     *
     * @return
     */
    @RequestMapping(value = "/gotoWordDetail")
    public String findWordDetailString() {

        return "/word/detail";
    }

    /**
     * 普通查询，返回基本数据
     *
     * @param word 单词
     * @return
     */
    @RequestMapping(value = "/findWord/{word}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> findWord(@PathVariable("word") String word, HttpSession session) {
        Integer learnerId = (Integer) session.getAttribute("learnerId");
        //这个是使用了redis，不过，我服务器老是被攻击，redis老是不行，暂时不用它了
        //但至少说明，这个是能用的，而且速度还不错，要怪就怪服务器不行
//        wordEctJson = wordEctService.findWord(word,learnerId);
        return wordEctService.findWordNoRedis(word, learnerId);
    }

}
