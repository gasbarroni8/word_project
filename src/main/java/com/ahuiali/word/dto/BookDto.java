package com.ahuiali.word.dto;

import lombok.Data;

/**
 * BookDto
 * 书籍基本信息dto
 * @author ZhengChaoHui
 * @date 2020/12/24 1:05
 */
@Data
public class BookDto {
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String img;

    /**
     * 书籍号
     */
    private Integer indexBook;
}
