package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;


public interface WordEctService {
    Response<?> getWordsByPre(String wordRre);

    Response<?> findWordDetail(String word, Integer learner_id);

    Response<?> findWord(String word, Integer learner_id);

    Response<?> findWordDetailNoRedis(String word, Integer learnerId);

    Response<?> findWordNoRedis(String word, Integer learnerId);
}
