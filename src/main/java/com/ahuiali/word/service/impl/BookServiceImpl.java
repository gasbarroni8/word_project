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
     * @param index_book 书籍号
     * @param learner_id 用户id
     * @return
     */
    @Override
    public Response<?> getBookDetail(Integer index_book, Integer learner_id) {
        Response<Book> response = Response.success();
        Book book = bookMapper.findBookByIndex(index_book, learner_id);
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
     * @param learner_id
     * @return
     */
    @Override
    public Response<?> getMyBooks(Integer learner_id) {
        Response<List<Book>> response = Response.success();
        List<Book> books = bookMapper.getMyBooks(learner_id);
        if (books.size() <= ZERO) {
            return Response.result(Constant.Error.BOOKSHELF_EMPTY);
        }
        response.setData(books);
        return response;
    }

    /**
     * 根据章节号返回该章节内容
     *
     * @param chapter_index
     * @return
     */
    @Override
    public Response<?> findParasByChapterIndex(Integer chapter_index) {
        Response<Chapter> response = Response.success();
        Chapter chapter = bookMapper.getParaByChapterIndex(chapter_index);
        if (chapter == null) {
            return Response.result(Constant.Error.CHAPTER_EMPTY);
        }
        response.setData(chapter);
        return response;
    }

    /**
     * 根据书号获取其所有的章节名称
     *
     * @param index_book
     * @param pageUtil
     * @return
     */
    @Override
    public Response<?> getAllChapterByBookIndex(Integer index_book, PageUtil pageUtil) {
        Response<List<Chapter>> response = Response.success();
        List<Chapter> chapters = bookMapper.getAllChapterByBookIndex(index_book, pageUtil);
        if (chapters.size() <= ZERO) {
            return Response.result(Constant.Error.CHAPTER_LIST_EMPTY);
        }
        response.setData(chapters);
        return response;
    }

    /**
     * 加入书架
     *
     * @param index_book 书号
     * @param learner_id 用户id
     * @return
     */
    @Override
    public Response<?> addBook(Integer index_book, Integer learner_id) {
        Response<?> response = Response.success();
        String lastest_loc = index_book + INIT_LOC;
        Integer count = bookMapper.addBook(index_book, learner_id, lastest_loc);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_ADD_ERROR);
        }
        return response;
    }

    /**
     * 从书架中删除书本
     *
     * @param learner_id
     * @param index_book
     * @return
     */
    @Override
    public Response<?> removeBook(Integer learner_id, Integer index_book) {
        Response<?> response = Response.success();
        Integer count = bookMapper.removeBook(learner_id, index_book);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_REMOVE_ERROR);
        }
        return response;
    }

    /**
     * 更新最新阅读位置
     *
     * @param learner_id
     * @param book_index
     * @param lastest_loc
     * @return
     */
    @Override
    public Response<?> updateBook(Integer learner_id, Integer book_index, String lastest_loc) {
        Response<?> response = Response.success();
        Integer count = bookMapper.updateBook(learner_id, book_index, lastest_loc);
        if (count <= ZERO) {
            response = Response.result(Constant.Error.BOOK_LOC_UPDATE_ERROR);
        }
        return response;
    }


    /**
     * 根据段落id查询相应的中文翻译
     *
     * @param para_id
     * @return
     */
    @Override
    public Response<?> findParaCNById(Integer para_id) {
        Response<Paragraph> response = Response.success();
        Paragraph paragraph = bookMapper.findParaCNById(para_id);
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
