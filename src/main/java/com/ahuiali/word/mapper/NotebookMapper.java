package com.ahuiali.word.mapper;


import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.common.utils.PageUtil;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NotebookMapper {
    /**
     * 查询用户的所有生词本
     * @param learner_id 用户id
     * @return
     */
    @Select("select id,name,count from notebook where learner_id = #{leaner_id} order by modified desc")
    List<Notebook> findAllNotebookByLearnerId(Integer learner_id);

    /**
     * 新建生词本
     * @param notebook 生词本对象
     * @return
     */
    @Insert("insert into notebook (learner_id,name,count,created,modified)" +
            " values (#{learner_id},#{name},0,NOW(),NOW())")
    Integer addNotebook(Notebook notebook);

    /**
     * 删除生词本
     * @param id 生词本id
     * @return 操作数
     */
    @Delete("delete from notebook where id = #{id}")
    Integer removeNotebook(Integer id);

    /**
     * 删除生词本的所有单词
     * @param notebook_id 生词本id
     * @return
     */
    @Delete("delete from notebook_word where notebook_id = #{notebook_id}")
    Integer removeAllNotebookWords(Integer notebook_id);


    /**
     * 从生词本中移除单词
     * @param id 生词本-单词id
     * @return
     */
    @Delete("delete from notebook_word where id = #{id}")
    Integer removeWord(Integer id);

    /**
     * 添加单词到生词本
     * @param notebook_id 生词本id
     * @param word 单词
     * @return
     */
    @Insert("INSERT INTO notebook_word (notebook_id,word,pron_us,pron_uk,paraphrase) " +
            "SELECT #{notebook_id},word,pron_us,pron_uk,translation " +
            "FROM wordect " +
            "WHERE word = #{word};")
    Integer addWord(Integer notebook_id, String word);

    /**
     * 分页显示生词本单词
     * @param notebook_id 生词本id
     * @param pageUtil 分页
     * @return
     */
    @Select("select id,word,pron_uk,pron_us,paraphrase from notebook_word where notebook_id = #{notebook_id} " +
            "limit #{pageUtil.offset},#{pageUtil.size};")
    List<Word> listWords(Integer notebook_id, PageUtil pageUtil);

    /**
     * 查看该单词是否存在该用户的所有生词表中
     *  -？？？？？这TM是啥？？？？？？
     *      -噢，知道了
     * @param word 单词
     * @param learner_id 用户id
     * @return
     */
    @Select("SELECT id FROM notebook_word WHERE word = #{word} " +
            "AND notebook_id IN (SELECT id FROM notebook WHERE learner_id = #{learner_id}) limit 1;")
    Integer findWordExistNotebooks(String word, Integer learner_id);

    /**
     * 修改名称
     * @param name 生词本新名称
     * @param id 生词本id
     * @return
     */
    @Update("UPDATE notebook SET NAME = #{name} , modified = NOW() WHERE id = #{id};")
    Integer editNotebookName(String name, Integer id);


    /**
     * 为生词本添加单词（能返回主键）
     * @param notebookd  生词本id
     * @param wordect 单词实体
     * @return
     */
    @Insert("insert into notebook_word (notebook_id,word,pron_us,pron_uk,paraphrase,created,modified) value " +
            "(#{notebookd},#{wordect.word},#{wordect.pron_us},#{wordect.pron_uk},#{wordect.translation},NOW(),NOW())")
    @Options(useGeneratedKeys = true,keyProperty = "wordect.id",keyColumn = "id")
    int addWordEct(WordEct wordect, Integer notebookd);

    /**
     * 生词本单词数目加一
     * @param notebook_id
     */
    @Update("update notebook set count = count + 1, modified = NOW() where id = #{notebook_id}; ")
    void notebookCountPlus(Integer notebook_id);

    /**
     * 生词本单词数目减一
     * @param notebook_id
     */
    @Update("update notebook set count = count - 1, modified = NOW() where id = #{notebook_id}; ")
    void notebookCountMinus(Integer notebook_id);

    /**
     * 通过notebook_word的id查询notebook_id
     * @param id
     * @return
     */
    @Select("select notebook_id from notebook_word where id = #{id}")
    Integer findIdByNotebookWordId(Integer id);
}
