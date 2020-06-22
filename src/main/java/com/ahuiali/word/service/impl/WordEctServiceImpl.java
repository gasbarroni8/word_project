package com.ahuiali.word.service.impl;

import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.json.WordEctJson;
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
    WordEctJson wordEctJson;

    @Autowired
    WordEctMapper wordEctMapper;

    @Autowired
    WordEctDetailJson wordEctDetailJson;

    @Autowired
    StringRedisTemplate template;

    @Autowired
    NotebookMapper notebookMapper;

    @Autowired
    WordEctDetail wordEctDetail;

    @Autowired
    WordEct wordEct;

    @Autowired
    SentencesMapper sentencesMapper;

    /**
     * 通过单词前缀来模糊查询单词，自动提示效果
     * @param wordpre
     * @return
     */
    @Override
    public WordEctJson getWordsByPre(String wordpre) {
        wordEctJson = new WordEctJson();

        //数据库中查找
        List<WordEct> wordEctList = wordEctMapper.getWordsByPre(wordpre);

        //如果大于0说明仍有提示
        if(wordEctList.size() > 0){
            wordEctJson.setWordEctList(wordEctList);
            wordEctJson.create(200,"success");
        } else {
            wordEctJson.create(700,"该单词模糊查询已无结果");
        }
        return wordEctJson;
    }

    /**
     * 查找单词详细信息
     * @param word 单词
     * @param learner_id 用户id
     * @return
     */
    @Override
    public WordEctDetailJson findWordDetail(String word, Integer learner_id) {
        wordEctDetailJson = new WordEctDetailJson();
        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words",word);
        //单词详细信息
        WordEctDetail wordEctDetail;
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
                wordEctDetailJson.create(701,"数据库中找不到该单词");

                return wordEctDetailJson;
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
        wordEctDetailJson.setWordEctDetail(wordEctDetail);
        //成功，返回200
        wordEctDetailJson.create(200,"success");
        return wordEctDetailJson;
    }

    /**
     * 查询单词（释义音标）
     * @param word 单词
     * @param learner_id 用户id
     * @return
     */
    @Override
    public WordEctJson findWord(String word, Integer learner_id) {

        wordEctJson = new WordEctJson();

        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words",word);
        String wordRedis = "";
        //如果redis中能找到
        if(hasWordKey){
            //获取该单词的信息
            String wordJsonStr = (String) template.opsForHash().get("words","word");
            if(!"".equals(wordRedis) && wordRedis != null){
                //将json转换为单词对象
                wordEct =  JSON.parseObject(wordJsonStr,WordEct.class);
            }
        }
        if("".equals(wordRedis) || null == wordRedis){
            //数据库中查询
            wordEct = wordEctMapper.findWord(word);
            if(wordEct == null){
                wordEctJson.create(701,"数据库中找不到该单词");
                return wordEctJson;
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

        wordEctJson.setWordEct(wordEct);
        wordEctJson.create(200,"success");

        return wordEctJson;
    }

    /**
     * 查找单词详细信息（非redis）
     * @param word
     * @param learnerId
     * @return
     */
    @Override
    public WordEctDetailJson findWordDetailNoRedis(String word, Integer learnerId) {
        wordEctDetailJson = new WordEctDetailJson();
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
        wordEctDetailJson.setWordEctDetail(wordEctDetail);
        //成功，返回200
        wordEctDetailJson.create(200,"success");
        return wordEctDetailJson;
    }

    @Override
    public WordEctJson findWordNoRedis(String word, Integer learnerId) {
        wordEctJson = new WordEctJson();
        wordEct = wordEctMapper.findWord(word);
        if(wordEct == null){
            wordEctJson.create(701,"数据库中找不到该单词");
            return wordEctJson;
        }
        //查询该单词是否已收藏
        Integer notebookId = notebookMapper.findWordExistNotebooks(wordEct.getWord(),learnerId);
        //已收藏
        if(notebookId != null && notebookId > 0){
            wordEct.setNotebook_word_id(notebookId);
        }else {
            wordEct.setNotebook_word_id(0);
        }

        wordEctJson.setWordEct(wordEct);
        wordEctJson.create(200,"success");

        return wordEctJson;
    }
}
