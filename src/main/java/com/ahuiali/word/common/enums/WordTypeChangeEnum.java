package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 单词类型转移
 * @author ZhengChaoHui
 * @Date 2020/10/8 22:28
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum WordTypeChangeEnum {
    /**
     * 记忆中->掌握 : 1, 未背->掌握 : 2, 掌握->未背 : 3
     */
    MEMORIZING_TO_MEMORIZED(1, "记忆中->掌握"),
    UN_MEMORIZE_TO_MEMORIZED(2, "未背->掌握"),
    MEMORIZED_TO_UN_MEMORIZE(3, "掌握->未背");

    private Integer type;
    private String msg;

}
