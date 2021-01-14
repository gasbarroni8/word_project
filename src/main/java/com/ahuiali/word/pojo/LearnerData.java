package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * LearnerData
 * learner_data表
 * @author ZhengChaoHui
 * @date 2021/1/14 11:49
 */
@Data
@TableName(value = "learner_data")
public class LearnerData {

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "learner_id")
    private Integer learnerId;

    /**
     * 今日复习数
     */
    @TableField(value = "today_review_count")
    private Integer todayReviewCount;

    /**
     * 今日学习单词数
     */
    @TableField(value = "today_learn_count")
    private Integer todayLearnCount;

    /**
     * 今日阅读时长（单位：分钟）
     */
    @TableField(value = "today_read_count")
    private Integer todayReadCount;

    /**
     * 日期
     */
    @TableField(value = "date")
    private Date date;
}
