package com.ahuiali.word.common.utils;

import com.ahuiali.word.common.enums.BookHardEnum;
import com.ahuiali.word.common.enums.BookLongEnum;
import com.ahuiali.word.common.enums.BookTagEnum;

/**
 * BookTagUtil
 * 书籍类别工具类
 *
 * @author ZhengChaoHui
 * @date 2020/12/30 12:09
 */
public class BookTagUtil {

    private static final String SPLIT = "_";

    /**
     * 获取书籍类别的中文
     * @param tag 类别组合
     * @return string
     */
    public static String getCnTag(String tag) {
        String[] tags = tag.split(SPLIT);
        StringBuilder sb = new StringBuilder(3);
        if (tags.length == 3) {
            sb.append(BookTagEnum.getEnumDesc(tags[0])).append(SPLIT)
                    .append(BookHardEnum.getEnumDesc(tags[1])).append(SPLIT)
                    .append(BookLongEnum.getEnumDesc(tags[2]));
        }


        return sb.toString();
    }
}
