package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.NextTimeUtils;
import com.ahuiali.word.common.utils.UpdateBaseDataUtil;
import com.ahuiali.word.dto.IdDto;
import com.ahuiali.word.dto.NoteBookWordDto;
import com.ahuiali.word.dto.NotebookDto;
import com.ahuiali.word.dto.WordBaseDto;
import com.ahuiali.word.mapper.NotebookMapper;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.service.NotebookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 生词本impl
 * @author ZhengChaoHui
 */
@Service
public class NotebookServiceImpl implements NotebookService {

    @Autowired
    NotebookMapper notebookMapper;

    @Autowired
    private UpdateBaseDataUtil updateBaseDataUtil;

    /**
     * 根据用户id查询所有生词本
     *
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> findAllNotebookByLearnerId(Integer learnerId) {
        Response<List<NotebookDto>> response = Response.success();
        //根据id查询所有生词本
        List<NotebookDto> notebooks = notebookMapper.findAllNotebookByLearnerId(learnerId);
        //大于0则返回
        if (notebooks.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.NOTEBOOK_EMPTY);
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
    public Response<?> addNotebook(NotebookDto notebook, Integer learnerId) {
        Response<Integer> response = Response.success();
        //返回影响条数
        int total = notebookMapper.addNotebook(notebook, learnerId);
        if (total <= Constant.ZERO) {
            response = Response.result(Constant.Error.NOTEBOOK_ADD_ERROR);
        }
        response.setData(notebook.getId());
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
     * 为生词本添加单词(弃用) --2020/12/28 暂不弃用
     *
     * @param notebookId 生词本id
     * @param word       单词
     * @return resp
     */
    @Override
    public Response<?> addWord(Integer notebookId, String word) {
        Response<IdDto> response = Response.success();
        IdDto idDto = new IdDto();
        // 这个id是
        notebookMapper.addWord(notebookId, word, idDto);
        if (idDto.getId() <= Constant.ZERO) {
            response = Response.result(Constant.Error.NOTEBOOK_WORD_ADD_ERROR);
            idDto.setId(Constant.ZERO);
        }
        //notebook中的count字段也要加一
        notebookMapper.notebookCountPlus(notebookId);
        response.setData(idDto);
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
        Response<List<WordBaseDto>> response = Response.success();
        //获取单词列表
        List<WordBaseDto> words = notebookMapper.listWords(notebookId, pageUtil);

        //不为空返回200
        if (words.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.NOTEBOOK_WORD_EMPTY);
        }
        response.setData(words);
        return response;
    }

    /**
     * 修改生词本名称
     *
     * @param name      生词本新名字
     * @param notebookId 生词本
     * @return resp
     */
    @Override
    public Response<?> editNotebook(String name, Integer notebookId) {
        Response<?> response = Response.success();
        Integer count = notebookMapper.editNotebookName(name, notebookId);
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

    /**
     * 批量更新
     * @param idsList
     * @return
     */
    @Override
    public Response<?> updateWords(List<Long> idsList, Integer learnerId) {
        Response<?> response = Response.success();
        StringBuilder sql = new StringBuilder();
        //ids的
        StringBuilder ids = new StringBuilder();
        sql.append("UPDATE notebook_word SET next_time = CASE ")
                .append("WHEN review_count = 1 THEN ").append(" \"").append(NextTimeUtils.getNextTime(2, Calendar.getInstance())).append("\" ")
                .append("WHEN review_count = 2 THEN ").append(" \"").append(NextTimeUtils.getNextTime(3, Calendar.getInstance())).append("\" ")
                .append("WHEN review_count = 3 THEN ").append(" \"").append(NextTimeUtils.getNextTime(4, Calendar.getInstance())).append("\" ")
                .append("WHEN review_count = 4 THEN ").append(" \"").append(NextTimeUtils.getNextTime(5, Calendar.getInstance())).append("\" ")
                .append("WHEN review_count = 5 THEN ").append(" \"").append(NextTimeUtils.getNextTime(6, Calendar.getInstance())).append("\" ")
                .append("WHEN review_count = 6 THEN ").append(" \"").append(NextTimeUtils.getNextTime(7, Calendar.getInstance())).append("\" ")
                .append(" end,").append("review_count = review_count + 1 WHERE id IN (");
        for (Long id : idsList) {
            ids.append(id).append(",");
        }
        ids.setLength(ids.length() - 1);
        sql.append(ids.toString()).append(");");
        Integer count = notebookMapper.updateReviewWords(sql.toString());
        // 更新redis中用户的学习数据
        updateBaseDataUtil.addTodayReviewCount(count, learnerId);
        //如果全部更新成功
        if (count != idsList.size()) {
            response = Response.result(Constant.Error.NOTEBOOK_WORD_NOT_ALL_UPDATE);
        }
        return response;
    }

    @Override
    public Response<?> getReviewWords(Integer notebookId, PageUtil pageUtil) {
        Response<List<NoteBookWordDto>> response = Response.success();
        List<NoteBookWordDto> words = notebookMapper.getReviewWords(notebookId, pageUtil);
        if (words.size() <= 0) {
            return Response.result(Constant.Error.NO_REVIEW_WORD);
        }
        response.setData(words);
        return response;
    }


}












