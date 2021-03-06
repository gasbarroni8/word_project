package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.enums.WordTypeChangeEnum;
import com.ahuiali.word.common.enums.WordTypeEnum;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.UpdateBaseDataUtil;
import com.ahuiali.word.dto.WordDto;
import com.ahuiali.word.mapper.WordMapper;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.service.WordService;
import com.ahuiali.word.common.utils.NextTimeUtils;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Transactional
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private UpdateBaseDataUtil updateBaseDataUtil;

    /**
     * 返回size个该词书的单词
     *
     * @param wordBookId 词书id
     * @param pageUtil   分页
     * @return resp
     */
    @Override
    public Response<?> getWords(int wordBookId, PageUtil pageUtil) {
        Response<List<Word>> response = Response.success();
        List<Word> words = wordMapper.getWords(wordBookId, pageUtil);
        if (words.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.WORDBOOK_EMPTY);
        }
        response.setData(words);
        return response;
    }

    /**
     * 获取不同类型单词，未背，记忆中，已掌握
     *
     * @param wordbookId 词书id
     * @param learnerId  用户id
     * @param pageUtil   分页
     * @param wordsType  单词类型
     * @return resp
     */
    @Override
    public Response<?> myWordbookWords(Integer wordbookId, Integer learnerId, PageUtil pageUtil, int wordsType) {
        Response<List<WordDto>> response = Response.success();
        List<WordDto> words = new ArrayList<>(pageUtil.getSize());
        //返回单词类型，1为未背，2为记忆中，3为已掌握
        if (wordsType == WordTypeEnum.UN_MEMORIZE.getType()) {
            words = wordMapper.getMyWordbookWords(wordbookId, learnerId, pageUtil);
        } else if (wordsType == WordTypeEnum.MEMORIZING.getType()) {
            words = wordMapper.findMemorizingWords(wordbookId, learnerId, pageUtil);
        } else if (wordsType == WordTypeEnum.MEMORIZED.getType()) {
            words = wordMapper.findMemorizdWords(wordbookId, learnerId, pageUtil);
        } else {
            return Response.result(Constant.Error.ARG_ERROR);
        }
        if (words.size() == Constant.ZERO) {
            return Response.result(Constant.Error.WORDBOOK_EMPTY);
        }
        response.setData(words);
        return response;
    }

    /**
     * 单词类型转移
     *
     * @param wordbookId 词书id
     * @param id         未背->掌握时id为words的id，其余为记忆表的id
     * @param type       记忆中->掌握 : 1, 未背->掌握 : 2, 掌握->未背 : 3
     * @param learnerId  用户id
     * @return resp
     */
    @Override
    public Response<?> wordTypeChange(Integer learnerId, Integer wordbookId, Integer id, int type) {
        Response<?> response = Response.success();
        if (type == WordTypeChangeEnum.MEMORIZING_TO_MEMORIZED.getType()) {
            //记忆中->掌握
            wordMapper.setWordIsMemorized(learnerId, wordbookId, id);
        } else if (type == WordTypeChangeEnum.UN_MEMORIZE_TO_MEMORIZED.getType()) {
            //未背->掌握，该id是words表的id
            wordMapper.addWordAndSetMemorized(learnerId, wordbookId, id);
            // 更新学习数量
            wordMapper.updateLearnCount(wordbookId, learnerId, 1);
        } else if (type == WordTypeChangeEnum.MEMORIZED_TO_UN_MEMORIZE.getType()) {
            //掌握->未背 重新学习
            wordMapper.removeMemorizeWord(learnerId, wordbookId, id);
        } else {
            response = Response.result(Constant.Error.ARG_ERROR);
        }
        return response;
    }

    /**
     * 获取需要复习的单词，30一组
     *
     * @param learnerId  用户id
     * @param wordbookId 词书id
     * @param pageUtil   分页
     * @return resp
     */
    @Override
    public Response<?> getReviewWords(Integer learnerId, Integer wordbookId, PageUtil pageUtil) {
        Response<List<Word>> response = Response.success();
        List<Word> words = wordMapper.getReviewWords(learnerId, wordbookId, pageUtil);
        if (words.size() <= 0) {
            return Response.result(Constant.Error.NO_REVIEW_WORD);
        }
        response.setData(words);
        return response;
    }

    /**
     * 批量插入新词(新词插入)
     *
     * @param wordbookId 词书id
     * @param learnerId  用户id
     * @param ids        批量
     * @return resp
     */
    @Override
    public Response<?> insertWords(Integer wordbookId, Integer learnerId, List<Long> ids) {

        Response<?> response = Response.success();
        StringBuilder sql = new StringBuilder();
        String next_time = NextTimeUtils.getNextTime(1, Calendar.getInstance());
        sql.append("insert into memorize(learner_id,wordbook_id,word_id,next_time) values ");
        for (Long id : ids) {
            sql.append("(").append(learnerId).append(",").append(wordbookId).append(",")
                    .append(id).append(",").append("\"").append(next_time).append("\"").append("),");
        }

        sql.setLength(sql.length() - 1);
        Integer count = wordMapper.insertWords(sql.toString());
        if (count > 0) {
            //更新学习数量
            wordMapper.updateLearnCount(wordbookId, learnerId, count);
            // 更新redis中用户的学习数据
            updateBaseDataUtil.addTodayLearnCount(count, learnerId);
        } else {
            response = Response.result(Constant.Error.WORD_BATCH_INSERT_ERROR);
        }
        return response;
    }

    /**
     * 更新
     *
     * @param idsList 单词 这里要wordId就行阿
     * @return
     */
    @Override
    public Response<?> updateWords(List<Long> idsList, Integer learnerId) {
        Response<?> response = Response.success();
        StringBuilder sql = new StringBuilder();
        //ids的
        StringBuilder ids = new StringBuilder();
        sql.append("UPDATE memorize SET next_time = CASE ")
                .append("WHEN memorized_count = 1 THEN ").append(" \"").append(NextTimeUtils.getNextTime(2, Calendar.getInstance())).append("\" ")
                .append("WHEN memorized_count = 2 THEN ").append(" \"").append(NextTimeUtils.getNextTime(3, Calendar.getInstance())).append("\" ")
                .append("WHEN memorized_count = 3 THEN ").append(" \"").append(NextTimeUtils.getNextTime(4, Calendar.getInstance())).append("\" ")
                .append("WHEN memorized_count = 4 THEN ").append(" \"").append(NextTimeUtils.getNextTime(5, Calendar.getInstance())).append("\" ")
                .append("WHEN memorized_count = 5 THEN ").append(" \"").append(NextTimeUtils.getNextTime(6, Calendar.getInstance())).append("\" ")
                .append("WHEN memorized_count = 6 THEN ").append(" \"").append(NextTimeUtils.getNextTime(7, Calendar.getInstance())).append("\" ")
                .append(" end,").append("memorized_count = memorized_count + 1 WHERE id IN (");
        for (Long id : idsList) {
            ids.append(id).append(",");
        }
        ids.setLength(ids.length() - 1);
        sql.append(ids.toString()).append(");");
        Integer count = wordMapper.updateReviewWords(sql.toString());

        //如果全部更新成功
        if (count != idsList.size()) {
            response = Response.result(Constant.Error.WORDBOOK_WORD_NOT_ALL_UPDATE);
        }
        // 更新redis中用户的学习数据
        updateBaseDataUtil.addTodayReviewCount(count, learnerId);
        return response;
    }


}
