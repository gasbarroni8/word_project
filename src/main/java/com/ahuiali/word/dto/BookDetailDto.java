package com.ahuiali.word.dto;

import lombok.Data;

/**
 * BookDetailDto
 * 书籍详情
 * @author ZhengChaoHui
 * @date 2020/12/30 11:42
 */
@Data
public class BookDetailDto {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String summary;

    /**
     * 作者
     */
    private String author;

    /**
     * 阅读位置（如果没加入就为null）
     */
    private String latestLoc;

    /**
     * 是否加入了书架，1为加入，0为未加入
     */
    private Integer isAdd;

    /**
     * 封面
     */
    private String img;

    /**
     * 标签、类别
     */
    private String tag;

    /**
     * 书籍号
     */
    private Integer indexBook;

}
