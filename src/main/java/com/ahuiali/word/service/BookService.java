package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.BookJson;
import com.ahuiali.word.json.ChapterJson;
import com.ahuiali.word.json.JsonBase;
import com.ahuiali.word.json.ParagraphJson;
import com.ahuiali.word.common.utils.PageUtil;

public interface BookService {
    Response<?> findHotBooks();

    Response<?> getBooksByTag(String tag, PageUtil pageUtil);

    Response<?> getBookDetail(Integer book_index, Integer learner_id);

    Response<?> getMyBooks(Integer learner_id);

    Response<?> findParasByChapterIndex(Integer chapter_index);

    Response<?> getAllChapterByBookIndex(Integer index_book, PageUtil pageUtil);

    Response<?> addBook(Integer index_book, Integer learner_id);

    Response<?> removeBook(Integer learner_id, Integer index_book);

    Response<?> updateBook(Integer learner_id, Integer book_index, String lastest_loc);

    Response<?> findParaCNById(Integer para_id);

    Response<?> getBooksByName(String bookName);
}
