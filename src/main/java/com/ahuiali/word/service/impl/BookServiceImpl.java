package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.mapper.BookMapper;
import com.ahuiali.word.pojo.Book;
import com.ahuiali.word.pojo.Chapter;
import com.ahuiali.word.pojo.Paragraph;
import com.ahuiali.word.service.BookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ahuiali.word.common.Constant.ZERO;

@Transactional
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    StringRedisTemplate template;

    private final static String INIT_LOC = "0001_0";

    /**
     * 查询热门书籍
     *
     * @return
     */
    @Override
    public Response<?> findHotBooks() {
        Response<List<Book>> response = Response.success();
        List<Book> books = bookMapper.getHotBooks();
        if (books.size() <= 0) {
            return Response.result(Constant.Error.BOOK_HOT_EMPTY);
        }
        response.setData(books);
        return response;
    }

    /**
     * 分类查询书籍
     *
     * @param tag
     * @param pageUtil
     * @return
     */
    @Override
    public Response<?> getBooksByTag(String tag, PageUtil pageUtil) {
        Response<List<Book>> response = Response.success();
        List<Book> books = bookMapper.getBooksByTag(tag, pageUtil);
        if (books.size() <= 0) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        response.setData(books);
        return response;
    }

    /**
     * 获取书籍详情
     *
     * @param indexBook 书籍号
     * @param learnerId 用户id
     * @return
     */
    @Override
    public Response<?> getBookDetail(Integer indexBook, Integer learnerId) {
        Response<Book> response = Response.success();
        Book book = bookMapper.findBookByIndex(indexBook, learnerId);
        if (book == null) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        // TODO 有空看看这里的注释代码
        //查询该用户有没有将该本书加入书架
//        String  count = bookMapper.findIsAddThisBook(index_book,learner_id);
        //有则将is_add设置为1
//        if(count > 0)
//            bookJson.setIs_add(1);
        response.setData(book);
        return response;
    }

    /**
     * 获取用户书架，不需要分页
     *
     * @param learnerId
     * @return
     */
    @Override
    public Response<?> getMyBooks(Integer learnerId) {
        Response<List<Book>> response = Response.success();
        List<Book> books = bookMapper.getMyBooks(learnerId);
        if (books.size() <= ZERO) {
            return Response.result(Constant.Error.BOOKSHELF_EMPTY);
        }
        response.setData(books);
        return response;
    }

    /**
     * 根据章节号返回该章节内容
     *
     * @param chapterIndex
     * @return
     */
    @Override
    public Response<?> findParasByChapterIndex(Integer chapterIndex) {
        Response<Chapter> response = Response.success();
        Chapter chapter = bookMapper.getParaByChapterIndex(chapterIndex);
        if (chapter == null) {
            return Response.result(Constant.Error.CHAPTER_EMPTY);
        }
        response.setData(chapter);
        return response;
    }

    /**
     * 根据书号获取其所有的章节名称
     *
     * @param indexBook
     * @param pageUtil
     * @return
     */
    @Override
    public Response<?> getAllChapterByBookIndex(Integer indexBook, PageUtil pageUtil) {
        Response<List<Chapter>> response = Response.success();
        List<Chapter> chapters = bookMapper.getAllChapterByBookIndex(indexBook, pageUtil);
        if (chapters.size() <= ZERO) {
            return Response.result(Constant.Error.CHAPTER_LIST_EMPTY);
        }
        response.setData(chapters);
        return response;
    }

    /**
     * 加入书架
     *
     * @param indexBook 书号
     * @param learnerId 用户id
     * @return
     */
    @Override
    public Response<?> addBook(Integer indexBook, Integer learnerId) {
        Response<?> response = Response.success();
        String lastest_loc = indexBook + INIT_LOC;
        Integer count = bookMapper.addBook(indexBook, learnerId, lastest_loc);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_ADD_ERROR);
        }
        return response;
    }

    /**
     * 从书架中删除书本
     *
     * @param learnerId
     * @param indexBook
     * @return
     */
    @Override
    public Response<?> removeBook(Integer learnerId, Integer indexBook) {
        Response<?> response = Response.success();
        Integer count = bookMapper.removeBook(learnerId, indexBook);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_REMOVE_ERROR);
        }
        return response;
    }

    /**
     * 更新最新阅读位置
     *
     * @param learnerId
     * @param bookIndex
     * @param lastestLoc
     * @return
     */
    @Override
    public Response<?> updateBook(Integer learnerId, Integer bookIndex, String lastestLoc) {
        Response<?> response = Response.success();
        Integer count = bookMapper.updateBook(learnerId, bookIndex, lastestLoc);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_LOC_UPDATE_ERROR);
        }
        return response;
    }


    /**
     * 根据段落id查询相应的中文翻译
     *
     * @param paraId
     * @return
     */
    @Override
    public Response<?> findParaCNById(Integer paraId) {
        Response<Paragraph> response = Response.success();
        Paragraph paragraph = bookMapper.findParaCNById(paraId);
        if (paragraph == null) {
            return Response.result(Constant.Error.PARA_CN_EMPTY);
        }
        response.setData(paragraph);
        return response;
    }

    /**
     * 根据书名查询书籍
     *
     * @param bookName
     * @return
     */
    @Override
    public Response<?> getBooksByName(String bookName) {
        Response<List<Book>> response = Response.success();
        List<Book> books = bookMapper.getBooksByName(bookName);
        if (books.size() <= ZERO) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        response.setData(books);
        return response;
    }
}
