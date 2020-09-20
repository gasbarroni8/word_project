package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.json.WordEctJson;

public interface WordEctService {
    Response<?> getWordsByPre(String wordpre);

    Response<?> findWordDetail(String word, Integer learner_id);

    Response<?> findWord(String word, Integer learner_id);

    Response<?> findWordDetailNoRedis(String word, Integer learnerId);

    Response<?> findWordNoRedis(String word, Integer learnerId);
}
