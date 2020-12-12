package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.utils.PageUtil;

public interface BookService {
    Response<?> findHotBooks();

    Response<?> getBooksByTag(String tag, PageUtil pageUtil);

    Response<?> getBookDetail(Integer bookIndex, Integer learnerId);

    Response<?> getMyBooks(Integer learnerId);

    Response<?> findParasByChapterIndex(Integer chapter_index);

    Response<?> getAllChapterByBookIndex(Integer index_book, PageUtil pageUtil);

    Response<?> addBook(Integer index_book, Integer learnerId);

    Response<?> removeBook(Integer learnerId, Integer index_book);

    Response<?> updateBook(Integer learnerId, Integer book_index, String lastest_loc);

    Response<?> findParaCNById(Integer para_id);

    Response<?> getBooksByName(String bookName);
}
