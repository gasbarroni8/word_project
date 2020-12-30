package com.ahuiali.word.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * BookTagEnum
 * 书籍类型枚举
 *
 * @author ZhengChaoHui
 * @date 2020/12/30 12:00
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public enum BookTagEnum {

    /**
     *
     */
    SOC("soc", "社会"),
    LOV("lov", "爱情"),
    BIO("bio", "传记"),
    SUS("sus", "悬疑"),
    CHI("chi", "儿童"),
    SCF("scf", "科幻"),
    MAG("mag", "魔幻"),
    ADV("adv", "冒险"),
    COM("com", "喜剧"),
    HIS("his", "历史");

    /**
     * 类别
     */
    private String tag;

    /**
     * 描述
     */
    private String desc;

    public static String getEnumDesc(String tag) {
        for (BookTagEnum value : BookTagEnum.values()) {
            if (value.getTag().equals(tag)) {
                return value.getDesc();
            }
        }
        return null;
    }
}
