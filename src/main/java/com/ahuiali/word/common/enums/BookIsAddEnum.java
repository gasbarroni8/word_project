package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookIsAddEnum
 * 书籍是否加入枚举
 * @author ZhengChaoHui
 * @date 2020/12/30 11:48
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum BookIsAddEnum {
    /**
     *
     */
    UN_ADD(0, "未加入"),
    ADD(1, "已加入");

    /**
     * 状态
     */
    private Integer status;
    /**
     * 描述
     */
    private String desc;

}
