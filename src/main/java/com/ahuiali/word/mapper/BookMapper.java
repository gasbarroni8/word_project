package com.ahuiali.word.mapper;

import com.ahuiali.word.dto.*;
import com.ahuiali.word.pojo.Book;
import com.ahuiali.word.pojo.Chapter;
import com.ahuiali.word.pojo.Paragraph;
import com.ahuiali.word.common.utils.PageUtil;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BookMapper extends BaseMapper<Book> {

    @Select("SELECT id,title,img,author,tag,summary,book_index,is_hot FROM book;")
    List<Book> getAllBooks();



    /**
     * 获取热门书籍
     *
     * @return list
     */
    @Select("select id, title, book_index as indexBook, img from book where is_hot = 1;")
    List<BookDto> getHotBooks();

    /**
     * 根据分类找书籍
     *
     * @param tag      分类
     * @param pageUtil 分页
     * @return
     */
    @Select("select id,title,book_index as indexBook,img from book where tag like #{tag} " +
            "LIMIT #{pageUtil.offset},#{pageUtil.size};")
    List<BookDto> getBooksByTag(String tag, PageUtil pageUtil);

    /**
     * @param book_index
     * @param learner_id
     * @return
     */
    @Select("select latest_loc from learner_book where learner_id = #{learner_id} and book_index = #{book_index}")
    String findIsAddThisBook(Integer book_index, Integer learner_id);

    /**
     * 根据书籍号查询书籍详情，并且返回是否加入
     *
     * @param indexBook 书籍号
     * @param learnerId 用户id
     * @return list
     */
    @Select("SELECT id,title,book_index as indexBook, img, tag, summary, author, " +
            " (SELECT latest_loc FROM learner_book WHERE learner_id = #{learnerId} AND book_index = #{indexBook} limit 1) as latestLoc " +
            " FROM book WHERE book_index = #{indexBook} limit 1;")
    @Results({
            @Result(property = "chapters", column = "indexBook", javaType = List.class,
            many = @Many(select = "com.ahuiali.word.mapper.BookMapper.getAllChapterByBookIndex"))
    })
    BookDetailDto findBookByIndex(Integer indexBook, Integer learnerId);

    /**
     * 根据书籍号查询所有章节
     *
     * @param bookIndex 书籍号
     * @return list
     */
    @Select("SELECT id, chapter_name as chapterName,chapter_index as chapterIndex FROM book_chapter " +
            "WHERE book_index = #{bookIndex} ")
    List<ChapterDto> getAllChapterByBookIndex(Integer bookIndex);

    /**
     * 查询我的书籍,结果按照时间排序，最新排在最前
     * @param learnerId 用户id
     * @return list
     */
    @Select("SELECT lb.`latest_loc` as latestLoc, b.`id` , b.`img`, b.`title`, b.`book_index` as indexBook " +
            "FROM learner_book lb " +
            "INNER JOIN book b " +
            "ON (lb.`learner_id` = #{learner_id} AND lb.`book_index` = b.`book_index`) ORDER BY lb.`modified` DESC;")
    List<MyBookDto> getMyBooks(Integer learnerId);

//    //根据章节号返回所有段落
//    @Select("SELECT id,para_en,para_cn FROM chapter_paragraph WHERE chapter_index = #{chapter_index};")
//    List<Paragraph> getParaByChapterIndex(Integer chapter_index);

    /**
     * 根据章节号返回所有段落
     * @param chapterIndex 章节号
     * @return chapter
     */
    @Select("SELECT id, chapter_name, chapter_index FROM book_chapter WHERE chapter_index = #{chapterIndex};")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "chapterName", column = "chapter_name"),
            @Result(property = "chapterIndex", column = "chapter_index"),
            @Result(property = "paragraphs", column = "chapter_index", javaType = List.class,
                    many = @Many(select = "com.ahuiali.word.mapper.BookMapper.getAllParasByChapterIndex"))
    })
    ChapterParaDto getParaByChapterIndex(Integer chapterIndex);

    /**
     * 查询某章节的所有英语段落
     * @param chapterIndex 章节号
     * @return list
     */
    @Select("select id, para_en as paraEn from chapter_paragraph where chapter_index = #{chapterIndex};")
    List<ParaEnDto> getAllParasByChapterIndex(Integer chapterIndex);

    @Insert("insert into learner_book (learner_id, book_index, latest_loc) " +
            "values (#{learnerId},#{indexBook},#{lastestLoc});")
    Integer addBook(Integer indexBook, Integer learnerId, String lastestLoc);

    @Delete("delete from learner_book where learner_id = #{learnerId} and book_index = #{indexBook};")
    Integer removeBook(Integer learnerId, Integer indexBook);

    @Update("update learner_book set latest_loc = #{latestLoc},modified = NOW() " +
            "where learner_id = #{learnerId} and book_index = #{bookIndex};")
    Integer updateBook(Integer learnerId, Integer bookIndex, String latestLoc);

    /**
     * 根据段落id查询段落翻译
     * @param paraId 段落id
     * @return Paragraph
     */
    @Select("SELECT para_cn FROM chapter_paragraph WHERE id = #{paraId};")
    String findParaCNById(Integer paraId);

    /**
     * 根据书名查询书籍
     *
     * @param bookName 书名
     * @return list
     */
    @Select("select id, title, book_index as indexBook, img from book where title like concat('%',#{bookName},'%');")
    List<BookDto> getBooksByName(String bookName);
}
