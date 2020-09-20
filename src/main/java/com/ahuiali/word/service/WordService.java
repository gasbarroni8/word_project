package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.JsonBase;
import com.ahuiali.word.json.WordJson;
import com.ahuiali.word.pojo.Word;
import com.ahuiali.word.common.utils.PageUtil;

import java.util.List;

public interface WordService {
    Response<?> getWords(int id, PageUtil pageUtil);

    WordJson myWordbookWords(Integer wordbook_id, Integer learnerId, PageUtil pageUtil, Integer wordsType);

    JsonBase wordTypeChange(Integer learner_id, Integer wordbook_id, Integer id, Integer type);

    Response<?> getReviewWords(Integer learner_id, Integer wordbook_id, PageUtil pageUtil);

    WordJson insertWords(Integer wordbook_id, Integer learner_id,List<Long> ids);

    WordJson updateWords(List<Word> words);

}
