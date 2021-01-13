package com.ahuiali.word.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AddNotebookVo
 * 新增生词本vo
 * @author ZhengChaoHui
 * @date 2021/1/9 15:30
 */
@Data
public class AddNotebookVo {

    /**
     * 名称
     */
    @NotBlank
    private String name;
}
