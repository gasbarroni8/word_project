package com.ahuiali.word.dto;

import lombok.Data;

/**
 * UserSettingDto
 * 用户设置信息（存放到redis中）
 * @author ZhengChaoHui
 * @date 2021/1/13 12:36
 */
@Data
public class LearnerSettingDto {

    /**
     * 用户id
     */
    private Integer learnerId;

    /**
     * 用户邮箱
     */
    private String email;

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
