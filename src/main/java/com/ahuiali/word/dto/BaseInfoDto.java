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
     * 词书图片地址
     */
    private String img;

    /**
     * 词书已背单词数目
     */
    private Integer learnedCount;

    /**
     * 需复习单词数
     */
    private Integer reviewCount;

    /**
     * 是否邮箱提醒复习，0为否，1为是
     */
    private Integer isNotice;

    /**
     * 今日复习数
     */
    private Integer todayReviewCount;

    /**
     * 今日学习单词数
     */
    private Integer todayLearnCount;

    /**
     * 今日阅读时长（单位：分钟）
     */
    private Integer todayReadCount;
}
