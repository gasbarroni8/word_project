package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.ArticleDto;
import com.ahuiali.word.dto.BaseInfoDto;
import com.ahuiali.word.dto.IndexDto;
import com.ahuiali.word.service.ArticleService;
import com.ahuiali.word.service.IndexService;
import com.ahuiali.word.service.WordbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * IndexServiceImpl
 *
 * @author ZhengChaoHui
 * @date 2020/12/24 12:38
 */
@Service
@Transactional
public class IndexServiceImpl implements IndexService {

    @Autowired
    private WordbookService wordbookService;

    @Autowired
    private ArticleService articleService;

    @Override
    public Response<IndexDto> getIndexDto(Integer learnerId) {
        Response<IndexDto> response = Response.success();
        BaseInfoDto baseInfoDto = wordbookService.getMemorizingWordbookAndReviewCount(learnerId);
        List<ArticleDto> lastestArticle = articleService.getLastestArticle();
        IndexDto indexDto = new IndexDto(baseInfoDto, lastestArticle);
        response.setData(indexDto);
        return response;
    }
}
