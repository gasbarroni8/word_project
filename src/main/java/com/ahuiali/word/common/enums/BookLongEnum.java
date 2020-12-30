package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookLongEnum
 * 书籍长度
 * @author ZhengChaoHui
 * @date 2020/12/30 12:08
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum BookLongEnum {

    L("l","长篇"),
    M("m","中篇"),
    S("s","短篇");
    /**
     * 类别
     */
    private String tag;

    /**
     * 描述
     */
    private String desc;

    public static String getEnumDesc(String tag) {
        for (BookLongEnum value : BookLongEnum.values()) {
            if (value.getTag().equals(tag)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
