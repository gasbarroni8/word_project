package com.ahuiali.word.dto;

import lombok.Data;

import java.util.Date;

/**
 * ArticleDto
 * 文章dto
 * @author ZhengChaoHui
 * @date 2020/12/24 11:33
 */
@Data
public class ArticleDto {

    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章来源
     */
    private String source;

    /**
     * 文章来源链接
     */
    private String url;

    /**
     * 文章描述
     */
    private String description;

    /**
     * 日期
     */
    private Date date;

    /**
     * 图片地址（只有一张）
     */
    private String image;
}
