package com.ahuiali.word.dto;

import lombok.Data;

/**
 * WordPreDto
 * 根据单词前缀找单词
 * @author ZhengChaoHui
 * @date 2021/1/4 22:52
 */
@Data
public class WordPreDto {

    /**
     * 单词
     */
    private String word;

    /**
     * 释义
     */
    private String translation;
}
