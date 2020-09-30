package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.mapper.WordMapper;
import com.ahuiali.word.mapper.WordbookMapper;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.Wordbook;
import com.ahuiali.word.service.WordbookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@Slf4j
public class WordbookServiceImpl implements WordbookService {

    @Autowired
    WordbookMapper wordbookMapper;

    @Autowired
    WordMapper wordMapper;


    /**
     * 查询所有词书
     * @return
     */
    @Override
    public Response<?> getWordbooks() {
        Response<List<Wordbook>> response = Response.success();
        List<Wordbook> wordbooks = wordbookMapper.findAllWordbook();
        if(wordbooks == null){
            response = Response.result(Constant.Error.WORDBOOK_NOT_FOUNDED);
        }
        response.setData(wordbooks);
        return response;
    }

    /**
     * 获取词书细节
     * @param id
     * @param learner_id
     * @return
     */
    @Override
    public Response<?> getWordbookDetail(Integer id, Integer learner_id) {
        Response<Wordbook> response = Response.success();
        Wordbook wordbook = wordbookMapper.getWordbookDetailAndIsAdd(id,learner_id);

        if(wordbook == null) {
            log.warn("获取词书细节失败:{}", Constant.Error.WORDBOOK_NOT_FOUNDED.getMessage());
            response = Response.result(Constant.Error.WORDBOOK_NOT_FOUNDED);
        }
        response.setData(wordbook);
        return response;
    }

    /**
     *  获取词书单词
     * @param id
     * @param curr
     * @param size
     * @return
     */
    @Override
    public Response<?> getWords(Integer id, int curr, int size) {
        Response<Wordbook> response = Response.success();
        List<Word> words = wordbookMapper.getWords(id,(curr-1)*size,size);
        Wordbook wordbook = new Wordbook();
        wordbook.setWords(words);
        response.setData(wordbook);
        return response;
    }

    /**
     * 为用户添加词书
     * 这里要开启事务，但是还不太了解事务，所以以后再完善
     * @param learnerId
     * @param wordbook_id
     * @return
     */
//    @Transactional(rollbackFor=Exception.class)
    @Override
    public Response<?> addWordbook(Integer learnerId, Integer wordbook_id){
        Response<?> response = Response.success();
        //将原先计划去掉
        wordbookMapper.removePlan(learnerId);
        //新增计划，total为影响条数
        Integer total = wordbookMapper.addWordbook(learnerId,wordbook_id);
        if(total <= 0) {
            log.warn("为用户添加词书失败: {}, wordbook_id:{}", Constant.Error.ADD_WORDBOOK_ERROR.getMessage(), wordbook_id);
            response = Response.result(Constant.Error.ADD_WORDBOOK_ERROR);
        }
        return response;
    }

    /**
     * 查询我的词书
     * @param learnerId
     * @return
     */
    @Override
    public Response<?> findMyWordbooks(Integer learnerId) {
        Response<List<Wordbook>> response = Response.success();
        //查询我的词书
        List<Wordbook> myWordbooks = wordbookMapper.findMyWordbooks(learnerId);
        //如果没有词书，则返回502
        if(myWordbooks.size() <= 0){
            response = Response.result(Constant.Error.LEARNER_NOT_WORDBOOK);
        }
        response.setData(myWordbooks);
        return response;
    }

    /**
     * 更新背词计划
     * @param learnerId
     * @param wordbook_id
     * @return
     */
    @Override
    public Response<?> updateWordbookPlan(Integer learnerId, Integer wordbook_id) {
        Response<?> response = new Response<>();
        //将原先计划去掉
        wordbookMapper.removePlan(learnerId);
        //修改计划
        Integer total = wordbookMapper.updateWordbookPlan(learnerId,wordbook_id);
        if(total < 1) {
            response = Response.result(Constant.Error.SET_NEW_WORDBOOK_PLAN_ERROR);
        }
        return response;
    }

    /**
     *查看用户当前的计划
     * @param learnerId
     * @return
     */
    @Override
    public Response<?> myMemorizingWordbook(Integer learnerId) {
        Response<Wordbook> response = Response.success();
        Wordbook wordbook = wordbookMapper.findMemorizingWordbook(learnerId);
        if(wordbook == null){
            response = Response.result(Constant.Error.WORDBOOK_NOT_FOUNDED);
        }
        response.setData(wordbook);
        return response;
    }

    /**
     * 查询复习单词数目
     * @param learnerId
     * @param wordbook_id
     * @return
     */
    @Override
    public Integer findReviewCount(Integer learnerId, Integer wordbook_id) {
        return wordMapper.getReviewCount(learnerId,wordbook_id);
    }

    /**
     *查看用户当前的计划并返回复习单词数量
     * @param learnerId
     * @return
     */
    @Override
    public Response<?> getMemorizingWordbookAndReviewCount(Integer learnerId) {
        Response<Wordbook> response = Response.success();
        Wordbook wordbook = wordbookMapper.getMemorizingWordbookAndReviewCount(learnerId);
        if(wordbook == null){
            return Response.result(Constant.Error.WORDBOOK_NOT_FOUNDED);
        }
        response.setData(wordbook);
        return response;
    }
}
