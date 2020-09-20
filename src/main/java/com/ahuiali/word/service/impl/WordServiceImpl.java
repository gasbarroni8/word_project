package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
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
    WordMapper wordMapper;

    @Override
    public Response<?> getWords(int id, PageUtil pageUtil) {
        Response<List<Word>> response = Response.success();
        List<Word> words = wordMapper.getWords(id,pageUtil);
        if(words.size() <= 0){
            return Response.result(Constant.Error.WORDBOOK_EMPTY);
        }
        response.setData(words);
        return response;
    }

    /**
     * 获取不同类型单词，未背，记忆中，已掌握
     * @param wordbook_id
     * @param learnerId
     * @param pageUtil
     * @param wordsType
     * @return
     */
    @Override
    public Response<?> myWordbookWords(Integer wordbook_id, Integer learnerId, PageUtil pageUtil, Integer wordsType) {
        Response<List<Word>> response = Response.success();
        List<Word> words = new ArrayList<>(pageUtil.getSize());
        //返回单词类型，1为未背，2为记忆中，3为已掌握
        if(wordsType == 1){
            words = wordMapper.getMyWordbookWords(wordbook_id,learnerId,pageUtil);
        } else if(wordsType == 2){
            words = wordMapper.findMemorizingWords(wordbook_id,learnerId,pageUtil);
        } else if(wordsType == 3){
            words = wordMapper.findMemorizdWords(wordbook_id,learnerId,pageUtil);
        } else{
            return Response.result(Constant.Error.ARG_ERROR);
        }
        if(words.size() == 0){
            return Response.result(Constant.Error.WORDBOOK_EMPTY);
        }
        response.setData(words);
        return response;
    }

    /**
     *  单词类型转移
     *
     * @param wordbook_id
     * @param id 未背->掌握时id为words的id，其余为记忆表的id
     * @param type 记忆中->掌握 : 1, 未背->掌握 : 2, 掌握->未背 : 3
     * @param learner_id 用户id
     * @return
     */
    @Override
    public Response<?> wordTypeChange(Integer learner_id, Integer wordbook_id, Integer id, Integer type) {
        Response<?> response = Response.success();
        if(type == 1){
            //记忆中->掌握
            wordMapper.setWordIsMemorized(id);
        } else if(type == 2){
            //未背->掌握，该id是words表的id
            wordMapper.addWordAndSetMemorized(learner_id,wordbook_id,id);
        } else if(type == 3){
            //掌握->未背 重新学习
            wordMapper.removeMemorizeWord(id);
        } else{
            response = Response.result(Constant.Error.ARG_ERROR);
        }
        return response;
    }

    /**
     * 获取需要复习的单词，30一组
     * @param learner_id
     * @param wordbook_id
     * @param pageUtil
     * @return
     */
    @Override
    public Response<?> getReviewWords(Integer learner_id, Integer wordbook_id, PageUtil pageUtil) {
        Response<List<Word>> response = Response.success();
        List<Word> words = wordMapper.getReviewWords(learner_id,wordbook_id,pageUtil);
        if(words.size() <= 0){
           return Response.result(Constant.Error.NO_REVIEW_WORD);
        }
        response.setData(words);
        return response;
    }
    /**
     * 批量插入新词
     * @param wordbook_id
     * @param learner_id
     * @param ids
     * @return
     */
    @Override
    public Response<?> insertWords(Integer wordbook_id, Integer learner_id, List<Long> ids) {
        Response<?> response = Response.success();
        StringBuilder sql = new StringBuilder();
        String next_time = NextTimeUtils.getNextTime(1, Calendar.getInstance());
        sql.append("insert into memorize(learner_id,wordbook_id,word_id,next_time,created,modified) values ");
        for(Long id : ids){
            sql.append("(").append(learner_id).append(",").append(wordbook_id).append(",")
                    .append(id).append(",").append("\"").append(next_time).append("\"").append(",")
                    .append("NOW(),NOW()").append("),");
        }

        sql.setLength(sql.length() - 1);
        Integer count = wordMapper.insertWords(sql.toString());
        if(count > 0){
            //更新学习数量
            wordMapper.updateLearnCount(wordbook_id,learner_id,count);
        }else {
            response = Response.result(Constant.Error.WORD_BATCH_INSERT_ERROR);
        }
        return response;
    }

    /**
     * 更新
     * @param words
     * @return
     */
    @Override
    public Response<?> updateWords(List<Word> words) {
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
        for(Word word : words){
            ids.append(word.getId()).append(",");
        }
        ids.setLength(ids.length() - 1);
        sql.append(ids.toString()).append(");");
        Integer count = wordMapper.updateReviewWords(sql.toString());

        //如果全部更新成功
        if(count != words.size()){
            response = Response.result(Constant.Error.WORDBOOK_WORD_NOT_ALL_UPDATE);
        }
        return response;
    }


}
