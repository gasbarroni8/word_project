package com.ahuiali.word.service;


import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.dto.NotebookDto;
import com.ahuiali.word.pojo.Notebook;
import com.ahuiali.word.pojo.WordEct;
import com.ahuiali.word.common.utils.PageUtil;

import java.util.List;

public interface NotebookService {

    Response<?> findAllNotebookByLearnerId(Integer learnerId);

    Response<?> addNotebook(NotebookDto notebook, Integer learnerId);

    Response<?> removeNotebook(Integer id);

    Response<?> removeWord(Integer id);

    Response<?> addWord(Integer notebookId, String word);

    Response<?> listWord(Integer notebookId, PageUtil pageUtil);

    Response<?> editNotebook(String trim, Integer notebookId);

    Response<?> addWordEct(Integer id, WordEct wordect);

    Response<?> updateWords(List<Long> ids, Integer learnerId);

    Response<?> getReviewWords(Integer notebookId, PageUtil pageUtil);
}
