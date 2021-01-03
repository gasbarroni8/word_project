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

    /**
     * 将小说爬虫网页的小说分类数据格式化
     * @param tag 类别
     * @return 分类 难度 长度
     */
    public static String[] getTags(String tag) {
        //            分类：社会,        难度：大学,        长度：短篇
        // 1. 去除空格
        // 2. ','分隔
        // 3. '：'分隔，获取第二个
        // 最终获取社会 大学 短篇 分别加入到不同字段
        String s1 = tag.replaceAll(" ", "");
        String[] split1 = s1.split(",");
        String[] result = new String[3];
        for (int i = 0; i < split1.length; i++) {
            result[i] = split1[i].split("：")[1];
        }
        return result;
    }

}
