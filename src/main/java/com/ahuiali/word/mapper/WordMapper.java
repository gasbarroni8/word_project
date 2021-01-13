package com.ahuiali.word.mapper;


import com.ahuiali.word.dto.WordDto;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.common.utils.PageUtil;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单词 mapper
 * @author ZhengChaoHui
 * @date 2019/9/18 22:27
 */
@Repository
@Mapper
public interface WordMapper {

   @Select("SELECT word, translation FROM enwords WHERE word = #{word};")
    Word findWord(String word);

    @Select("select word from enwords where word_id > #{start} and word_id <= #{end}")
    List<Word> getAllWords(@Param("start") int start, @Param("end") int end);

    @Select("SELECT word, translation FROM enwords WHERE word like concat(#{wordPrefix},'%') limit 20;")
    List<Word> findAllByPrefix(String prefix);

    @Select("SELECT * FROM enwords WHERE translation IS NULL;")
    List<Word> findNoTrans();

    @Update("update enwords set translation = #{translation} where word_id = #{word_id}")
    void upateWord(Word word);

    /**
     * 返回size个该词书的单词
     * @param id
     * @param pageUtil
     * @return
     */
    @Select("select id, word, paraphrase, pron_us, pron_uk from words " +
            "where wordbook_id = #{id} " +
            "limit #{pageUtil.offset},#{pageUtil.size};")
    List<Word> getWords(int id, PageUtil pageUtil);

    /**
     * 查询未背单词
     * @param wordbook_id
     * @param learner_id
     * @param pageUtil
     * @return
     */
    // TODO 使用IN的时候，如果IN里面的内容很大，那么效率会低
    @Select("SELECT id,word,paraphrase FROM words " +
            "WHERE wordbook_id = #{wordbook_id} " +
            "AND id NOT IN " +
            "(SELECT word_id FROM memorize " +
            "WHERE learner_id = #{learner_id} " +
            "AND  wordbook_id = #{wordbook_id}) " +
            "LIMIT #{pageUtil.offset},#{pageUtil.size};")
    List<WordDto> getMyWordbookWords(Integer wordbook_id, Integer learner_id, PageUtil pageUtil);

    /**
     * 查询正在背单词
     * @param wordbook_id
     * @param learner_id
     * @param pageUtil
     * @return
     */
    @Select("SELECT w.id, w.`word`,w.`paraphrase`,w.`pron_us`,w.`pron_uk` FROM memorize m  " +
            "INNER JOIN words w ON m.word_id = w.id " +
            "WHERE m.learner_id = #{learner_id} " +
            "AND  m.wordbook_id = #{wordbook_id} " +
            "AND m.`is_get` = 0 " +
            "AND memorized_count < 7 order by m.modified,m.id desc " +
            "LIMIT #{pageUtil.offset},#{pageUtil.size};")
    List<WordDto> findMemorizingWords(Integer wordbook_id, Integer learner_id, PageUtil pageUtil);

    /**
     * 查询已掌握单词
     * @param wordbook_id
     * @param learner_id
     * @param pageUtil
     * @return
     */
    @Select("SELECT w.id, w.`word`,w.`paraphrase` FROM memorize m  " +
            "INNER JOIN words w ON m.word_id = w.id " +
            "WHERE m.learner_id = #{learner_id} " +
            "AND  m.wordbook_id = #{wordbook_id} " +
            "AND (m.`is_get` = 1 or memorized_count >= 7) order by m.modified desc " +
            "LIMIT #{pageUtil.offset},#{pageUtil.size} ;")
    List<WordDto> findMemorizdWords(Integer wordbook_id, Integer learner_id, PageUtil pageUtil);

    /**
     * 设置该单词已掌握,记忆中->掌握
     * @param wordId
     */
    @Update("update memorize set is_get = 1, modified = NOW() " +
            "where learner_id = #{learnerId} AND wordbook_id = #{wordbookId} AND word_id = #{wordId}")
    void setWordIsMemorized(Integer learnerId, Integer wordbookId, Integer wordId);

    /**
     * 往记忆表中加一个单词，并将其设为已掌握。未学->掌握
     * @param learnerId
     * @param wordbookId
     * @param wordId
     */
    @Insert("insert into memorize (learner_id,wordbook_id,word_id,memorized_count,is_get) " +
            "values (#{learnerId},#{wordbookId},#{wordId},0,1) " +
            "ON DUPLICATE KEY UPDATE modified = NOW()")
    void addWordAndSetMemorized(Integer learnerId, Integer wordbookId, Integer wordId);

    /**
     * 从记忆表中删除某条数据，即重新学习
     * @param wordId
     */
    @Delete("delete from memorize " +
            "where learner_id = #{learnerId} AND wordbook_id = #{wordbookId} AND word_id = #{wordId}")
    void removeMemorizeWord(Integer learnerId, Integer wordbookId, Integer wordId);

    /**
     * 获取复习单词数量
     * @param learnerId
     * @param wordbookId
     * @return
     */
    @Select("SELECT count(*) FROM memorize " +
            "WHERE learner_id = #{learnerId} " +
            "AND wordbook_id = #{wordbookId} " +
            "AND is_get = 0 " +
            "AND NOW() > next_time;")
    Integer getReviewCount(Integer learnerId, Integer wordbookId);

    /**
     * 获取复习单词
     * @param learner_id
     * @param wordbook_id
     * @param pageUtil
     * @return
     */
    // TODO 背了7次的呢？
    @Select("SELECT m.`id`,w.word,w.pron_us,w.pron_uk,m.memorized_count,w.paraphrase " +
            "FROM memorize m  INNER JOIN words w ON \n" +
         "(m.`word_id` = w.`id`  \n" +
         "AND  m.learner_id = #{learner_id} \n" +
         "AND m.wordbook_id = #{wordbook_id}\n" +
         "AND m.is_get = 0\n" +
         "AND m.memorized_count < 7\n" +
         "AND (NOW() > m.`next_time`)) limit #{pageUtil.offset},#{pageUtil.size};")
    List<Word> getReviewWords(Integer learner_id, Integer wordbook_id, PageUtil pageUtil);


    @Select("insert into memorize (word_id,wordbook_id,learner_id,next_time) value (33,1,21,#{next_time})")
    Integer demo(String next_time);

   @InsertProvider(type = ADD.class,method = "insert")
    Integer insertWords(String sql);

    @InsertProvider(type = UpdateReviewWords.class,method = "update")
    Integer updateReviewWords(String toString);

    @Update("update  learner_wordbook set learned_count = learned_count + #{count},modified = NOW() where learner_id = #{learner_id} and wordbook_id=#{wordbook_id}")
    void updateLearnCount(Integer wordbook_id, Integer learner_id, Integer count);

    class ADD{
      public String insert(String next_time){
       return next_time;
      }
   }

   class UpdateReviewWords{
     public String update(String sql){
      return sql;
     }
   }

}
