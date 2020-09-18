package com.ahuiali.word.mapper;


import com.ahuiali.word.pojo.Sentence;
import com.ahuiali.word.pojo.WordEctDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shkstart on 2019/10/6
 * @author ahui
 */
@Repository
@Mapper
public interface SentencesMapper {

    /**
     * 查询所有例句（测试和导入redis用的，会导致项目暂停）
     * @return
     */
    @Select("select id, sentence_en , sentence_cn from sentence")
    List<Sentence> findAllSentences();

    /**
     * 插入例句
     * @param s 传入的参数
     */
    @InsertProvider(type = Provider.class, method = "insertSens")
    void insertSens(String s);


    /**
     * 忘记了
     * @param word1
     * @param word2
     * @param word3
     * @return
     */
    @Select("select id from sentence where sentence_en like concat('',#{param1},' %') " +
            "or sentence_en like concat('% ',#{param2},' %')" +
            "or sentence_en like concat('% ',#{param3},'') limit 20")
    List<Integer> findAllIdByLike(String word1, String word2, String word3);

    @Select("select word, sentence_list from word_sentences ")
    List<WordEctDetail> getWordSentences();

    /**
     *  向单词-例句中间表插入数据
     * @param id
     * @param word
     * @param idlist
     */
    @Insert("insert into word_sentence (word_id,word,sentence_list) value (#{id},#{word},#{idlist})")
    void addWordAndSentence(Integer id, String word , String idlist);


    class Provider{
        public String insertSens(String s){

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into sentence(sentence_en,sentence_cn) values ");
            stringBuilder.append(s);
            return stringBuilder.toString();
        }
    }

    /**
     * 查询例句
     * @param sens
     * @return
     */
    @SelectProvider(type = FindSentences.class, method = "find")
    List<Sentence> findSentences(String sens);

    class FindSentences{
        public String find(String sens){
            return sens;
        }

    }
}
