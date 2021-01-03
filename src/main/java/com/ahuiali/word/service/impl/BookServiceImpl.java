package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.constant.Constant;
import com.ahuiali.word.common.enums.BookIsAddEnum;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.BookTagUtil;
import com.ahuiali.word.dto.*;
import com.ahuiali.word.mapper.BookMapper;
import com.ahuiali.word.pojo.Book;
import com.ahuiali.word.pojo.Chapter;
import com.ahuiali.word.pojo.Paragraph;
import com.ahuiali.word.service.BookService;
import com.ahuiali.word.common.utils.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ahuiali.word.common.constant.Constant.ZERO;

/**
 * 书籍impl
 */
@Transactional
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    StringRedisTemplate template;

    private final static String INIT_LOC = "0001_0";

    /**
     * 查询热门书籍
     *
     * @return resp
     */
    @Override
    public Response<?> findHotBooks() {
        Response<List<BookDto>> response = Response.success();
        List<BookDto> books = bookMapper.getHotBooks();
        if (books.size() <= 0) {
            return Response.result(Constant.Error.BOOK_HOT_EMPTY);
        }
        response.setData(books);
        return response;
    }

    /**
     * 分类查询书籍
     *
     * @param tag      类别
     * @param pageUtil 分页
     * @return
     */
    @Override
    public Response<?> getBooksByTag(String tag, PageUtil pageUtil) {
        Response<List<BookDto>> response = Response.success();
        List<BookDto> books = bookMapper.getBooksByTag(tag, pageUtil);
        if (books.size() <= 0) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        response.setData(books);
        return response;
    }

    /**
     * 获取书籍详情
     *
     * @param bookIndex 书籍号
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> getBookDetail(Integer bookIndex, Integer learnerId) {
        Response<BookDetailDto> response = Response.success();
        BookDetailDto bookDetailDto = bookMapper.findBookByIndex(bookIndex, learnerId);
        if (bookDetailDto == null) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        // 如果找不到阅读位置则说明尚未加入书架
        if (bookDetailDto.getLatestLoc() == null || "".equals(bookDetailDto.getLatestLoc())) {
            bookDetailDto.setIsAdd(BookIsAddEnum.UN_ADD.getStatus());
        } else {
            bookDetailDto.setIsAdd(BookIsAddEnum.ADD.getStatus());
        }
        // 将书籍类别替换为中文
        bookDetailDto.setTag(BookTagUtil.getCnTag(bookDetailDto.getTag()));
        response.setData(bookDetailDto);
        return response;
    }

    /**
     * 获取用户书架，不需要分页
     *
     * @param learnerId 用户id
     * @return resp
     */
    @Override
    public Response<?> getMyBooks(Integer learnerId) {
        Response<List<MyBookDto>> response = Response.success();
        List<MyBookDto> books = bookMapper.getMyBooks(learnerId);
        if (books.size() <= ZERO) {
            return Response.result(Constant.Error.BOOKSHELF_EMPTY);
        }
        response.setData(books);
        return response;
    }

    /**
     * 根据章节号返回该章节内容
     *
     * @param chapterIndex 章节号
     * @return resp
     */
    @Override
    public Response<?> findParasByChapterIndex(Integer chapterIndex) {
        Response<ChapterParaDto> response = Response.success();
        ChapterParaDto chapterParaDto = bookMapper.getParaByChapterIndex(chapterIndex);
        if (chapterParaDto != null && chapterParaDto.getParagraphs() == null) {
            return Response.result(Constant.Error.CHAPTER_EMPTY);
        }
        response.setData(chapterParaDto);
        return response;
    }

    /**
     * 根据书号获取其所有的章节名称
     *
     * @param indexBook 小说号
     * @return resp
     */
    @Override
    public Response<?> getAllChapterByBookIndex(Integer indexBook) {
        Response<List<ChapterDto>> response = Response.success();
        List<ChapterDto> chapters = bookMapper.getAllChapterByBookIndex(indexBook);
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
     * @return resp
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
     * @param learnerId 用户id
     * @param indexBook 书号
     * @return resp
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
     * @param learnerId  用户id
     * @param bookIndex  书号
     * @param lastestLoc 最新位置
     * @return resp
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
     * @param paraId 段落号
     * @return resp
     */
    @Override
    public Response<?> findParaCNById(Integer paraId) {
        Response<String> response = Response.success();
        String paraMean = bookMapper.findParaCNById(paraId);
        if (paraMean == null || "".equals(paraMean)) {
            return Response.result(Constant.Error.PARA_CN_EMPTY);
        }
        response.setData(paraMean);
        return response;
    }

    /**
     * 根据书名查询书籍
     *
     * @param bookName 书名
     * @return resp
     */
    @Override
    public Response<?> getBooksByName(String bookName) {
        Response<List<BookDto>> response = Response.success();
        List<BookDto> books = bookMapper.getBooksByName(bookName);
        if (books.size() <= ZERO) {
            return Response.result(Constant.Error.BOOK_NOT_FOUNDED);
        }
        response.setData(books);
        return response;
    }
}
