package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.BaseInfoDto;

public interface WordbookService {
    Response<?> getWordbooks();

    Response<?> getWordbookDetail(Integer id, Integer wordbookId);

    Response<?> getWords(Integer id, int curr, int size);

    Response<?> addWordbook(Integer learnerId, Integer wordbookId) throws Exception;

    Response<?> findMyWordbooks(Integer learnerId);

    Response<?> updateWordbookPlan(Integer learnerId, Integer wordbookId);

    Response<?> myMemorizingWordbook(Integer learnerId);

    Integer findReviewCount( Integer learnerId, Integer wordbookId);

    BaseInfoDto getMemorizingWordbookAndReviewCount(Integer learnerId);
}
