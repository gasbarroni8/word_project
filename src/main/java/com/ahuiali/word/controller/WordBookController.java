package com.ahuiali.word.controller;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;

import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.service.WordService;
import com.ahuiali.word.service.WordbookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 词书控制器
 */
@Controller
@RequestMapping("/wordbook")
public class WordBookController {

    @Autowired
    WordbookService wordbookService;

    @Autowired
    WordService wordService;

    //跳转至词库
    @RequestMapping("/gotoWordbook")
    public String gotoWordbook() {
        return "/wordbook/wordbooks";
    }

    //跳转至词书页面
    @RequestMapping("/gotoWordbookDetail")
    public String gotoWordbookDetail() {

        return "/wordbook/wordbookDetail";
    }

    //跳转到我的词书的页面
    @RequestMapping("/gotoMyWordbooks")
    public String gotoMyWordbooks() {

        return "/wordbook/myWordbooks";
    }

    //跳转到我的某个词书的页面
    @RequestMapping("/gotoMyWordbookDetail")
    public String gotoMyWordbookDetail() {

        return "/wordbook/myWordbookDetail";
    }

    //跳转到复习页面
    @RequestMapping("/gotoReview")
    public String gotoReview() {

        return "/word/review";
    }

    /**
     * 获取所有词书
     *
     * @return
     */
    @RequestMapping(value = "/all", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getWordbooks() {
        return wordbookService.getWordbooks();
    }

    /**
     * 根据词书id返回细节
     *
     * @param id
     * @param
     * @return
     */
    @RequestMapping(value = "/detail/{id}")
    public @ResponseBody
    Response<?> getWordbookDetail(@PathVariable("id") String id,
                                  HttpSession session) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute("learnerId");
        return wordbookService.getWordbookDetail(Integer.parseInt(id), learnerId);
    }

    /**
     * 返回size个该词书的单词
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/showWords/{id}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> showWords(@PathVariable("id") Integer id,
                          @RequestBody PageUtil pageUtil) {
        //刷新offset
        pageUtil.renew();
        return wordService.getWords(id, pageUtil);
    }

    /**
     * 为用户添加词书，并将其设置为当前词书
     *
     * @param wordbookId
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/addWordbook/{wordbookId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> addWordbook(@PathVariable("wordbookId") Integer wordbookId, HttpSession session) throws Exception {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return wordbookService.addWordbook(learnerId, wordbookId);
    }

    /**
     * 修改当前计划
     *
     * @param wordbookId
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/changeWordbook/{wordbookId}")
    public @ResponseBody
    Response<?> changeWordbook(@PathVariable("wordbookId") Integer wordbookId, HttpSession session) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        // TODO 这里要加个判断

        return wordbookService.updateWordbookPlan(learnerId, wordbookId);
    }

    /**
     * 查询我的词书
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/myWordbooks", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> myWordbooks(HttpSession session) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        //session中保存我的词书
        return wordbookService.findMyWordbooks(learnerId);
    }


    /**
     * 移除我的词书计划
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/remove", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> removeWordbooks(HttpSession session) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        //session中保存我的词书
        return wordbookService.removePlan(learnerId);
    }

    /**
     * 我的词书的单词类型
     *
     * @param wordbookId 词书id
     * @param wordsType  返回单词类型，1为未背，2为记忆中，3为已掌握
     * @param pageUtil   分页类
     * @param session    用于获取learnerId
     * @return
     */
    @RequestMapping(value = "/myWordbook/words/{wordbookId}/{wordsType}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> myWordbookWords(
            @PathVariable("wordbookId") Integer wordbookId,
            @PathVariable("wordsType") Integer wordsType,
            @RequestBody PageUtil pageUtil,
            HttpSession session) {
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        pageUtil.renew();
        return wordService.myWordbookWords(wordbookId, learnerId, pageUtil, wordsType);
    }

    /**
     * 单词类型转移
     *
     * @param wordbookId
     * @param id         word_id
     * @param type       记忆中->掌握 : 1, 未背->掌握 : 2, 掌握->未背 : 3
     * @param session
     * @return
     */
    @RequestMapping("/myWordbook/wordTypeChange/{wordbookId}/{id}/{type}")
    public @ResponseBody
    Response<?> wordTypeChange(@PathVariable("wordbookId") Integer wordbookId,
                               @PathVariable("id") Integer id,
                               @PathVariable("type") Integer type,
                               HttpSession session) {
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return wordService.wordTypeChange(learnerId, wordbookId, id, type);
    }


    /**
     * 将新词加入记忆表中
     *
     * @param wordbookId
     * @param session
     * @return
     */
    @RequestMapping(value = "/myWordbook/insert/{wordbookId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> insert(@PathVariable("wordbookId") Integer wordbookId,
                       @RequestBody List<Long> ids,
                       HttpSession session) {
        Integer learner_id = (Integer) session.getAttribute("learnerId");
        return wordService.insertWords(wordbookId, learner_id, ids);
    }

    /**
     * 更新记忆表的单词
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/myWordbook/review", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> review(@RequestBody List<Long> ids) {
        return wordService.updateWords(ids);
    }


}
















