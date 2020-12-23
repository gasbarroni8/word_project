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

    private String title;

    private String img;

    private Integer indexBook;
}
