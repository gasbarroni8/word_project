package com.ahuiali.word.redis;

import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.mapper.NotebookMapper;
import com.ahuiali.word.mapper.SentencesMapper;
import com.ahuiali.word.mapper.WordEctMapper;
import com.ahuiali.word.pojo.Sentence;
import com.ahuiali.word.pojo.WordEctDetail;
import com.ahuiali.word.service.WordEctService;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisInit {

    @Autowired
    StringRedisTemplate template;

    @Autowired
    SentencesMapper sentencesMapper;

    @Autowired
    WordEctMapper wordEctMapper;

    @Autowired
    NotebookMapper notebookMapper;

    @Autowired
    WordEctService wordEctService;


    @Autowired
    WordEctDetailJson wordEctDetailJson;

    //常用词
    @Test
    public void words(){
        List<WordEctDetail> wordEctDetails = wordEctMapper.findAllWordToRedis();
        Map<String,String> map = new HashMap<String,String>();
        wordEctDetails.forEach(wordEctDetail -> {
//            String value = JSON.toJSONString(wordEctDetail);
//            template.opsForValue().set(wordEctDetail.getWord(),value);
            map.put(wordEctDetail.getWord(),JSON.toJSONString(wordEctDetail));
        });
        template.opsForHash().putAll("words",map);
    }

    //把word_sentence加入redis（*）
    @Test
    public void initWord_sentence(){
        Map<String,String> map = new HashMap<String,String>();
        List<WordEctDetail> wordEcts = sentencesMapper.getWordSentences();
        wordEcts.forEach(wordEct -> {
            map.put(wordEct.getWord(),wordEct.getSentence_list());
        });
        template.opsForHash().putAll("word_sentences",map);
    }

    //把所有句子加载到内存
    @Test
    public void initSentence() {
        List<Sentence> sentences = sentencesMapper.findAllSentences();
        Map<String,String> map = new HashMap<String,String>();
        sentences.forEach(sentence -> {
            String key = sentence.getId() + "";
            String value = JSON.toJSONString(sentence);
            map.put(key,value);
        });
        template.opsForHash().putAll("sentences",map);
    }

    //查词测试
    @Test
    public void test1(){
        String word = "aa";

        //先去redis查有没有该单词
        boolean hasWordKey = template.opsForHash().hasKey("words",word);

        WordEctDetail wordEctDetail;

        List<Sentence> sentences = new ArrayList<>();

        String senStr;
        //如果redis中有该单词
        if(hasWordKey){
            //获取该单词的信息
            String wordJsonStr = (String) template.opsForHash().get("words","word");
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
                    for(Object s : sentencesObject){
                        Sentence sentence = JSON.parseObject(s.toString(),Sentence.class);;
                        sentences.add(sentence);
                    }
                }else {
                    //去数据库中查
                    List<String> sens = Arrays.asList(senStr.split(","));
                    //处理例句id字符串
                    StringBuilder sb = new StringBuilder();
                    sb.append("select id, sentence_en, sentence_cn from sentence where");
                    sens.forEach(s -> {
                        sb.append(" id = ").append(s).append(" or ");
                    });
                    sb.append("id = 0;");
                    sentences = sentencesMapper.findSentences(sb.toString());
                }
                wordEctDetail.setSentences(sentences);
                //查询该单词是否已经被该用户收藏
                Integer notebook_id = notebookMapper.findWordExistNotebooks(wordEctDetail.getWord(),2);
                //已收藏
                if(notebook_id != null && notebook_id > 0){
                    wordEctDetail.setNotebook_word_id(notebook_id);
                }

            }
        }else {
            //否则全部数据去数据库中查询
            wordEctDetail = wordEctMapper.findWordEctDetail(word,20);
            //数据库中仍找不到
            if(wordEctDetail == null ||wordEctDetail.getId() == null){
                wordEctDetailJson.create(701,"数据库中找不到该单词");
                return;
                //return wordEctDetailJson;
            }else {
                //数据库中查询到了单词
                //查询例句id字符串
                senStr = wordEctMapper.findWordSentenceIds(wordEctDetail.getWord());
                //如果查询到例句id字符串
                if(senStr != null && !"".equals(senStr)){
                    List<String> sens = Arrays.asList(senStr.split(","));
                    //处理例句id字符串
                    StringBuilder sb = new StringBuilder();
                    sb.append("select id, sentence_en, sentence_cn from sentence where");
                    sens.forEach(s -> {
                        sb.append(" id = ").append(s).append(" or ");
                    });
                    sb.append("id = 0;");
                    sentences = sentencesMapper.findSentences(sb.toString());
                }
            }
            //清除无关数据
            wordEctDetail.setSentence_list("");
            //设置例句
            wordEctDetail.setSentences(sentences);

        }

        //将单词详细加入json
        wordEctDetailJson.setWordEctDetail(wordEctDetail);
        //成功，返回200
        wordEctDetailJson.create(200,"success");
        System.err.println(wordEctDetailJson);
    }


    @Test
    public void test2(){
        System.err.println(wordEctService.findWordDetail("demo", 20));
    }
}