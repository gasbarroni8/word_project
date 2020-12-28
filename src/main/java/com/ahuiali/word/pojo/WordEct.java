package com.ahuiali.word.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 标准单词类
 */
@Data
public class WordEct implements Serializable {

    private Integer id;

    private String word;

    private String pronUs;

    private String pronUk;

    private String translation;

    private Integer notebook_word_id = 0;


}
