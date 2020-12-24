package com.ahuiali.word.dto;

import lombok.Data;

/**
 * BaseInfoDto
 * 主页基础信息dto
 * @author ZhengChaoHui
 * @date 2020/12/24 12:29
 */
@Data
public class BaseInfoDto {

    /**
     * 词书id
     */
    private Integer id;

    /**
     * 词书名
     */
    private String name;

    /**
     * 词书单词数目
     */
    private Integer count;

    /**
     * 词书已背单词数目
     */
    private Integer learnedCount;

    /**
     * 需复习单词数
     */
    private Integer reviewCount;
}
