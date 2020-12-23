package com.ahuiali.word.mapper;

import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.Wordbook;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ahui
 */
@Repository
@Mapper
public interface WordbookMapper {


    /**
     * 查询所有词书
     * @return
     */
    @Select("select * from wordbook")
    List<Wordbook> findAllWordbook();

    /**
     * 返回词书的详细情况
     * @param id
     * @param learnerId
     * @return
     */
    @Select(" SELECT id, NAME, summary, img ,COUNT,(SELECT COUNT(*) " +
            "FROM learner_wordbook " +
            "WHERE wordbook_id = #{id} AND learner_id = #{learnerId}\n" +
            ") AS is_memorizing FROM wordbook WHERE id = #{id} LIMIT 1;")
    Wordbook getWordbookDetailAndIsAdd(Integer id,Integer learnerId);

    /**
     * 返回词书的详细情况
     * @param id
     * @return
     */
    @Select("select id, name, summary,count,img from wordbook where id = #{id}")
    Wordbook getWordbookDetail(Integer id);

    /**
     * 分页获取某词书的单词
     * @param id
     * @param curr
     * @param size
     * @return
     */
    @Select("select id, word, paraphrase, pronUs, pronUk from words where wordbook_id = #{id} limit #{curr},#{size};")
    List<Word> getWords(Integer id, int curr, int size);

    /**
     *
     * 移除当前计划
     * @param learnerId
     * @return
     */
    @Update("update learner_wordbook set is_memorizing = 0, modified = NOW() where learner_id = #{learnerId} and is_memorizing = 1;")
    Integer removePlan(Integer learnerId);

    /**
     * 为用户添加新词书
     * @param learnerId
     * @param wordbookId
     * @return
     */
    @Insert("insert into learner_wordbook (learner_id,wordbook_id,learned_count,is_memorizing,created,modified)" +
            "values (#{learnerId},#{wordbookId},0,1,NOW(),NOW());")
    @Options(useGeneratedKeys=true, keyProperty="id")
    Integer addWordbook(Integer learnerId, Integer wordbookId);

    /**
     * 查询用户的词书
     * @param learnerId
     * @return
     */
    @Select("SELECT lw.`learned_count`  as learnedCount ,lw.`is_memorizing` as isMemorizing,w.`count`,w.`id`,w.`name`,w.`img` \n" +
            "FROM learner_wordbook lw \n" +
            "INNER JOIN wordbook w \n" +
            "ON lw.`wordbook_id` = w.`id` \n" +
            "AND lw.`learner_id` = #{learnerId};")
    List<Wordbook> findMyWordbooks(Integer learnerId);

    /**
     * 查询当前计划
     * @param learnerId
     * @return
     */
    @Select("SELECT lw.`learned_count` as learnedCount,w.`count`,w.`id`,w.`name`,w.`img` \n" +
            "FROM learner_wordbook lw \n" +
            "INNER JOIN wordbook w \n" +
            "ON lw.`wordbook_id` = w.`id` \n" +
            "AND lw.`learner_id` = #{learnerId} " +
            "AND lw.is_memorizing = 1;")
    Wordbook findMemorizingWordbook(Integer learnerId);

    /**
     *
     * 查询当前计划的同时返回需复习单词数量
     * @param learnerId
     * @return
     */
    @Select("SELECT lw.`learned_count` as learnedCount,w.`count`,w.`id`,w.`name`,\n" +
            "(SELECT COUNT(*) FROM memorize \n" +
            "  WHERE learner_id = #{learnerId}\n" +
            "  AND wordbook_id = w.`id`\n" +
            "  AND is_get = 0 \n" +
            "  AND NOW() > next_time) AS reviewCount\n" +
            "        FROM learner_wordbook lw \n" +
            "        INNER JOIN wordbook w \n" +
            "        ON lw.`wordbook_id` = w.`id` \n" +
            "        AND lw.`learner_id` = #{learnerId}\n" +
            "        AND lw.is_memorizing = 1;")
    Wordbook getMemorizingWordbookAndReviewCount(Integer learnerId);

    /**
     * 设置新计划
     * @param learnerId
     * @param wordbookId
     * @return
     */
    @Update("update learner_wordbook set is_memorizing = 1, modified = NOW() " +
            "where learner_id = #{learnerId} and wordbook_id = #{wordbookId}")
    Integer updateWordbookPlan(Integer learnerId, Integer wordbookId);
}
