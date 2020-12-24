package com.ahuiali.word.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * IndexDto
 * 主页信息
 * @author ZhengChaoHui
 * @date 2020/12/24 12:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexDto {

    /**
     * 基础数据
     */
    private BaseInfoDto baseInfo;

    /**
     * 最新文章
     */
    private List<ArticleDto> articles;
}
