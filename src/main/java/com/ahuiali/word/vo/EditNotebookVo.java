package com.ahuiali.word.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * EditNotebookVo
 * 修改生词本vo
 * @author ZhengChaoHui
 * @date 2021/1/9 15:05
 */
@Data
public class EditNotebookVo {

    /**
     * 新名称
     */
    @NotBlank
    private String name;
}
