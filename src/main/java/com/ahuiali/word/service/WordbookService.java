package com.ahuiali.word.service;

import com.ahuiali.word.common.resp.Response;

public interface WordbookService {
    Response<?> getWordbooks();

    Response<?> getWordbookDetail(Integer id, Integer learner_id);

    Response<?> getWords(Integer id, int curr, int size);

    Response<?> addWordbook(Integer learnerId, Integer wordbook_id) throws Exception;

    Response<?> findMyWordbooks(Integer learnerId);

    Response<?> updateWordbookPlan(Integer learnerId, Integer wordbook_id);

    Response<?> myMemorizingWordbook(Integer learnerId);

    Integer findReviewCount( Integer learnerId, Integer wordbook_id);

    Response<?> getMemorizingWordbookAndReviewCount(Integer learnerId);
}
