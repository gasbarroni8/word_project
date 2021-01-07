package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.WordBaseDto;
import com.ahuiali.word.dto.WordDetailDto;
import com.ahuiali.word.dto.WordPreDto;
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
     *
     * @param wordRre 单词前缀
     * @return resp
     */
    @Override
    public Response<?> getWordsByPre(String wordRre) {
        Response<List<WordPreDto>> response = Response.success();
        //数据库中查找
        List<WordPreDto> wordEctList = wordEctMapper.getWordsByPre(wordRre);
        //如果大于0说明仍有提示
        if (wordEctList.size() <= Constant.ZERO) {
            return Response.result(Constant.Error.WORD_PRE_NOT_FOUNDED);
        }
        response.setData(wordEctList);
        return response;
    }

    /**
     * 查找单词详细信息
     *
     * @param word      单词
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> findWordDetail(String word, Integer learnerId) {
//        Response<WordEctDetail> response = Response.success();
//        //先去redis查有没有该单词
//        boolean hasWordKey = template.opsForHash().hasKey("words", word);
//        //单词详细信息
//        WordEctDetail wordEctDetail = new WordEctDetail();
//        //例句
//        List<Sentence> sentences = new ArrayList<>();
//        //例句id字符串
//        String senStr;
//        //例句id列表
//        List<String> sens;
//        //是否需要在数据库中查询
//        boolean searchSensInDB = false;
//        //如果redis中有该单词
//        if (hasWordKey) {
//            //获取该单词的信息
//            String wordJsonStr = (String) template.opsForHash().get("words", word);
//            //转码
//            wordEctDetail = JSON.parseObject(wordJsonStr, WordEctDetail.class);
//            // TODO NPE
//            //获取例句id数组字符串
//            if (wordEctDetail == null) {
//                response.putResult(Constant.Error.SYSTEM_ERROR);
//                return response;
//            }
//            senStr = wordEctDetail.getSentence_list();
//            //查询例句
//            if (senStr != null && !"".equals(senStr)) {
//                //将字符串转为list
//                List<Object> sensList = Arrays.asList(senStr.split(","));
//                //从redis的sentences中获取
//                List<Object> sentencesObject = template.opsForHash().multiGet("sentences", sensList);
//
//                //如果获取的到，说明redis没失效
//                if (sentencesObject.size() != 0) {
//                    //转码（应该还可以优化）
//                    for (Object s : sentencesObject) {
//                        Sentence sentence = JSON.parseObject(s.toString(), Sentence.class);
//                        ;
//                        sentences.add(sentence);
//                    }
//                } else {
//                    //去数据库中查
//                    searchSensInDB = true;
//                }
//                wordEctDetail.setSentences(sentences);
//                //查询该单词是否已经被该用户收藏
//                Integer notebook_id = notebookMapper.findWordExistNotebooks(wordEctDetail.getWord(), learnerId);
//                //已收藏
//                if (notebook_id != null && notebook_id > Constant.ZERO) {
//                    wordEctDetail.setNotebook_word_id(notebook_id);
//                }
//
//            }
//        } else {
//            //否则全部数据去数据库中查询
//            wordEctDetail = wordEctMapper.findWordEctDetail(word, learnerId);
//            //数据库中仍找不到
//            if (wordEctDetail == null || wordEctDetail.getId() == null) {
//                return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
//            } else {
//                //数据库中查询到了单词
//                //查询例句id字符串
//                senStr = wordEctMapper.findWordSentenceIds(wordEctDetail.getWord());
//                //如果查询到例句id字符串
//                if (senStr != null && !"".equals(senStr)) {
//                    //需要在数据库中查
//                    searchSensInDB = true;
//                }
//            }
//        }
//        //是否需要在数据库中查询例句
//        if (searchSensInDB) {
//            sens = Arrays.asList(senStr.split(","));
//            //处理例句id字符串
//            StringBuilder sb = new StringBuilder();
//            sb.append("select id, sentence_en, sentence_cn from sentence where");
//            sens.forEach(s -> {
//                sb.append(" id = ").append(s).append(" or ");
//            });
//            sb.append("id = 0;");
//            sentences = sentencesMapper.findSentences(sb.toString());
//        }
//        //清除无关数据(*)
//        wordEctDetail.setSentence_list("");
//        //设置例句
//        wordEctDetail.setSentences(sentences);
//        //将单词详细加入json
//        response.setData(wordEctDetail);
//        return response;
        return null;
    }


    /**
     * 查询单词（释义音标）
     *
     * @param word      单词
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> findWord(String word, Integer learnerId) {
        Response<WordBaseDto> response = Response.success();
        WordBaseDto wordBaseDto = new WordBaseDto();
        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words", word);
        //如果redis中能找到
        if (hasWordKey) {
            //获取该单词的信息
            String wordJsonStr = (String) template.opsForHash().get("words", "word");
            if (!"".equals(wordJsonStr) && wordJsonStr != null) {
                //将json转换为单词对象
                wordBaseDto = JSON.parseObject(wordJsonStr, WordBaseDto.class);
            }
        } else {
            //数据库中查询
            wordBaseDto = wordEctMapper.findWord(word);
            if (wordBaseDto == null) {
                return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
            }
        }
        //查询该单词是否已收藏
        Integer notebook_id = notebookMapper.findWordExistNotebooks(wordBaseDto.getWord(), learnerId);
        //已收藏
        if (notebook_id != null && notebook_id > Constant.ZERO) {
            wordBaseDto.setNotebookWordId(notebook_id);
        } else {
            wordBaseDto.setNotebookWordId(Constant.ZERO);
        }
        return response;
    }

    /**
     * 查找单词详细信息（非redis）
     *
     * @param word      单词
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> findWordDetailNoRedis(String word, Integer learnerId) {
        Response<WordDetailDto> response = Response.success();
        //单词详细信息
        WordDetailDto wordEctDetail = wordEctMapper.findWordEctDetail(word, learnerId);
        if (wordEctDetail == null) {
            response.putResult(Constant.Error.WORDECT_NOT_FOUNDED);
            return response;
        }
        String senStr = wordEctMapper.findWordSentenceIds(wordEctDetail.getWord());
        if (!"".equals(senStr) && senStr != null) {
            List<String> sens = Arrays.asList(senStr.split(","));
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
        Integer notebookId = notebookMapper.findWordExistNotebooks(wordEctDetail.getWord(), learnerId);
        //已收藏
        if (notebookId != null && notebookId > 0) {
            wordEctDetail.setIsAdd(notebookId);
        } else {
            wordEctDetail.setIsAdd(Constant.ZERO);
        }
        //将单词详细加入json
        response.setData(wordEctDetail);
        return response;
    }

    @Override
    public Response<?> findWordNoRedis(String word, Integer learnerId) {
        Response<WordBaseDto> response = Response.success();
        WordBaseDto wordBaseDto = wordEctMapper.findWord(word);
        if (wordBaseDto == null) {
            return Response.result(Constant.Error.WORDECT_NOT_FOUNDED);
        }
        //查询该单词是否已收藏
        Integer notebookId = notebookMapper.findWordExistNotebooks(wordBaseDto.getWord(), learnerId);
        //已收藏
        if (notebookId != null && notebookId > Constant.ZERO) {
            wordBaseDto.setNotebookWordId(notebookId);
        } else {
            // 生词本为0说明没有收藏
            wordBaseDto.setNotebookWordId(Constant.ZERO);
        }
        response.setData(wordBaseDto);
        return response;
    }
}
