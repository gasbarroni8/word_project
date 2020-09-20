package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.WordEctDetailJson;
import com.ahuiali.word.json.WordEctJson;

public interface WordEctService {
    Response<?> getWordsByPre(String wordpre);

    WordEctDetailJson findWordDetail(String word, Integer learner_id);

    WordEctJson findWord(String word, Integer learner_id);

    WordEctDetailJson findWordDetailNoRedis(String word, Integer learnerId);

    WordEctJson findWordNoRedis(String word, Integer learnerId);
}
