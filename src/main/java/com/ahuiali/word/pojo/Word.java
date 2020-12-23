package com.ahuiali.word.pojo;

/**
 * Created by shkstart on 2019/9/18
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 词书单词表，不是词表！
 */
@Data
public class Word implements Serializable {
    private int id;

    private int wordbookId;

    private String word;

    private String paraphrase;

    private String pronUs;

    private String pronUk;

    private Integer memorizedCount;

}
