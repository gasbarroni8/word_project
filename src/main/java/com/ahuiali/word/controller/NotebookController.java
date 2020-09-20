package com.ahuiali.word.controller;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.service.NotebookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.Map;

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
     * @return
     */
    @RequestMapping(value = "/gotoNotebook")
    public String myNotebook(){
        return "/notebook/notebooks";
    }

    /**
     * 查询我的生词本
     * @return
     */
    @RequestMapping(value = "/myNotebookJson")
    public @ResponseBody Response<?> myNotebookJSON(HttpSession session){
        //获取学习者id
       Integer learner_id = (Integer) session.getAttribute("learnerId");
        return notebookService.findAllNotebookByLearnerId(learner_id);
    }

    /**
     * 修改生词本
     * @param session
     * @return
     */
    @RequestMapping(value = "/editNotebook" )
    public String editNotebook(HttpSession session,
                               @RequestParam(value = "notebookName",required = false) String name,
                               @RequestParam(value = "id",required = false) Integer id){
        notebookService.editNotebook(name.trim(),id);
        //重定向
        return "redirect:/notebook/gotoNotebook";
    }


    /**
     * 新增生词本
     * @param session
     * @return
     */
    @RequestMapping(value = "/addNotebook" )
    public String addNotebook(HttpSession session,
                              @RequestParam(value = "notebookName",required = false) String name){
        //获取学习者id
        Integer learnerId = (Integer) session.getAttribute("learnerId");
        Notebook notebook = new Notebook();
        notebook.setLearner_id(learnerId);
        notebook.setName(name.trim());
        notebookService.addNotebook(notebook);

        //前端自动判断，若添加成功，则在生词本列表追加，否则显示‘添加生词本失败信息’，code为601(*)

        //重定向
        return "redirect:/notebook/gotoNotebook";
    }

    /**
     * 删除生词本(*)
     * @param id 生词本id
     * @return
     */
    @RequestMapping(value = "/removeNotebook/{id}" ,produces = "application/json;charset=utf-8;")
    public String  removeNotebook(@PathVariable("id") Integer id){
        // TODO
        notebookService.removeNotebook(id);
        return "redirect:/notebook/gotoNotebook";
    }

    /**
     * 为生词本添加单词1
     * @param word  单词
     * @param id 生词本id
     * @return
     */
    @RequestMapping(value = "/addWord/{word}/{id}" ,produces = "application/json;charset=utf-8;")
    public @ResponseBody Response<?> addWord(@PathVariable("word") String  word,
                                          @PathVariable("id") Integer id){
        return notebookService.addWord(id,word);
    }

    /**
     * 为生词本添加单词2
     * @param wordect  单词实体
     * @param notebook_id 生词本id
     * @return
     */
    @RequestMapping(value = "/addWordEct/{notebook_id}" ,produces = "application/json;charset=utf-8;")
    public @ResponseBody Response<?> addWord(@RequestBody WordEct wordect,
                                          @PathVariable("notebook_id") Integer notebook_id){
        return notebookService.addWordEct(notebook_id,wordect);
    }

    /**
     * 从生词本中移除单词
     * @param id 生词本某单词主键id
     * @return
     */
    @RequestMapping(value = "/removeWord/{id}" ,produces = "application/json;charset=utf-8;")
    public @ResponseBody Response<?> removeWord(@PathVariable("id") Integer id){
        return notebookService.removeWord(id);
    }


    /**
     * 列出该生词本的单词（分页）
     * @param notebook_id
     * @param pageUtil
     * @return
     */
    @RequestMapping(value = "/listWords/{notebook_id}")
    public @ResponseBody Response<?> listWords(@PathVariable("notebook_id") Integer notebook_id, @RequestBody PageUtil pageUtil){
        pageUtil.renew();
        return notebookService.listWord(notebook_id,pageUtil);
    }

    /**
     * 跳转到生词本详细页面
     * @return
     */
    @RequestMapping(value = "/gotoNoteDetail")
    public String  gotoNoteDetail(){
        return "/notebook/notebookDetail";
    }



}
