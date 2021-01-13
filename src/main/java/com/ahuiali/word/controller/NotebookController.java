package com.ahuiali.word.controller;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.NotebookDto;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.service.NotebookService;
import com.ahuiali.word.common.utils.PageUtil;
import com.ahuiali.word.vo.AddNotebookVo;
import com.ahuiali.word.vo.EditNotebookVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ahui
 */
@Controller
@RequestMapping("/notebook")
public class NotebookController {

    @Autowired
    NotebookService notebookService;

    /**
     * 跳转至我的生词本界面
     *
     * @return
     */
    @RequestMapping(value = "/gotoNotebook")
    public String myNotebook() {
        return "/notebook/notebooks";
    }

    /**
     * 查询我的生词本
     *
     * @return
     */
    @RequestMapping(value = "/myNotebook")
    public @ResponseBody
    Response<?> myNotebook(HttpSession session) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return notebookService.findAllNotebookByLearnerId(learnerId);
    }

    /**
     * 修改生词本
     *
     * @param editNotebookVo 新名称
     * @param notebookId 生词本id
     * @return
     */
    @RequestMapping(value = "/editNotebook/{notebookId}")
    public @ResponseBody Response<?> editNotebook(@Valid @RequestBody EditNotebookVo editNotebookVo,
                               @PathVariable(value = "notebookId", required = false) Integer notebookId) {

        //重定向
        return notebookService.editNotebook(editNotebookVo.getName().trim(), notebookId);
    }


    /**
     * 新增生词本
     *
     * @param session
     * @param addNotebookVo
     * @return
     */
    @RequestMapping(value = "/addNotebook")
    public @ResponseBody Response<?> addNotebook(HttpSession session,
                                                 @Valid @RequestBody AddNotebookVo addNotebookVo) {
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        NotebookDto notebookDto = new NotebookDto();
        notebookDto.setName(addNotebookVo.getName().trim());
        //前端自动判断，若添加成功，则在生词本列表追加，否则显示‘添加生词本失败信息’，code为601(*)
        //重定向
        return notebookService.addNotebook(notebookDto, learnerId);
    }

    /**
     * 删除生词本(*)
     *
     * @param id 生词本id
     * @return
     */
    @RequestMapping(value = "/removeNotebook/{id}", produces = "application/json;charset=utf-8;")
    public @ResponseBody Response<?> removeNotebook(@PathVariable("id") Integer id) {
        // TODO
        return notebookService.removeNotebook(id);
    }

    /**
     * 为生词本添加单词1
     *
     * @param word 单词
     * @param id   生词本id
     * @return
     */
    @RequestMapping(value = "/addWord/{word}/{id}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> addWord(@PathVariable("word") String word,
                        @PathVariable("id") Integer id) {
        return notebookService.addWord(id, word);
    }

    /**
     * 为生词本添加单词2
     *
     * @param wordect     单词实体
     * @param notebookId 生词本id
     * @return
     */
    @RequestMapping(value = "/addWordEct/{notebookId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> addWord(@RequestBody WordEct wordect,
                        @PathVariable("notebookId") Integer notebookId) {
        return notebookService.addWordEct(notebookId, wordect);
    }

    /**
     * 从生词本中移除单词
     *
     * @param id 生词本某单词主键id
     * @return
     */
    @RequestMapping(value = "/removeWord/{id}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> removeWord(@PathVariable("id") Integer id) {
        return notebookService.removeWord(id);
    }


    /**
     * 列出该生词本的单词（分页）
     *
     * @param notebookId
     * @param pageUtil
     * @return
     */
    @RequestMapping(value = "/listWords/{notebookId}")
    public @ResponseBody
    Response<?> listWords(@PathVariable("notebookId") Integer notebookId, @RequestBody PageUtil pageUtil) {
        pageUtil.renew();
        return notebookService.listWord(notebookId, pageUtil);
    }

    /**
     * 跳转到生词本详细页面
     *
     * @return
     */
    @RequestMapping(value = "/gotoNoteDetail")
    public String gotoNoteDetail() {
        return "/notebook/notebookDetail";
    }

    /**
     * 更新记忆表的单词
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/myNotebook/review", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> review(@RequestBody List<Long> ids) {
        return notebookService.updateWords(ids);
    }

    /**
     * 返回复习词汇
     *
     * @param notebookId
     * @param pageUtil
     * @param session
     * @return
     */
    @RequestMapping(value = "/getReviewWords/{notebookId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getReviewWords(@PathVariable("notebookId") Integer notebookId,
                               @RequestBody PageUtil pageUtil,
                               HttpSession session) {
        //获取所有需复习单词(分页)
        pageUtil.renew();
        return notebookService.getReviewWords(notebookId, pageUtil);
    }
}
