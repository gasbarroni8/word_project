package com.ahuiali.word.controller;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.service.BookService;
import com.ahuiali.word.common.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    /**
     * 跳转至书城
     *
     * @return
     */
    @RequestMapping(value = "/gotoBookStore")
    public String gotoBookStore() {
        return "/book/bookstore";
    }

    /**
     * 跳转至书籍详情
     *
     * @return
     */
    @RequestMapping(value = "/gotoBookDetail")
    public String gotoBookDetail() {
        return "/book/bookDetail";
    }

    /**
     * 跳转至阅读界面
     *
     * @return
     */
    @RequestMapping(value = "/gotoRead")
    public String gotoRead() {
        return "/book/read";
    }

    /**
     * 跳转至书架
     *
     * @return
     */
    @RequestMapping(value = "/gotoShelf")
    public String gotoShelf() {
        return "/book/bookshelf";
    }

    /**
     * 初始化，自动显示热门书籍,不需要分页
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/hotbooks", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> hotbooks() {
        return bookService.findHotBooks();
    }

    /**
     * 根据段落id获取释义
     *
     * @param paraId
     * @return
     */
    @RequestMapping(value = "/getCn/{paraId}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getCn(@PathVariable("paraId") Integer paraId) {
        return bookService.findParaCNById(paraId);
    }

    /**
     * 根据分类查询书籍
     *
     * @param tag
     * @param
     * @return
     */
    @RequestMapping(value = "/tagbooks/{tag}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getBooksByTag(@PathVariable("tag") String tag, @RequestBody PageUtil pageUtil) {
        tag = tag.replaceAll("0", "%");
        pageUtil.renew();
        return bookService.getBooksByTag(tag, pageUtil);
    }

    /**
     * 根据名字查询书籍
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/getBookByName/{bookName}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getBooksByName(@PathVariable("bookName") String bookName) {
        return bookService.getBooksByName(bookName);
    }

    /**
     * 查询书籍详情
     *
     * @param indexBook
     * @return
     */
    @RequestMapping(value = "/bookDetail/{indexBook}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getBookDetail(@PathVariable("indexBook") Integer indexBook, HttpSession session) {

        Integer learner_id = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return bookService.getBookDetail(indexBook, learner_id);
    }

    /**
     * 书架，不需要分页，到时品字展示
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "/mybook", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> getMyBooks(HttpSession session) {

        Integer learner_id = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return bookService.getMyBooks(learner_id);
    }


    /**
     * 根据书籍号查询所有章节
     *
     * @param indexBook
     * @param pageUtil
     * @return
     */
    @RequestMapping(value = "/mybook/chapters/{indexBook}")
    public @ResponseBody
    Response<?> listChapters(@PathVariable("indexBook") Integer indexBook,
                             PageUtil pageUtil) {
        pageUtil.renew();
        return bookService.getAllChapterByBookIndex(indexBook, pageUtil);
    }

    /**
     * 用户浏览文章(只有英)
     *
     * @param chapterIndex
     * @return
     */
    @RequestMapping(value = "/read/{chapter_index}")
    public @ResponseBody
    Response<?> read(@PathVariable("chapter_index") Integer chapterIndex) {
//        Integer index_book = chapter_index/10000;
        //获取文章内容
        return bookService.findParasByChapterIndex(chapterIndex);
    }


    /**
     * 加入书架
     *
     * @param indexBook
     * @param session
     * @return
     */
    @RequestMapping(value = "/addBook/{index_book}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> addBook(@PathVariable("index_book") Integer indexBook,
                        HttpSession session) {

        Integer learnerId = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return bookService.addBook(indexBook, learnerId);
    }

    /**
     * 从书架中删除书本
     *
     * @param indexBook
     * @param session
     * @return
     */
    @RequestMapping(value = "/remove/{index_book}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> removeBook(@PathVariable("index_book") Integer indexBook,
                           HttpSession session) {
        Integer learner_id = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return bookService.removeBook(learner_id, indexBook);
    }

    /**
     * 更新最新阅读位置
     *
     * @param bookIndex
     * @param lastestLoc
     * @param session
     * @return
     */
    @RequestMapping(value = "/update/{book_index}/{lastest_loc}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> updateBook(@PathVariable("book_index") Integer bookIndex,
                           @PathVariable("lastest_loc") String lastestLoc,
                           HttpSession session) {
        Integer learner_id = (Integer) session.getAttribute(Constant.LEARNER_ID);
        return bookService.updateBook(learner_id, bookIndex, lastestLoc);
    }
}
