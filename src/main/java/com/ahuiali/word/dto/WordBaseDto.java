package com.ahuiali.word.dto;

import lombok.Data;

/**
 * WordBaseDto
 * 单词基本数据
 * @author ZhengChaoHui
 * @date 2020/12/28 16:36
 */
@Data
public class WordBaseDto {

    /**
     * id
     */
    private Integer id;

    /**
     * 单词
     */
    private String word;

    /**
     * 英式发音
     */
    private String pronUk;

    /**
     * 美式发音
     */
    private String pronUs;

    /**
     * 释义
     */
    private String mean;

    /**
     * 所属单词本_单词中间表
     */
    private Integer notebookWordId;
}
