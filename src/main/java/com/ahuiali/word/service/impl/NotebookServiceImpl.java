package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.mapper.NotebookMapper;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.service.NotebookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhengChaoHui
 */
@Service
public class NotebookServiceImpl implements NotebookService {

    @Autowired
    NotebookMapper notebookMapper;

    /**
     * 根据用户id查询所有生词本
     *
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> findAllNotebookByLearnerId(Integer learnerId) {
        Response<List<Notebook>> response = Response.success();
        //根据id查询所有生词本
        List<Notebook> notebooks = notebookMapper.findAllNotebookByLearnerId(learnerId);
        //大于0则返回
        if (notebooks.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.NOTEBOOK_ADD_ERROR);
        }
        response.setData(notebooks);
        return response;
    }

    /**
     * 添加生词本
     *
     * @param notebook 生词本
     * @return resp
     */
    @Override
    public Response<?> addNotebook(Notebook notebook) {
        Response<?> response = Response.success();
        //返回影响条数
        int total = notebookMapper.addNotebook(notebook);
        if (total <= Constant.ZERO) {
            response = Response.result(Constant.Error.NOTEBOOK_ADD_ERROR);
        }
        return response;
    }

    /**
     * 根据生词本id删除生词本
     * 开启事务
     *
     * @param id 生词本id
     * @return resp
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<?> removeNotebook(Integer id) {
        Response<?> response = Response.success();
        int total = notebookMapper.removeNotebook(id);
        if (total > Constant.ZERO) {
            //删除生词本成功
            //删除生词本的所有单词
            // TODO 之后要用队列
            Integer total1 = notebookMapper.removeAllNotebookWords(id);
        } else {
            //小于0说明删除失败
            response = Response.result(Constant.Error.NOTEBOOK_DELETE_ERROR);
        }
        return response;
    }

    /**
     * 删除生词本的某个单词
     *
     * @param id 生词本-单词表的id
     * @return resp
     */
    @Override
    public Response<?> removeWord(Integer id) {
        Response<?> response = Response.success();
        Integer notebook_id = notebookMapper.findIdByNotebookWordId(id);
        Integer total = notebookMapper.removeWord(id);
        if (total > Constant.ZERO) {
            //大于0说明删除成功
            //生词本单词数量减一
            notebookMapper.notebookCountMinus(notebook_id);
        } else {
            //小于0说明删除失败
            response = Response.result(Constant.Error.NOTEBOOK_WORD_DELETE_ERROR);
        }
        return response;
    }

    /**
     * 为生词本添加单词(弃用)
     *
     * @param notebookId 生词本id
     * @param word       单词
     * @return resp
     */
    @Override
    public Response<?> addWord(Integer notebookId, String word) {
        Response<?> response = Response.success();
        Integer total = notebookMapper.addWord(notebookId, word);
        if (total <= Constant.ZERO) {
            response = Response.result(Constant.Error.NOTEBOOK_WORD_ADD_ERROR);
        }
        return response;
    }

    /**
     * 查询某生词本的单词，分页
     *
     * @param notebookId 生词本id
     * @param pageUtil   分页
     * @return resp
     */
    @Override
    public Response<?> listWord(Integer notebookId, PageUtil pageUtil) {
        Response<Notebook> response = Response.success();
        //获取单词列表
        List<Word> words = notebookMapper.listWords(notebookId, pageUtil);
        Notebook notebook = new Notebook();
        notebook.setWords(words);
        //不为空返回200
        if (words.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.NOTEBOOK_WORD_EMPTY);
        }
        response.setData(notebook);
        return response;
    }

    /**
     * 修改生词本名称
     *
     * @param name      生词本新名字
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> editNotebook(String name, Integer learnerId) {
        Response<?> response = Response.success();
        Integer count = notebookMapper.editNotebookName(name, learnerId);
        if (count <= Constant.ZERO) {
            response = Response.result(Constant.Error.NOTEBOOK_UPDATE_ERROR);
        }
        return response;
    }

    /**
     * 为生词本添加单词（能返回主键）
     *
     * @param notebookId 生词本id
     * @param wordect    单词实体
     * @return wordctJson
     */
    @Override
    public Response<?> addWordEct(Integer notebookId, WordEct wordect) {
        Response<WordEct> response = Response.success();
        //增加
        notebookMapper.addWordEct(wordect, notebookId);
        if (wordect.getId() != null) {
            //notebook中的count字段也要加一
            notebookMapper.notebookCountPlus(notebookId);
            response.setData(wordect);
        }
        // TODO 失败没做处理
        return response;
    }


}












