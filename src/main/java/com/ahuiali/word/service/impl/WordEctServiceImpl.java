package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.mapper.NotebookMapper;
import com.ahuiali.word.mapper.SentencesMapper;
import com.ahuiali.word.mapper.WordEctMapper;
import com.ahuiali.word.pojo.Sentence;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.pojo.WordEctDetail;
import com.ahuiali.word.service.WordEctService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service
public class WordEctServiceImpl implements WordEctService {

    @Autowired
    WordEctMapper wordEctMapper;

    @Autowired
    StringRedisTemplate template;

    @Autowired
    NotebookMapper notebookMapper;

    @Autowired
    SentencesMapper sentencesMapper;

    /**
     * 通过单词前缀来模糊查询单词，自动提示效果
     * @param wordRre
     * @return
     */
    @Override
    public Response<?> getWordsByPre(String wordRre) {
        Response<List<WordEct>> response = Response.success();
        //数据库中查找
        List<WordEct> wordEctList = wordEctMapper.getWordsByPre(wordRre);
        //如果大于0说明仍有提示
        if(wordEctList.size() <= 0){
           return Response.result(Constant.Error.WORD_PRE_NOT_FOUNDED);
        }
        response.setData(wordEctList);
        return response;
    }

    /**
     * 查找单词详细信息
     * @param word 单词
     * @param learner_id 用户id
     * @return
     */
    @Override
    public Response<?> findWordDetail(String word, Integer learner_id) {
        Response<WordEctDetail> response = Response.success();
        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words",word);
        //单词详细信息
        WordEctDetail wordEctDetail = new WordEctDetail();
        //例句
        List<Sentence> sentences = new ArrayList<>();
        //例句id字符串
        String senStr;
        //例句id列表
        List<String> sens;
        //是否需要在数据库中查询
        boolean searchSensInDB = false;
        //如果redis中有该单词
        if(hasWordKey){
            //获取该单词的信息
            String wordJsonStr = (String) template.opsForHash().get("words",word);
            //转码
            wordEctDetail =  JSON.parseObject(wordJsonStr,WordEctDetail.class);
            //获取例句id数组字符串
            senStr = wordEctDetail.getSentence_list();
            //查询例句
            if(senStr != null && !"".equals(senStr)){
                //将字符串转为list
                List<Object> sensList = Arrays.asList(senStr.split(","));
                //从redis的sentences中获取
                List<Object> sentencesObject = template.opsForHash().multiGet("sentences",sensList);

                //如果获取的到，说明redis没失效
                if(sentencesObject != null && sentencesObject.size()!= 0){
                    //转码（应该还可以优化）
                    for(Object s : sentencesObject){
                        Sentence sentence = JSON.parseObject(s.toString(),Sentence.class);;
                        sentences.add(sentence);
                    }
                }else {
                    //去数据库中查
                    searchSensInDB = true;
                }
                wordEctDetail.setSentences(sentences);
                //查询该单词是否已经被该用户收藏
                Integer notebook_id = notebookMapper.findWordExistNotebooks(wordEctDetail.getWord(),learner_id);
                //已收藏
                if(notebook_id != null && notebook_id > 0){
                    wordEctDetail.setNotebook_word_id(notebook_id);
                }

            }
        }else {
            //否则全部数据去数据库中查询
            wordEctDetail = wordEctMapper.findWordEctDetail(word,learner_id);
            //数据库中仍找不到
            if(wordEctDetail == null ||wordEctDetail.getId() == null){
                return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
            }else {
                //数据库中查询到了单词
                //查询例句id字符串
                senStr = wordEctMapper.findWordSentenceIds(wordEctDetail.getWord());
                //如果查询到例句id字符串
                if(senStr != null && !"".equals(senStr)){
                    //需要在数据库中查
                    searchSensInDB = true;
                }
            }
        }
        //是否需要在数据库中查询例句
        if(searchSensInDB){
            sens = Arrays.asList(senStr.split(","));
            //处理例句id字符串
            StringBuilder sb = new StringBuilder();
            sb.append("select id, sentence_en, sentence_cn from sentence where");
            sens.forEach(s -> {
                sb.append(" id = ").append(s).append(" or ");
            });
            sb.append("id = 0;");
            sentences = sentencesMapper.findSentences(sb.toString());
        }
        //清除无关数据(*)
        wordEctDetail.setSentence_list("");
        //设置例句
        wordEctDetail.setSentences(sentences);
        //将单词详细加入json
        response.setData(wordEctDetail);
        return response;
    }

    /**
     * 查询单词（释义音标）
     * @param word 单词
     * @param learner_id 用户id
     * @return
     */
    @Override
    public Response<?> findWord(String word, Integer learner_id) {
        Response<WordEct> response = Response.success();
        WordEct wordEct = new WordEct();
        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words",word);
        //如果redis中能找到
        if(hasWordKey){
            //获取该单词的信息
            String wordJsonStr = (String) template.opsForHash().get("words","word");
            if(!"".equals(wordJsonStr) && wordJsonStr != null){
                //将json转换为单词对象
                wordEct =  JSON.parseObject(wordJsonStr,WordEct.class);
            }
        } else {
            //数据库中查询
            wordEct = wordEctMapper.findWord(word);
            if(wordEct == null){
                return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
            }
        }
        //查询该单词是否已收藏
        Integer notebook_id = notebookMapper.findWordExistNotebooks(wordEct.getWord(),learner_id);
        //已收藏
        if(notebook_id != null && notebook_id > 0){
            wordEct.setNotebook_word_id(notebook_id);
        }else {
            wordEct.setNotebook_word_id(0);
        }
        return response;
    }

    /**
     * 查找单词详细信息（非redis）
     * @param word
     * @param learnerId
     * @return
     */
    @Override
    public Response<?> findWordDetailNoRedis(String word, Integer learnerId) {
        Response<WordEctDetail> response = Response.success();
        //单词详细信息
        WordEctDetail wordEctDetail = wordEctMapper.findWordEctDetail(word,learnerId);
        String senStr = wordEctMapper.findWordSentenceIds(wordEctDetail.getWord());
        if(!"".equals(senStr) && senStr != null){
            List<String > sens = Arrays.asList(senStr.split(","));

            //处理例句id字符串
            StringBuilder sb = new StringBuilder();
            sb.append("select id, sentence_en, sentence_cn from sentence where");
            sens.forEach(s -> {
                sb.append(" id = ").append(s).append(" or ");
            });
            sb.append("id = 0;");
            List<Sentence> sentences = sentencesMapper.findSentences(sb.toString());
            //设置例句
            wordEctDetail.setSentences(sentences);
        }

        //查询该单词是否已经被该用户收藏
        Integer notebook_id = notebookMapper.findWordExistNotebooks(wordEctDetail.getWord(),learnerId);
        //已收藏
        if(notebook_id != null && notebook_id > 0){
            wordEctDetail.setNotebook_word_id(notebook_id);
        }

        //清除无关数据(*)
        wordEctDetail.setSentence_list("");

        //将单词详细加入json
        response.setData(wordEctDetail);
        return response;
    }

    @Override
    public Response<?> findWordNoRedis(String word, Integer learnerId) {
        Response<WordEct> response = Response.success();
        WordEct wordEct = wordEctMapper.findWord(word);
        if(wordEct == null){
            return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
        }
        //查询该单词是否已收藏
        Integer notebookId = notebookMapper.findWordExistNotebooks(wordEct.getWord(),learnerId);
        //已收藏
        if(notebookId != null && notebookId > 0){
            wordEct.setNotebook_word_id(notebookId);
        }else {
            wordEct.setNotebook_word_id(0);
        }
        response.setData(wordEct);
        return response;
    }
}
