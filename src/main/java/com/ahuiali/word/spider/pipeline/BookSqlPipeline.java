package com.ahuiali.word.spider.pipeline;

import com.ahuiali.word.common.utils.BookTagUtil;
import com.ahuiali.word.common.utils.CatePicDownloadUtil;
import com.ahuiali.word.common.utils.ImagePathUtil;
import com.ahuiali.word.mapper.BookMapper;
import com.ahuiali.word.pojo.Book;
import com.ahuiali.word.pojo.Chapter;
import com.ahuiali.word.pojo.Paragraph;
import com.ahuiali.word.service.BookService;
import com.ahuiali.word.service.ChapterService;
import com.ahuiali.word.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

import static com.ahuiali.word.common.constant.SpiderConstant.*;

/**
 * BookSqlPipeline
 *
 * @author ZhengChaoHui
 * @date 2021/1/2 22:40
 */
@Component
public class BookSqlPipeline implements Pipeline {

    @Autowired
    private BookService bookService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ParagraphService paragraphService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        String value = resultItems.get(KEY);
        if (BOOK.equals(value)) {
            List<String> titles = resultItems.get(TITLES);
            List<String> tags = resultItems.get(TAG);
            List<String> summaries = resultItems.get(SUMMARY);
            List<String> images = resultItems.get(IMAGES);
            List<String> authors = resultItems.get(AUTHOR);
            List<Book> books = new ArrayList<>(titles.size());
            for (int i = 0; i < titles.size(); i++) {
                Book book = new Book();
                book.setAuthor(authors.get(i));
                book.setImg("/img/book/" + ImagePathUtil.getImageName(images.get(i)));
                // 下载
//                CatePicDownloadUtil.downloadPicture(images.get(i), ImagePathUtil.getImageName(images.get(i)));
                book.setIndexBook(i + 1);
                book.setIsHot(0);
                book.setTitle(titles.get(i));
                String[] realTag = BookTagUtil.getTags(tags.get(i));
                book.setTag(realTag[0]);
                book.setHard(realTag[1]);
                book.setLength(realTag[2]);
                book.setSummary(summaries.get(i));
                books.add(book);
            }
            bookService.saveOrUpdateBatch(books);

        } else if (CHAPTER.equals(value)) {
            Integer bookIndex = resultItems.get("bookIndex");
            List<String> chapterNames = resultItems.get("chapterName");
            List<Chapter> chapters = new ArrayList<>(chapterNames.size());
            for (int i = 0; i < chapterNames.size(); i++) {
                Chapter chapter = new Chapter();
                chapter.setBookIndex(bookIndex);
                chapter.setChapterName(chapterNames.get(i));
                chapter.setChapterIndex(bookIndex * 10000 + (i + 1));
                chapters.add(chapter);
            }
            chapterService.saveOrUpdateBatch(chapters);

        } else if (PARAGRAPH.equals(value)) {
            List<String> ens = resultItems.get(ENS);
            List<String> cns = resultItems.get(CNS);
            Integer chapterIndex = resultItems.get(CHAPTER_INDEX);
            List<Paragraph> paragraphs = new ArrayList<>(ens.size());
            for (int i = 0; i < ens.size(); i++) {
                Paragraph paragraph = new Paragraph();
                paragraph.setParaEn(ens.get(i));
                paragraph.setParaCn(cns.get(i));
                paragraph.setChapterIndex(chapterIndex);
                paragraph.setParaIndex(i + 1);
                paragraphs.add(paragraph);
            }
            paragraphService.saveOrUpdateBatch(paragraphs);
        }
    }
}
