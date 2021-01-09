package com.ahuiali.word.dto;

import lombok.Data;

/**
 * WordDto
 * 词书单词基本数据
 * @author ZhengChaoHui
 * @date 2021/1/8 0:36
 */
@Data
public class WordDto {

    /**
     * id
     */
    private Integer id;

    /**
     * 单词
     */
    private String word;

    /**
     * 释义
     */
    private String paraphrase;
}
