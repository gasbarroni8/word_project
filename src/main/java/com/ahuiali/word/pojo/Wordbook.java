package com.ahuiali.word.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@TableName(value = "wordbook")
public class Wordbook implements Serializable {

    /**
     * 词书id
     */
    private Integer id;

    /**
     * 词书名
     */
    private String name;

    /**
     * 简要
     */
    private String summary;

    /**
     * 词书单词数目
     */
    private Integer count;

    /**
     * 词书已背单词数目
     */
    private Integer learnedCount;

    /**
     * 词书img
     */
    private String img;

    /**
     * 创建日期
     */
    private Date created;

    /**
     * 修改日期
     */
    private Date modified;

    /**
     * 我的词书里面的计划(从词库中查询详细时，该字段作为是否已添加)
     */
    private Integer isMemorizing;

    /**
     * 需复习单词数
     */
    private Integer reviewCount;

    /**
     * 词书词表单词
     */
    private List<Word> words;

}
