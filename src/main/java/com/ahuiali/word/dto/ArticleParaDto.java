package com.ahuiali.word.dto;

import lombok.Data;

import java.util.List;

/**
 * ArticleParaDto
 * 文章内容dto
 * @author ZhengChaoHui
 * @date 2020/12/24 13:17
 */
@Data
public class ArticleParaDto {

    private ArticleDto article;

    private List<String> paragraphs;
}
