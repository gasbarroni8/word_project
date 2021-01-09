package com.ahuiali.word.dto;

import lombok.Data;

/**
 * WordbookDto
 * 词书dto
 * @author ZhengChaoHui
 * @date 2021/1/7 21:33
 */
@Data
public class WordbookDto {

    /**
     * 词书id
     */
    private Integer id;

    /**
     * 词书学习单词数
     */
    private Integer learnedCount;

    /**
     * 词书图片地址
     */
    private String img;

    /**
     * 词书描述
     */
    private String summary;

    /**
     * 词书名称
     */
    private String name;

    /**
     * 词书单词总数
     */
    private Integer count;

    /**
     * 是否是当前计划，0否、1是
     */
    private Integer isMemorizing;
}
