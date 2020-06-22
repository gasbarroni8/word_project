package com.ahuiali.word.service;


import com.ahuiali.word.json.JsonBase;
import com.ahuiali.word.json.NotebookJson;
import com.ahuiali.word.json.WordEctJson;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.utils.PageUtil;

public interface NotebookService {


    NotebookJson findAllNotebookByLearnerId(Integer learner_id);

    NotebookJson addNotebook(Notebook notebook);

    JsonBase removeNotebook(Integer id);

    JsonBase removeWord(Integer id);

    JsonBase addWord(Integer notebook_id, String word);

    NotebookJson listWord(Integer notebook_id, PageUtil pageUtil);

    NotebookJson editNotebook(String trim, Integer learner_id);

    WordEctJson addWordEct(Integer id, WordEct wordect);
}
