package com.ahuiali.word.common.enums;

import lombok.*;

/**
 * 用户状态枚举
 * @author ZhengChaoHui
 * @Date 2020/10/8 19:01
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum StatusEnum {
    /**
     * 状态 0 未激活，1 正常， 2 封禁中
     */
    NOT_ACTIVE(0, "未激活"),
    NORMAL(1, "正常"),
    BLOCKED(2, "封禁中");

    private Integer status;
    private String msg;

}
