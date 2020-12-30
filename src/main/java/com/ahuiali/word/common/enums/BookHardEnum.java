package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookHardEnum
 * 书籍难度
 *
 * @author ZhengChaoHui
 * @date 2020/12/30 12:07
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum BookHardEnum {

    U("u", "大学"),
    H("h", "高中"),
    M("m", "初中");

    /**
     * 类别
     */
    private String tag;

    /**
     * 描述
     */
    private String desc;

    public static String getEnumDesc(String tag) {
        for (BookHardEnum value : BookHardEnum.values()) {
            if (value.getTag().equals(tag)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
