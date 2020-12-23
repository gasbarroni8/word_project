package com.ahuiali.word.dto;

import lombok.Data;

/**
 * MyBookDto
 * 书架书籍信息dto添加Book相关dto
 * @author ZhengChaoHui
 * @date 2020/12/24 1:08
 */
@Data
public class MyBookDto extends BookDto {

    private String lastestLoc;
}
