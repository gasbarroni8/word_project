package com.ahuiali.word.mapper;


import com.ahuiali.word.dto.IdDto;
import com.ahuiali.word.dto.NoteBookWordDto;
import com.ahuiali.word.dto.NotebookDto;
import com.ahuiali.word.dto.WordBaseDto;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.common.utils.PageUtil;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface NotebookMapper {
    /**
     * 查询用户的所有生词本
     * @param learnerId 用户id
     * @return
     */
    @Select("select id,name,count from notebook where learner_id = #{learnerId} order by modified desc")
    List<NotebookDto> findAllNotebookByLearnerId(Integer learnerId);

    /**
     * 新建生词本
     * @param notebook 生词本对象
     * @return
     */
    @Insert("insert into notebook (learner_id,name,count)" +
            " values (#{learnerId},#{notebook.name},0)")
    @Options(useGeneratedKeys = true, keyProperty = "notebook.id", keyColumn = "id")
    Integer addNotebook(NotebookDto notebook, Integer learnerId);

    /**
     * 删除生词本
     * @param id 生词本id
     * @return 操作数
     */
    @Delete("delete from notebook where id = #{id}")
    Integer removeNotebook(Integer id);

    /**
     * 删除生词本的所有单词
     * @param notebookId 生词本id
     * @return
     */
    @Delete("delete from notebook_word where notebook_id = #{notebookId}")
    Integer removeAllNotebookWords(Integer notebookId);


    /**
     * 从生词本中移除单词
     * @param id 生词本-单词id
     * @return
     */
    @Delete("delete from notebook_word where id = #{id}")
    Integer removeWord(Integer id);

    /**
     * 添加单词到生词本
     * @param notebookId 生词本id
     * @param word 单词
     * @return
     */
    @Insert("INSERT INTO notebook_word (notebook_id,word,pron_us,pron_uk,paraphrase, review_count, next_time) " +
            "SELECT #{notebookId},word,pron_us,pron_uk,translation,1,date_add(NOW(), interval 30 minute) " +
            "FROM wordect " +
            "WHERE word = #{word};")
    @Options(useGeneratedKeys = true, keyProperty = "idDto.id", keyColumn = "id")
    Integer addWord(Integer notebookId, String word, IdDto idDto);

    /**
     * 分页显示生词本单词
     * @param notebookId 生词本id
     * @param pageUtil 分页
     * @return
     */
    @Select("select id,word,pron_uk as pronUk,pron_us as pronUs, paraphrase as mean, modified " +
            "from notebook_word where notebook_id = #{notebookId} ORDER BY modified DESC " +
            "limit #{pageUtil.offset},#{pageUtil.size}")
    List<WordBaseDto> listWords(Integer notebookId, PageUtil pageUtil);

    /**
     * 查看该单词是否存在该用户的所有生词表中
     *  -？？？？？这TM是啥？？？？？？
     *      -噢，知道了
     * @param word 单词
     * @param learnerId 用户id
     * @return
     */
    @Select("SELECT id FROM notebook_word WHERE word = #{word} " +
            "AND notebook_id IN (SELECT id FROM notebook WHERE learner_id = #{learnerId}) limit 1;")
    Integer findWordExistNotebooks(String word, Integer learnerId);

    /**
     * 修改名称
     * @param name 生词本新名称
     * @param notebookId 生词本id
     * @return
     */
    @Update("UPDATE notebook SET NAME = #{name} , modified = NOW() WHERE id = #{notebookId};")
    Integer editNotebookName(String name, Integer notebookId);


    /**
     * 为生词本添加单词（能返回主键）
     * @param notebookId  生词本id
     * @param wordect 单词实体
     * @return
     */
    @Insert("insert into notebook_word (notebook_id,word,pron_us,pron_uk,paraphrase,created,modified) value " +
            "(#{notebookId},#{wordect.word},#{wordect.pron_us},#{wordect.pron_uk},#{wordect.translation},NOW(),NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "wordect.id",keyColumn = "id")
    int addWordEct(WordEct wordect, Integer notebookId);

    /**
     * 生词本单词数目加一
     * @param notebookId 生词本id
     */
    @Update("update notebook set count = count + 1, modified = NOW() where id = #{notebookId}; ")
    void notebookCountPlus(Integer notebookId);

    /**
     * 生词本单词数目减一
     * @param notebookId 生词本id
     */
    @Update("update notebook set count = count - 1, modified = NOW() where id = #{notebookId}; ")
    void notebookCountMinus(Integer notebookId);

    /**
     * 通过notebook_word的id查询notebook_id
     * @param id
     * @return
     */
    @Select("select notebook_id from notebook_word where id = #{id}")
    Integer findIdByNotebookWordId(Integer id);

    @InsertProvider(type = NotebookMapper.UpdateReviewWords.class,method = "update")
    Integer updateReviewWords(String sql);

    /**
     * 获取需要复习的单词
     * @param notebookId 生词本id
     * @param pageUtil 分页
     * @return
     */
    @Select("SELECT `id`,word,pron_us as pronUs,pron_uk as pronUk,review_count as reviewCount, paraphrase " +
            "FROM notebook_word " +
            "WHERE notebook_id = #{notebookId} " +
            "AND review_count < 7 " +
            "AND (NOW() > next_time) limit #{pageUtil.offset},#{pageUtil.size};")
    List<NoteBookWordDto> getReviewWords(Integer notebookId, PageUtil pageUtil);

    class UpdateReviewWords{
        public String update(String sql){
            return sql;
        }
    }
}
