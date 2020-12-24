package com.ahuiali.word.dto;

import lombok.Data;

import java.util.List;

/**
 * ChapterParaDto
 * 章节内容
 * @author ZhengChaoHui
 * @date 2020/12/24 21:32
 */
@Data
public class ChapterParaDto {

    /**
     * 主键id
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

    /**
     * 英文
     */
    private List<ParaEnDto> paragraphs;
}
