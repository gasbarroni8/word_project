package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 生词本
 */
@Data
@TableName(value = "notebook")
public class Notebook implements Serializable {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "learner_id")
    private Integer learnerId;

    /**
     * 生词本名字
     */
    private String name;

    /**
     * 生词本单词数量
     */
    private Integer count;

    /**
     * 创建日期
     */
    private Date created;

    /**
     * 修改日期
     */
    private Date modified;

    /**
     * 生词本单词
     */
    private List<Word> words;
}
