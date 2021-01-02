package com.ahuiali.word.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * MyBookDto
 * 书架书籍信息dto添加Book相关dto
 * @author ZhengChaoHui
 * @date 2020/12/24 1:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyBookDto extends BookDto {

    /**
     * 上次阅读位置
     */
    private String latestLoc;

    /**
     * 小说阅读位置最新更新时间
     */
    private Date modified;
}
