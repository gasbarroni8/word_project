package com.ahuiali.word.dto;

import com.ahuiali.word.pojo.Sentence;
import lombok.Data;

import java.util.List;

/**
 * WordDetailDto
 * 单词详情dto
 * @author ZhengChaoHui
 * @date 2021/1/4 23:25
 */
@Data
public class WordDetailDto {

    private Integer id;

    /**
     * 单词
     */
    private String word;

    /**
     * 美式音标
     */
    private String pronUs;

    /**
     * 英式音标
     */
    private String pronUk;

    /**
     * 中文翻译
     */
    private String translation;

    /**
     * 英文翻译
     */
    private String definition;

    /**
     * 柯林斯
     */
    private Integer collins;

    /**
     * 牛津
     */
    private Integer oxford;

    /**
     * 单词分类
     */
    private String tag;

    /**
     * bnc等级
     */
    private Integer bnc;

    /**
     * 使用频率
     */
    private Integer frq;

    /**
     * 变化形式，时态
     */
    private String exchange;

    /**
     * 是否已收藏，未收藏未0，收藏为词书id
     */
    private Integer isAdd;

    /**
     * 例句
     */
    private List<Sentence> sentences;

    /**
     * 例句id列表
     */
    private String sens;
}
