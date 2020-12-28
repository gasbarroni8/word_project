package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.common.utils.PageUtil;

public interface NotebookService {

    Response<?> findAllNotebookByLearnerId(Integer learnerId);

    Response<?> addNotebook(Notebook notebook);

    Response<?> removeNotebook(Integer id);

    Response<?> removeWord(Integer id);

    Response<?> addWord(Integer notebookId, String word);

    Response<?> listWord(Integer notebookId, PageUtil pageUtil);

    Response<?> editNotebook(String trim, Integer learnerId);

    Response<?> addWordEct(Integer id, WordEct wordect);
}
