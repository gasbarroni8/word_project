package com.ahuiali.word.dto;

import lombok.Data;

/**
 * ChapterDto
 * 章节基本信息
 * @author ZhengChaoHui
 * @date 2020/12/29 17:34
 */
@Data
public class ChapterDto {

    /**
     * i
     */
    private Integer id;

    /**
     * 章节名称
     */
    private String chapterName;

    /**
     * 章节号
     */
    private Integer chapterIndex;
}
