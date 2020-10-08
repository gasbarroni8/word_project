package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 单词类型枚举
 * @author ZhengChaoHui
 * @Date 2020/10/8 19:16
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum WordTypeEnum {

    /**
     * 单词类型，1 未背，2 记忆中，3 已掌握
     */
    UN_MEMORIZE(1, "未背"),
    MEMORIZING(2, "记忆中"),
    MEMORIZED(3, "已掌握");


    private Integer type;
    private String msg;
}
