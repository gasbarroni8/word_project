package com.ahuiali.word.dto;

import lombok.Data;

/**
 * NotebookDto
 * 生词本基础数据
 * @author ZhengChaoHui
 * @date 2021/1/9 14:00
 */
@Data
public class NotebookDto {

    /**
     * id
     */
    private Integer id;

    /**
     * 生词本名称
     */
    private String name;

    /**
     * 生词本单词数量
     */
    private Integer count;
}
